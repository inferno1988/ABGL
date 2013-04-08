package org.ifno.graphics.primitives;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.*;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 31.03.13
 * Time: 21:33
 * Palamarchuk Maksym © 2013
 */
public class Point implements GraphicObject {

    private android.graphics.Point point;
    private String name;
    private Paint paint;
    private CompositeGraphicObject parent;

    public Point(GraphicObject parent, String name, android.graphics.Point point) {
        this.name = name;
        this.point = point;
    }

    public Point(GraphicObject parent, String name, Paint paint, android.graphics.Point point) {
        this.name = name;
        this.paint = paint;
        this.point = point;
    }

    public Point(android.graphics.Point point) {
        this.point = point;
    }

    public Point(android.graphics.Point point, String name, CompositeGraphicObject parent, Paint paint) {
        this.point = point;
        this.paint = paint;
        this.name = name;
        this.parent = parent;
    }

    public Point() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint parentsPaint = getParent().getPaint();
        if (parentsPaint != null)
            canvas.drawPoint(point.x, point.y, parentsPaint);
        else
            canvas.drawPoint(point.x, point.y, paint);
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
        throw new RuntimeException("Point getRect()? Are you kidding me? STUB!");
    }

    @Override
    public void setRect(Rect rect) {
        throw new RuntimeException("Point setRect()? Are you kidding me? STUB!");
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
        this.point.offset(deltaX, deltaY);
    }

    @Override
    public void resize(Rect newRect) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public android.graphics.Point getPoint() {
        return point;
    }

    public void setPoint(android.graphics.Point point) {
        this.point = point;
    }

    public void setPoint(int x, int y) {
        point.x = x;
        point.y = y;
    }

    @Override
    public Point cloneGraphicObject() {
        Point point = new Point();
        point.name = this.name;
        point.paint = new Paint(this.paint);
        point.parent = this.parent;
        point.point = new android.graphics.Point(this.point);
        return point;
    }
}
