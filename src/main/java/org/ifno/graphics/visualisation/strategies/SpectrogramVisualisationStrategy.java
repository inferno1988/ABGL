package org.ifno.graphics.visualisation.strategies;

import android.graphics.*;
import org.ifno.SpectrogramActivity;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:02
 * Palamarchuk Maksym © 2013
 */
public class SpectrogramVisualisationStrategy implements VisualisationStrategy<float[]> {
    private Bitmap destinationBitmap;
    private final Paint spectrogramPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    private final Paint cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    private final Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    private final Canvas canvas;
    private int visualisationStep = 1;
    private static final String SPECTROGRAM_STRATEGY_NAME = "Spectrogram";
    private final static float MAXIMUM_SIGNAL_LEVEL = 6000000.0f;
    public static final int DB_THRESHOLD = 50;
    private final float canvasWidth;
    private final float canvasHeight;
    private final Matrix matrix = new Matrix();

    {
        cursorPaint.setColor(Color.WHITE);
        redPaint.setColor(Color.RED);
        redPaint.setTextSize(20.f);
        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        matrix.setTranslate(50, 0);
    }

    private float[] matrixValues= new float[9];
    private float[] hsv = new float[]{255, 1.0f, 1.0f};

    public SpectrogramVisualisationStrategy(Bitmap destinationBitmap) {
        this.destinationBitmap = destinationBitmap;
        canvas = new Canvas(destinationBitmap);
        //canvas.translate(Math.abs(visualisationStep)*-1, 0);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
    }

    @Override
    public synchronized void visualise(float[] data) {
        int limit;
        if (canvasHeight > data.length)
            limit = data.length;
        else
            limit = (int) canvasHeight;

        drawLegend(data.length);

        canvas.setMatrix(matrix);
        canvas.drawLine(5, 0, 5, canvasHeight, cursorPaint);
        for (int i = limit; i > 0; i--) {
            int currentMagnitude = (int) ((Math.abs(10 * Math.log10(data[i] / MAXIMUM_SIGNAL_LEVEL)) / DB_THRESHOLD) * 256);
            hsv[0] = currentMagnitude;
            spectrogramPaint.setColor(Color.HSVToColor(hsv));
            canvas.drawPoint(0, limit - i, spectrogramPaint);
        }
        matrix.postTranslate(1, 0);
        matrix.getValues(matrixValues);
        if (matrixValues[Matrix.MTRANS_X] > canvasWidth)
            matrix.setTranslate(50, 0);
    }

    private void drawLegend(int dataLength) {
        canvas.save();
        canvas.setMatrix(null);
        canvas.drawText("Hz", 5, 20, redPaint);
        canvas.drawLine(50, 0, 50, canvasHeight, redPaint);
        for (int i = 100; i < canvasHeight; i +=100) {
            canvas.drawLine(40, i, 50, i, redPaint);
            final String freq = String.valueOf((int)((SpectrogramActivity.SAMPLE_RATE / dataLength / 2) * (canvasHeight - i)));
            final float textWidth = redPaint.measureText(freq);
            canvas.rotate(-90.f, 10, i - (textWidth/2));
            canvas.drawText(freq, 10-textWidth, i, redPaint);
            canvas.rotate(90.f, 10, i - (textWidth/2));
        }
        canvas.restore();
    }

    @Override
    public synchronized Bitmap getVisualisationResult() {
        return destinationBitmap;
    }

    @Override
    public synchronized void releaseResources() {
        destinationBitmap = null;
    }

    @Override
    public synchronized void toggleColor() {
        Random rnd = new Random();
        cursorPaint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public String toString() {
        return SPECTROGRAM_STRATEGY_NAME;
    }
}
