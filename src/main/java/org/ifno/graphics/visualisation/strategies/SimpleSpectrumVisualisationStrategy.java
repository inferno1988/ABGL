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
        paint.setStrokeWidth(10.f);
    }

    private float multiplier = .0001f;

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
            for (int i = 0; i < canvas.getWidth()/10; i++) {
                float v = data[i];
/*                if ((v * multiplier) > canvas.getHeight())
                    v = (canvas.getHeight() / multiplier);*/
                if (dataSnapshot[i] > v) {
                    dataSnapshot[i] -= 5.f / multiplier;
/*                    if (dataSnapshot[i] < 0) {
                        dataSnapshot[i] = 0;
                    }*/
                    canvas.drawLine(i*10, canvas.getHeight(), i*10, canvas.getHeight() - (dataSnapshot[i] * multiplier), paint);
                } else {
                    canvas.drawLine(i*10, canvas.getHeight(), i*10, canvas.getHeight() - (v * multiplier), paint);
                    dataSnapshot[i] = v;
                }
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
