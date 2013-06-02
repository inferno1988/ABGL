package org.ifno.graphics;

import org.ifno.graphics.interfaces.*;
import org.ifno.graphics.interfaces.Cloneable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 05.04.13
 * Time: 22:03
 * Palamarchuk Maksym © 2013
 */
public final class GraphicPrimitivesPrototypeFactory {

    private static final int INITIAL_CAPACITY_OF_PROTOTYPE_LIST = 10;
    private final ConcurrentHashMap<Class<? extends GraphicObject>, org.ifno.graphics.interfaces.Cloneable<? extends GraphicObject>> graphicObjectClassList = new ConcurrentHashMap<Class<? extends GraphicObject>, org.ifno.graphics.interfaces.Cloneable<? extends GraphicObject>>(INITIAL_CAPACITY_OF_PROTOTYPE_LIST);

    public void addPrototype(Class<? extends GraphicObject> prototypeKey, org.ifno.graphics.interfaces.Cloneable<? extends GraphicObject> prototype) {
        graphicObjectClassList.put(prototypeKey, prototype);
    }

    public void removePrototype(Class<? extends GraphicObject> prototypeKey) {
        graphicObjectClassList.remove(prototypeKey);
    }

    public <T extends GraphicObject> T createGraphicsObject(Class<? extends GraphicObject> prototypeKey) {
        Cloneable prototype = graphicObjectClassList.get(prototypeKey);
        if (prototype == null)
            throw new IllegalStateException();
        GraphicObject graphicObject = prototype.cloneGraphicObject();
        return (T) graphicObject;
    }
}
