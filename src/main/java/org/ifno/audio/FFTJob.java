package org.ifno.audio;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 15.04.13
 * Time: 21:55
 * Palamarchuk Maksym © 2013
 */
public class FFTJob implements Callable<float[]> {
    private final FloatFFT_1D fft;
    private final short[] data;
    private static final float BLACKMAN_HARRIS_A0 = 0.35875f;
    private static final float BLACKMAN_HARRIS_A1 = 0.48829f;
    private static final float BLACKMAN_HARRIS_A2 = 0.14128f;
    private static final float BLACKMAN_HARRIS_A3 = 0.01168f;
    private static final String LOG_TAG = FFTJob.class.getSimpleName();


    public FFTJob(short[] data) {
        this.data = data;
        this.fft = new FloatFFT_1D(data.length);
    }

    @Override
    public float[] call() throws Exception {
        float[] transformResult = shortToFloat(data);
        blackmanHarrisWindow(transformResult);
        fft.realForward(transformResult);
        return transformResult;
    }

    private float[] shortToFloat(short[] audioBuffer) {
        final float[] result = new float[audioBuffer.length];
        for (int i = 0; i < audioBuffer.length; i++) {
            short src = audioBuffer[i];
            float dst = 0.0f;
            if (src < -33) {
                dst = (Math.abs((float) src) / Math.abs(Short.MIN_VALUE))*-1.0f;
            } else if (src > 33) {
                dst = (float) src / Short.MAX_VALUE;
            }
            result[i] = dst;
        }
        return result;
    }

    private void blackmanHarrisWindow(float[] dataToBeWindowed) {
        int N = dataToBeWindowed.length;
        for (int i = 0; i < dataToBeWindowed.length; i++) {
            dataToBeWindowed[i] *= (float) (BLACKMAN_HARRIS_A0 -
                    BLACKMAN_HARRIS_A1 * Math.cos(2.0f * Math.PI * dataToBeWindowed[i] / N - 1.0f) +
                    BLACKMAN_HARRIS_A2 * Math.cos(4.0f * Math.PI * dataToBeWindowed[i] / N - 1.0f) -
                    BLACKMAN_HARRIS_A3 * Math.cos(6.0f * Math.PI * dataToBeWindowed[i] / N - 1.0f));
        }
    }
}
