package org.ifno.graphics.visualisation.strategies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:02
 * Palamarchuk Maksym © 2013
 */
public class SpectrogramVisualisationStrategy implements VisualisationStrategy<float[]> {
    private Bitmap destinationBitmap;
    private final Color backgroundColor;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Canvas canvas;
    private int visualisationStep = 1;
    private final Context context;

    public SpectrogramVisualisationStrategy(Bitmap destinationBitmap, Color backgroundColor, Context context) {
        this.destinationBitmap = destinationBitmap;
        this.backgroundColor = backgroundColor;
        this.context = context;
        canvas = new Canvas(destinationBitmap);
        canvas.translate(Math.abs(visualisationStep)*-1, 0);
    }

    public SpectrogramVisualisationStrategy(Bitmap destinationBitmap, Color backgroundColor, int visualisationStep, Context context) {
        this(destinationBitmap, backgroundColor, context);
        this.visualisationStep = visualisationStep;
        canvas.translate(Math.abs(visualisationStep)*-1, 0);
    }

    @Override
    public Bitmap visualise(float[] data) {
        return destinationBitmap;
    }

    @Override
    public Bitmap getVisualisationResult() {
        return destinationBitmap;
    }

    @Override
    public void releaseResources() {
        destinationBitmap = null;
    }


}
