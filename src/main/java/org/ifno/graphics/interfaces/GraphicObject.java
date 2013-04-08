package org.ifno.graphics.interfaces;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 31.03.13
 * Time: 21:21
 * Palamarchuk Maksym © 2013
 */
public interface GraphicObject extends Cloneable<GraphicObject> {
    public void draw(Canvas canvas);

    public String getName();
    public void setName(String name);

    public CompositeGraphicObject getParent();
    public void setParent(CompositeGraphicObject parent);

    public Rect getRect();
    public void setRect(Rect rect);

    public Paint getPaint();
    public void setPaint(Paint paint);

    public void move(int deltaX, int deltaY);
    public void resize(Rect newRect);
}
