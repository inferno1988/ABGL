package org.ifno.graphics.visualisation.strategies;

import android.graphics.*;
import android.util.Log;
import org.ifno.SpectrogramActivity;
import org.ifno.audio.AudioPoller;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:10
 * Palamarchuk Maksym © 2013
 */
public class SimpleSpectrumVisualisationStrategy implements VisualisationStrategy<float[]> {

    public static final int DB_THRESHOLD = 50;
    private final String simpleName = SimpleSpectrumVisualisationStrategy.class.getSimpleName();
    private Bitmap[] backBuffer = {null, null};
    private AtomicReference<Bitmap> resultBitmap = new AtomicReference<Bitmap>(null);
    private Canvas canvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    private float[] magnitudePeaks;
    private float maximum = 6000000.0f;
    private static final String HZ = "Hz";
    private final float hzWidth;

    public static final float TEXT_SIZE = 20.f;

    {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(TEXT_SIZE);
        paint.setStrokeWidth(1.f);
    }

    private boolean visibleBitmap = true;
    private int canvasHeight;
    private int canvasWidth;

    public SimpleSpectrumVisualisationStrategy(Bitmap backBuffer) {
        this.backBuffer[0] = backBuffer;
        this.backBuffer[1] = backBuffer.copy(Bitmap.Config.ARGB_8888, true);
        this.canvas = new Canvas(this.backBuffer[getIntValue(visibleBitmap)]);
        this.canvasHeight = this.canvas.getHeight();
        this.canvasWidth = this.canvas.getWidth();
        this.resultBitmap.set(this.backBuffer[getIntValue(visibleBitmap)]);
        this.hzWidth = this.paint.measureText(HZ);
    }

    @Override
    public synchronized void visualise(float[] data) {
        canvas.setBitmap(backBuffer[getIntValue(!visibleBitmap)]);
        canvas.drawColor(Color.DKGRAY);
        if (magnitudePeaks == null) {
            magnitudePeaks = new float[data.length];
            Arrays.fill(magnitudePeaks, 1f);
        }
        int limit;
        if (canvas.getWidth() > data.length)
            limit = data.length;
        else
            limit = canvas.getWidth();
        Path path = new Path();
        path.moveTo(0, canvasHeight - 50.f);
        for (int i = 0; i < limit; i++) {
            float currentMagnitude = (float) (canvasHeight - ((Math.abs(10 * Math.log10(data[i] / maximum)) / DB_THRESHOLD) * canvasHeight));
            if (currentMagnitude > magnitudePeaks[i]) {
                magnitudePeaks[i] = currentMagnitude;
            } else {
                if ((canvasHeight - magnitudePeaks[i]) <= canvasHeight - 60.f)
                    magnitudePeaks[i] -= 10f;
                else
                    magnitudePeaks[i] = 50.f;
            }
            path.lineTo(i, canvasHeight - magnitudePeaks[i]);
            if (data[i] > maximum)
                maximum = data[i];
        }
        path.lineTo(canvasWidth, canvasHeight - 50.f);
        path.close();
        canvas.drawPath(path, paint);
        canvas.drawLine(0, canvasHeight - 50.f, canvasWidth, canvasHeight - 50.f, paint);
        for (int i = 100; i < limit; i+=100) {
            canvas.drawLine(i, canvasHeight - 50.f, i, canvasHeight - 40f, paint);
            final String freq = String.valueOf((SpectrogramActivity.SAMPLE_RATE / data.length / 2) * i);
            final float textWidth = paint.measureText(freq);
            canvas.drawText(freq, i - (textWidth/2), canvasHeight - 20.f, paint);
        }
        canvas.drawText(HZ, canvasWidth/2, canvasHeight, paint);
        resultBitmap.set(backBuffer[getIntValue(!visibleBitmap)]);
        swapBuffers();
    }

    private void swapBuffers() {
        visibleBitmap = !visibleBitmap;
    }

    @Override
    public synchronized Bitmap getVisualisationResult() {
        return resultBitmap.get();
    }

    @Override
    public void releaseResources() {
        synchronized (this) {
            canvas = null;
            backBuffer[0].recycle();
            backBuffer[0] = null;
            backBuffer[1].recycle();
            backBuffer[1] = null;
            backBuffer = null;
        }
    }

    private static int getIntValue(boolean booleanValue) {
        return booleanValue ? 1 : 0;
    }
}
