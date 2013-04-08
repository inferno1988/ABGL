package org.ifno.graphics.interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 31.03.13
 * Time: 21:31
 * Palamarchuk Maksym © 2013
 */
public interface CompositeGraphicObject extends GraphicObject {
    public void addChild(GraphicObject graphicObject);
    public void addComposite(CompositeGraphicObject compositeGraphicObject);
    public GraphicObject remove(String name);
    public boolean remove(GraphicObject graphicObject);
}
