package org.ifno.graphics.primitives;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.*;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 05.04.13
 * Time: 21:32
 * Palamarchuk Maksym © 2013
 */
public class Rectangle implements GraphicObject {

    private String name;
    private CompositeGraphicObject parent;
    private Rect rect;
    private Paint paint;

    public Rectangle(CompositeGraphicObject parent, String name, Rect rect, Paint paint) {
        this.parent = parent;
        this.name = name;
        this.rect = rect;
        this.paint = paint;
    }

    private Rectangle() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint parentsPaint = getParent().getPaint();
        if (parentsPaint != null)
            canvas.drawRect(rect, parentsPaint);
        else
            canvas.drawRect(rect, paint);
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
    public void resize(Rect newRect) {
        this.rect = newRect;
    }

    @Override
    public Rectangle cloneGraphicObject() {
        Rectangle rectangle = new Rectangle();
        rectangle.paint = new Paint(this.paint);
        rectangle.rect = new Rect(this.rect);
        rectangle.name = this.name;
        rectangle.parent = this.parent;
        return rectangle;
    }
}
