package org.ifno.graphics.primitives;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.CompositeGraphicObject;
import org.ifno.graphics.interfaces.GraphicObject;
import org.ifno.graphics.visualisation.strategies.VisualisationStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:33
 * Palamarchuk Maksym © 2013
 */
public class BitmapPrimitive implements GraphicObject {

    private String name;
    private CompositeGraphicObject parent;
    private Rect rect;
    private VisualisationStrategy visualisationStrategy;
    private Paint paint;

    public BitmapPrimitive(Rect rect) {
        this.rect = rect;
    }

    private BitmapPrimitive() {
    }

    @Override
    public void draw(Canvas canvas) {
        if (visualisationStrategy != null) {
            final Bitmap visualisationResult = visualisationStrategy.getVisualisationResult();
            canvas.drawBitmap(visualisationResult, rect.left, rect.top, paint);
        }
        else
            throw new IllegalStateException("bitmap is null, can't proceed drawing.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CompositeGraphicObject getParent() {
        return parent;
    }

    @Override
    public void setParent(CompositeGraphicObject parent) {
        this.parent = parent;
    }

    @Override
    public Rect getRect() {
        return rect;
    }

    @Override
    public void setRect(Rect rect) {
        this.rect = rect;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void move(int deltaX, int deltaY) {
        rect.offset(deltaX, deltaY);
    }

    @Override
    public void resize(Rect newRect) throws IllegalArgumentException {
        if (newRect == null)
            throw new IllegalArgumentException("While resizing this " + this.getClass().getSimpleName() + ", new size looks like null... Sad :(");
        this.rect = newRect;
    }

    @Override
    public BitmapPrimitive cloneGraphicObject() {
        BitmapPrimitive bitmapPrimitive = new BitmapPrimitive();
        bitmapPrimitive.name = this.name;
        bitmapPrimitive.rect = new Rect(this.rect);
        bitmapPrimitive.paint = new Paint(this.paint);
        bitmapPrimitive.visualisationStrategy = this.visualisationStrategy;
        bitmapPrimitive.parent = this.parent;
        return bitmapPrimitive;
    }

    public VisualisationStrategy getVisualisationStrategy() {
        return visualisationStrategy;
    }

    public void setVisualisationStrategy(VisualisationStrategy visualisationStrategy) {
        this.visualisationStrategy = visualisationStrategy;
    }
}
