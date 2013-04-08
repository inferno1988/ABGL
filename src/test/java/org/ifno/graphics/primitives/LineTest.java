package org.ifno.graphics.primitives;

import android.graphics.Paint;
import android.graphics.Rect;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 08.04.13
 * Time: 0:01
 * Palamarchuk Maksym © 2013
 */
@RunWith(MockitoJUnitRunner.class)
public class LineTest {
    @Mock
    private Paint paint;
    private String name = new String("testInstance");
    @Mock
    private Rect rect;
    private Line line = new Line(paint, rect, name);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDraw() throws Exception {

    }

    @Test
    public void testCloneGraphicObject() throws Exception {
        Line line1 = line.cloneGraphicObject();
        Assert.assertNotSame(line, line1);
    }
}
