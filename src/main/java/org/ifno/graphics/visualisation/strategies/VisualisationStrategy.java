package org.ifno.graphics.visualisation.strategies;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:08
 * Palamarchuk Maksym © 2013
 */
public interface VisualisationStrategy<InputDataType> {
    public void visualise(InputDataType data);
    public Bitmap getVisualisationResult();
    public void releaseResources();
    public void toggleColor();
}
