package org.ifno.graphics.visualisation.strategies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:10
 * Palamarchuk Maksym © 2013
 */
public class SimpleSpectrumVisualisationStrategy implements VisualisationStrategy<float[]> {

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    private float[] dataSnapshot;

    {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.f);
    }

    public SimpleSpectrumVisualisationStrategy(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
    }

    @Override
    public void visualise(float[] data) {
        synchronized (this) {
            bitmap.eraseColor(Color.BLACK);
            if (dataSnapshot == null)
                dataSnapshot = data;
            int limit;
            if (canvas.getWidth() > data.length)
                limit = data.length;
            else
                limit = canvas.getWidth();

            for (int i = 0; i < limit; i++) {
                float v = data[i];
                canvas.drawLine(i, canvas.getHeight(), i, (float) (canvas.getHeight() - (30 * Math.log10(v))), paint);
            }
        }
    }

    @Override
    public Bitmap getVisualisationResult() {
        synchronized (this) {
            return bitmap;
        }
    }

    @Override
    public void releaseResources() {
        synchronized (this) {
            canvas = null;
            bitmap.recycle();
            bitmap = null;
        }
    }
}
