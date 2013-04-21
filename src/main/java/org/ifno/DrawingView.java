package org.ifno;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import org.ifno.graphics.primitives.GraphicsContainer;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 23.03.13
 * Time: 17:23
 * Palamarchuk Maksym © 2013
 */
public class DrawingView extends View {

    private final int baseUpdateInterval = getResources().getInteger(R.integer.base_update_interval);
    private GraphicsContainer graphicsContainer;
    private TimedInvalidator timedInvalidator = null;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Paint paint = new Paint();

    {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);
    }

    private Thread timedInvalidatorThread;

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
        Log.v(LOG_TAG, "onDraw in");
        if (timedInvalidator == null) {
            timedInvalidator = new TimedInvalidator(this);
            timedInvalidatorThread = new Thread(timedInvalidator);
            timedInvalidatorThread.setDaemon(true);
            timedInvalidatorThread.start();

        }
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(2, 2, canvas.getWidth() - 2, canvas.getHeight() - 2, paint);
        if (graphicsContainer != null)
            graphicsContainer.draw(canvas);
        else {
            canvas.drawText("Nothing to draw, sorry. Time is: " + System.currentTimeMillis(), 0, 20, paint);
            canvas.drawText("Canvas size: w->" + canvas.getWidth() + " h->" + canvas.getHeight(), 0, 50, paint);
            canvas.drawText("Update interval: " + timedInvalidator.getUpdateInterval(), 0, 80, paint);
        }
        Log.v(LOG_TAG, "onDraw out");
    }

    public GraphicsContainer getGraphicsContainer() {
        return graphicsContainer;
    }

    public void setGraphicsContainer(GraphicsContainer graphicsContainer) {
        this.graphicsContainer = graphicsContainer;
    }

    public void setUpdateInterval(long updateInterval) {
        timedInvalidator.setUpdateInterval(updateInterval);
    }

    public void startDrawing() {
        timedInvalidator.startUpdating();
    }

    public void stopDrawing() {
        timedInvalidator.stopUpdating();
    }

    public int getBaseUpdateInterval() {
        return baseUpdateInterval;
    }

    private final class TimedInvalidator implements Runnable {
        private final DrawingView drawingView;
        private final AtomicLong sleepTime;
        private boolean updating = false;

        private TimedInvalidator(DrawingView drawingView) {
            this.drawingView = drawingView;
            sleepTime = new AtomicLong(this.drawingView.getBaseUpdateInterval());
        }

        private long getUpdateInterval() {
            return sleepTime.get();
        }

        private void setUpdateInterval(long updateInterval) {
            this.sleepTime.getAndSet(updateInterval);
        }

        @Override
        public void run() {
            while (true && !Thread.currentThread().isInterrupted()) {
                Log.v(LOG_TAG, "repainting");
                try {
                    if (updating) {
                        drawingView.postInvalidate();
                        Thread.sleep(sleepTime.get());
                    } else {
                        checkForPaused();
                    }
                } catch (InterruptedException e) {
                }
            }
        }

        private void checkForPaused() throws InterruptedException {
            synchronized (this) {
                this.wait();
            }
        }

        public void stopUpdating() {
            updating = false;
        }

        public void startUpdating() {
            synchronized (this) {
                updating = true;
                this.notify();
            }
        }
    }
}
