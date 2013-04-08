package org.ifno;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import org.ifno.graphics.interfaces.GraphicObject;
import org.ifno.graphics.GraphicPrimitivesPrototypeFactory;
import org.ifno.graphics.primitives.GraphicsContainer;
import org.ifno.graphics.primitives.Line;

public class MainActivity extends Activity {

    private static String TAG = "FFTAnalyzer";
    private DrawingView drawingView;
    private GraphicsContainer graphicsContainer;
    private Display display;
    private Rect displayRect = new Rect();
    private GraphicPrimitivesPrototypeFactory graphicPrimitivesPrototypeFactory = new GraphicPrimitivesPrototypeFactory();

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        drawingView = (DrawingView) findViewById(R.id.view);
        display = getWindowManager().getDefaultDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                redPaint.setColor(Color.RED);
                greenPaint.setColor(Color.GREEN);
                greenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                greenPaint.setStrokeWidth(10);
                Rect rect = getScreenDimensions();
                GraphicObject graphicObject;
                graphicsContainer = new GraphicsContainer("Main", redPaint, rect);
                graphicPrimitivesPrototypeFactory.addPrototype(GraphicsContainer.class, new GraphicsContainer("Main", greenPaint, rect));
                GraphicsContainer redObjectsContainer = graphicPrimitivesPrototypeFactory.createGraphicsObject(GraphicsContainer.class);
                graphicPrimitivesPrototypeFactory.addPrototype(Line.class, new Line(new Paint(), new Rect(0, 0, 200, 200), "Line"));
                for (int i = 0; i < rect.width(); i += 30 ) {
                    graphicObject = graphicPrimitivesPrototypeFactory.createGraphicsObject(Line.class);
                    graphicObject.move(i, 0);
                    graphicsContainer.addChild(graphicObject);
                    graphicObject = graphicPrimitivesPrototypeFactory.createGraphicsObject(Line.class);
                    graphicObject.move(i, 200);
                    redObjectsContainer.addChild(graphicObject);
                }
                graphicsContainer.addComposite(redObjectsContainer);
                drawingView.setGraphicsContainer(graphicsContainer);
            }
        });
    }

    private Rect getScreenDimensions() {
        display.getRectSize(displayRect);
        return displayRect;
    }
}

