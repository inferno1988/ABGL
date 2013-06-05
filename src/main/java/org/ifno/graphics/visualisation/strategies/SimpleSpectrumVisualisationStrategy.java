package org.ifno.graphics.visualisation.strategies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:10
 * Palamarchuk Maksym © 2013
 */
public class SimpleSpectrumVisualisationStrategy implements VisualisationStrategy<float[]> {

    private Bitmap[] backBuffer = {null, null};
    private Bitmap resultBitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    private float[] magnitudePeaks;

    {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.f);
    }

    private boolean visibleBitmap = true;
    private int canvasHeight;

    public SimpleSpectrumVisualisationStrategy(Bitmap backBuffer) {
        this.backBuffer[0] = backBuffer;
        this.backBuffer[1] = backBuffer.copy(Bitmap.Config.ARGB_8888, true);
        this.canvas = new Canvas(this.backBuffer[getIntValue(visibleBitmap)]);
        this.canvasHeight = this.canvas.getHeight();
        this.resultBitmap = this.backBuffer[getIntValue(visibleBitmap)];
    }

    @Override
    public void visualise(float[] data) {
        synchronized (this) {
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

            for (int i = 0; i < limit; i++) {
                float currentMagnitude = (float) (30 * Math.log10(data[i]));
                paint.setColor(Color.WHITE);
                canvas.drawLine(i, canvasHeight, i, canvasHeight - currentMagnitude, paint);
                if (currentMagnitude > magnitudePeaks[i]) {
                    magnitudePeaks[i] = currentMagnitude;
                } else {
                    magnitudePeaks[i] -= 0.1f;
                }
                paint.setColor(Color.RED);
                canvas.drawPoint(i, canvasHeight - magnitudePeaks[i], paint);
            }
            resultBitmap = backBuffer[getIntValue(!visibleBitmap)];
            swapBuffers();
        }
    }

    private void swapBuffers() {
        visibleBitmap = !visibleBitmap;
    }

    @Override
    public Bitmap getVisualisationResult() {
        synchronized (this) {
            return resultBitmap;
        }
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
