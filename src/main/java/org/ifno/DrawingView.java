package org.ifno;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.ifno.graphics.primitives.GraphicsContainer;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 23.03.13
 * Time: 17:23
 * Palamarchuk Maksym © 2013
 */
public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {

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
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        timedInvalidator = new TimedInvalidator(baseUpdateInterval);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!timedInvalidator.isEnabled()) {
            timedInvalidator.enable();
        }
        timedInvalidatorThread = new Thread(timedInvalidator, "TimedInvalidator");
        timedInvalidatorThread.setDaemon(true);
        timedInvalidatorThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(LOG_TAG, "Surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        timedInvalidator.interrupt();
    }

    private final class TimedInvalidator implements Runnable {
        private final AtomicLong sleepTime;
        private boolean updating = false;
        private volatile boolean enabled = true;

        private TimedInvalidator(long sleepTime) {
            this.sleepTime = new AtomicLong(sleepTime);
        }

        private long getUpdateInterval() {
            return sleepTime.get();
        }

        private void setUpdateInterval(long updateInterval) {
            this.sleepTime.getAndSet(updateInterval);
        }

        @Override
        public void run() {
            while (enabled) {
                Log.v(LOG_TAG, "repainting");
                try {
                    if (updating) {
                        draw();
                        Thread.sleep(sleepTime.get());
                    } else {
                        checkForPaused();
                    }
                } catch (InterruptedException e) {
                }
            }
        }

        private void draw() {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas == null)
                return;
            canvas.drawColor(Color.BLACK);
            canvas.drawRect(2, 2, canvas.getWidth() - 2, canvas.getHeight() - 2, paint);
            if (graphicsContainer != null)
                graphicsContainer.draw(canvas);
            else {
                canvas.drawText("Nothing to draw, sorry. Time is: " + System.currentTimeMillis(), 0, 20, paint);
                canvas.drawText("Canvas size: w->" + canvas.getWidth() + " h->" + canvas.getHeight(), 0, 50, paint);
                canvas.drawText("Update interval: " + timedInvalidator.getUpdateInterval(), 0, 80, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);
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

        public void interrupt() {
            if (!updating)
                startUpdating();
            enabled = false;
        }

        public void enable() {
            enabled = true;
        }

        private boolean isEnabled() {
            return enabled;
        }
    }
}
