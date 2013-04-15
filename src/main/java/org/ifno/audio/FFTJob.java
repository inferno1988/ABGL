package org.ifno.audio;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 15.04.13
 * Time: 21:55
 * Palamarchuk Maksym © 2013
 */
public class FFTJob implements Callable<float []> {
    private final FloatFFT_1D fft;
    private final byte[] data;

    public FFTJob(byte[] data) {
        this.data = data;
        this.fft = new FloatFFT_1D(data.length);
    }

    @Override
    public float[] call() throws Exception {
        final float[] result = floatMe(data);
        fft.realForward(result);
        return result;
    }

    public static float[] floatMe(byte[] bytes) {
        float[] out = new float[bytes.length]; // will drop last byte if odd number
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        for (int i = 0; i < out.length-1; i++) {
            out[i] = bb.getFloat();
        }
        return out;
    }
}
