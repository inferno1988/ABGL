package org.ifno;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import edu.emory.mathcs.utils.ConcurrencyUtils;
import org.ifno.audio.AudioPoller;
import org.ifno.audio.FFTResultProcessor;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 17:05
 * Palamarchuk Maksym © 2013
 */
public class SpectrogramActivity extends Activity {
    private static final String LOG_TAG = SpectrogramActivity.class.getSimpleName();
    private static final int FFT_JOB_QUEUE_CAPACITY = 10;
    private static final int SAMPLE_RATE = 44100;
    public static final int UPDATE_INTERVAL_LIMIT = 16;
    private DrawingView drawingView;
    private Button toggleProcessingButton;
    private SeekBar updateIntervalSeekBar;
    public static final int CHANNEL_IN_MONO = AudioFormat.CHANNEL_IN_MONO;
    public static final int ENCODING_PCM_16_BIT = AudioFormat.ENCODING_PCM_16BIT;
    private final BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(FFT_JOB_QUEUE_CAPACITY);
    private final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
    private ExecutorService executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() + 1, 5000L, TimeUnit.MILLISECONDS, blockingQueue, rejectedExecutionHandler);
    private final ArrayBlockingQueue<Future<float[]>> fftJobQueue = new ArrayBlockingQueue<Future<float[]>>(FFT_JOB_QUEUE_CAPACITY);
    private AudioPoller audioPoller;
    private FFTResultProcessor fftResultProcessor;
    private Thread audioPollerThread;
    private Thread resultProcessorThread;
    private CharSequence startButtonText;
    private CharSequence stopButtonText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.spectrogram);
        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        toggleProcessingButton = (Button) findViewById(R.id.startButton);
        updateIntervalSeekBar = (SeekBar) findViewById(R.id.updateIntervalSeekBar);
        drawingView.setUpdateInterval(updateIntervalSeekBar.getProgress());
        updateIntervalSeekBar.setOnSeekBarChangeListener(new UpdateIntervalProgressHandler(drawingView));
        startButtonText = getResources().getText(R.string.startButton);
        stopButtonText = getResources().getText(R.string.stopButton);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawingView.stopDrawing();
    }

    public void toggleProcessing(View view) {
        final CharSequence currentButtonText = toggleProcessingButton.getText();
        if (currentButtonText.equals(startButtonText)) {
            drawingView.startDrawing();
            toggleProcessingButton.setText(stopButtonText);
        } else {
            drawingView.stopDrawing();
            toggleProcessingButton.setText(startButtonText);
        }
    }

    private void initThreads() {
        if (audioPoller == null) {
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN_MONO, ENCODING_PCM_16_BIT);
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

    private final class UpdateIntervalProgressHandler implements SeekBar.OnSeekBarChangeListener {

        private final DrawingView drawingView;

        private UpdateIntervalProgressHandler(DrawingView drawingView) {
            this.drawingView = drawingView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (progress > UPDATE_INTERVAL_LIMIT)
                    drawingView.setUpdateInterval(progress);
                else
                    drawingView.setUpdateInterval(UPDATE_INTERVAL_LIMIT);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}