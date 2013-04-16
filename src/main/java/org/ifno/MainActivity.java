package org.ifno;

import android.app.Activity;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import edu.emory.mathcs.utils.ConcurrencyUtils;
import org.ifno.audio.AudioPoller;
import org.ifno.audio.FFTResultProcessor;
import org.ifno.graphics.GraphicPrimitivesPrototypeFactory;
import org.ifno.graphics.primitives.GraphicsContainer;

import java.util.concurrent.*;

public class MainActivity extends Activity {

    public static final int CAPACITY = 10;
    public static final int SAMPLE_RATE = 44100;
    public static final int CHANNEL_IN_MONO = AudioFormat.CHANNEL_IN_MONO;
    public static final int ENCODING_PCM_16_BIT = AudioFormat.ENCODING_PCM_16BIT;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private DrawingView drawingView;
    private GraphicsContainer graphicsContainer;
    private Display display;
    private Rect displayRect = new Rect();
    private GraphicPrimitivesPrototypeFactory graphicPrimitivesPrototypeFactory = new GraphicPrimitivesPrototypeFactory();
    private final BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(CAPACITY);
    private final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
    private ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, Runtime.getRuntime().availableProcessors() + 1, 0L, TimeUnit.MILLISECONDS, blockingQueue, rejectedExecutionHandler);
    private final ArrayBlockingQueue<Future<float[]>> fftJobQueue = new ArrayBlockingQueue<Future<float[]>>(CAPACITY);
    private AudioPoller audioPoller;
    private FFTResultProcessor fftResultProcessor;
    private Thread audioPollerThread;
    private Thread resultProcessorThread;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.main);
        drawingView = (DrawingView) findViewById(R.id.view);
        display = getWindowManager().getDefaultDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initThreads();
            }
        });
    }

    @Override
    protected void onDestroy() {
        audioPollerThread.interrupt();
        resultProcessorThread.interrupt();
        super.onDestroy();
        Runtime.getRuntime().exit(0);
    }

    private Rect getScreenDimensions() {
        display.getRectSize(displayRect);
        return displayRect;
    }

    private void initThreads() {
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN_MONO, ENCODING_PCM_16_BIT);
        if (!ConcurrencyUtils.isPowerOf2(minBufferSize)) {
            minBufferSize = ConcurrencyUtils.nextPow2(minBufferSize);
        }
        if (audioPoller == null) {
            audioPoller = new AudioPoller(executor, fftJobQueue, MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_IN_MONO, ENCODING_PCM_16_BIT, minBufferSize);
            audioPollerThread = new Thread(audioPoller, "AudioPoller");
            audioPollerThread.setDaemon(true);
            audioPollerThread.start();
        }
        if (fftResultProcessor == null) {
            fftResultProcessor = new FFTResultProcessor(fftJobQueue);
            resultProcessorThread = new Thread(fftResultProcessor, "DataProcessor");
            resultProcessorThread.setDaemon(true);
            resultProcessorThread.start();
        }
    }
}

