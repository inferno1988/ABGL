package org.ifno.graphics.primitives;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.*;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 01.04.13
 * Time: 20:32
 * Palamarchuk Maksym © 2013
 */
public class Line implements GraphicObject {

    private String name;
    private CompositeGraphicObject parent;
    private Rect rect;
    private Paint paint;

    public Line(Paint paint, Rect rect, String name) {
        this.paint = paint;
        this.rect = rect;
        this.name = name;
    }

    public Line() {

    }

    @Override
    public void draw(Canvas canvas) {
        final CompositeGraphicObject parentLocal = getParent();
        if (parentLocal == null)
            throw new IllegalStateException("Can't draw orphan, add some parent to me");
        Paint parentsPaint = parentLocal.getPaint();
        if (parentsPaint != null)
            canvas.drawLine(rect.left, rect.top, rect.right, rect.bottom, parentsPaint);
        else
            canvas.drawLine(rect.left, rect.top, rect.right, rect.bottom, paint);
    }

    @Override
    public Line cloneGraphicObject() {
        Line line = new Line();
        line.name = this.name;
        line.rect = new Rect(this.rect);
        line.paint = new Paint(this.paint);
        line.parent = this.parent;
        return line;
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
    public void resize(Rect newRect) throws IllegalArgumentException{
        if (newRect == null)
            throw new IllegalArgumentException("While resizing this " + this.getClass().getSimpleName() + ", new size look's like null... Sad :(");
        this.rect = newRect;
    }
}
