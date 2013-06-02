package org.ifno.graphics.primitives;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.*;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 31.03.13
 * Time: 21:46
 * Palamarchuk Maksym © 2013
 */
public class GraphicsContainer implements CompositeGraphicObject {
    private ArrayDeque<GraphicObject> graphicsPool = new ArrayDeque<GraphicObject>();

    private String name;
    private CompositeGraphicObject parent;
    private Rect rect;
    private Paint paint;

    public GraphicsContainer(String name, Paint paint, Rect rect) {
        this.name = name;
        this.paint = paint;
        this.rect = rect;
    }

    private GraphicsContainer(CompositeGraphicObject parent, String name, Paint paint, Rect rect, ArrayDeque<GraphicObject> childes) {
        this.parent = parent;
        this.name = name;
        this.paint = paint;
        this.rect = rect;
        graphicsPool = childes;
    }

    private GraphicsContainer() {

    }

    @Override
    public void addChild(GraphicObject graphicObject) {
        graphicObject.setParent(this);
        graphicObject.setPaint(this.paint);
        if (graphicObject.getRect() == null)
            graphicObject.setRect(this.rect);
        if (graphicObject.getPaint() == null)
            graphicObject.setPaint(this.paint);
        if (graphicObject.getName() == null || graphicObject.getName().equals(""))
            graphicObject.setName("child of: "+this.name+" subtype: "+graphicObject.getClass().getSimpleName());
        graphicsPool.add(graphicObject);
    }

    @Override
    public void addComposite(CompositeGraphicObject compositeGraphicObject) {
        graphicsPool.add(compositeGraphicObject);
    }

    @Override
    public GraphicObject remove(String name) {
        return graphicsPool.remove();
    }

    @Override
    public boolean remove(GraphicObject graphicObject) {
        return graphicsPool.remove(graphicObject);
    }

    @Override
    public Iterator<GraphicObject> getChildIterator() {
        return graphicsPool.iterator();
    }

    @Override
    public void draw(Canvas canvas) {
        for (GraphicObject graphicObject : graphicsPool) {
            if (graphicObject != null)
                graphicObject.draw(canvas);
        }
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
        for (GraphicObject graphicObject : graphicsPool) {
            if (graphicObject != null)
                graphicObject.move(deltaX, deltaY);
        }
    }

    @Override
    public void resize(Rect newRect) {
        for (GraphicObject graphicObject : graphicsPool) {
            if (graphicObject != null)
                graphicObject.resize(newRect);
        }
    }

    @Override
    public GraphicsContainer cloneGraphicObject() {
        GraphicsContainer graphicsContainer = new GraphicsContainer();
        ArrayDeque<GraphicObject> children = new ArrayDeque<GraphicObject>(graphicsPool.size());
        for (GraphicObject graphicObject : graphicsPool) {
            if (graphicObject != null)
                children.add(graphicObject.cloneGraphicObject());
        }
        graphicsContainer.graphicsPool = children;
        graphicsContainer.paint = new Paint(this.paint);
        graphicsContainer.name = this.name;
        graphicsContainer.rect = new Rect(this.rect);
        graphicsContainer.parent = this.parent;
        return graphicsContainer;
    }
}
