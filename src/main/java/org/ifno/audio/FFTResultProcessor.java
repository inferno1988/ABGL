package org.ifno.audio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 16.04.13
 * Time: 18:19
 * Palamarchuk Maksym © 2013
 */
public class FFTResultProcessor implements Runnable {
    private final ArrayBlockingQueue<Future<float[]>> fftJobQueue;
    private final String LOG_TAG = this.getClass().getSimpleName();

    public FFTResultProcessor(ArrayBlockingQueue<Future<float[]>> fftJobQueue) {
        this.fftJobQueue = fftJobQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final Future<float[]> futureTask = fftJobQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (futureTask == null)
                    continue;
                final float[] result = futureTask.get();
                final double[] magnitude = new double[result.length / 2];
                for (int k = 0; k < result.length / 2 - 1; k++) {
                    float real = result[2 * k];
                    float im = result[2 * k + 1];
                    magnitude[k] = Math.sqrt(real * real + im * im);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}