package org.ifno.graphics.primitives;

import android.graphics.Paint;
import android.graphics.Rect;
import org.ifno.graphics.interfaces.CompositeGraphicObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.easymock.PowerMock.*;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 15.05.13
 * Time: 23:08
 * Palamarchuk Maksym © 2013
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Rectangle.class, Rect.class, Paint.class})
public class RectangleTest {

    @Test
    public void testCloneGraphicObject() throws Exception {
        Rect rect = createMock(Rect.class);
        Rect newRect = createMock(Rect.class);
        Paint paint = createMock(Paint.class);
        Paint newPaint = createMock(Paint.class);
        CompositeGraphicObject compositeGraphicObject = createMock(CompositeGraphicObject.class);

        Rectangle rectangle = new Rectangle(compositeGraphicObject, "Fucking rect", rect, paint);
        expectNew(Rect.class, rect).andReturn(newRect);
        expectNew(Paint.class, paint).andReturn(newPaint);
        replay(rect, Rect.class, paint, Paint.class);

        Rectangle deepCopiedRectangle = rectangle.cloneGraphicObject();
        Assert.assertNotEquals(rectangle, deepCopiedRectangle);
        verify(rect, Rect.class, paint, Paint.class);

    }
}
