package org.ifno.graphics.primitives;

import android.graphics.Paint;
import android.graphics.Rect;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 08.04.13
 * Time: 0:01
 * Palamarchuk Maksym © 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Rect.class })
public class LineTest {
    @Mock
    private Paint paint;
    private String name = new String("testInstance");
    private Rect rect;
    private Rect newRect;
    private Line line = new Line(paint, rect, name);

    @Before
    public void setUp() throws Exception {
        rect = PowerMockito.mock(Rect.class);
        newRect = PowerMockito.mock(Rect.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDraw() throws Exception {

    }

    @Test
    public void testCloneGraphicObject() throws Exception {
        PowerMock.expectNew(Rect.class, rect).andStubReturn(newRect);

        Line line1 = line.cloneGraphicObject();
        Assert.assertNotSame(line, line1);
    }
}
