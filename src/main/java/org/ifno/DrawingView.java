package org.ifno;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import org.ifno.graphics.primitives.GraphicsContainer;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 23.03.13
 * Time: 17:23
 * Palamarchuk Maksym © 2013
 */
public class DrawingView extends View {

    private GraphicsContainer graphicsContainer;
    Paint paint = new Paint();
    {
        paint.setColor(Color.BLACK);
    }

    public DrawingView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (graphicsContainer != null)
            graphicsContainer.draw(canvas);
        else
            canvas.drawText("Nothing to draw, sorry.", 0, 0, paint);
    }

    public GraphicsContainer getGraphicsContainer() {
        return graphicsContainer;
    }

    public void setGraphicsContainer(GraphicsContainer graphicsContainer) {
        this.graphicsContainer = graphicsContainer;
    }
}
