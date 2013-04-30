package org.ifno.audio;

import android.media.AudioRecord;
import android.util.Log;
import edu.emory.mathcs.utils.ConcurrencyUtils;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 15.04.13
 * Time: 21:38
 * Palamarchuk Maksym © 2013
 */
public class AudioPoller implements Runnable {

    private final ExecutorService executorService;
    private final int audioSource;
    private AudioRecord audioRecorder;
    private final int sampleRate;
    private final int channelConfig;
    private final int audioFormat;
    private final int bufferSize;
    private final short[] soundBuffer;
    private final ArrayBlockingQueue<Future<float[]>> fftJobQueue;
    private final String LOG_TAG = this.getClass().getSimpleName();


    public AudioPoller(ExecutorService executorService, ArrayBlockingQueue<Future<float[]>> fftJobQueue, int audioSource, int sampleRate, int channelConfig, int audioFormat, int bufferSize) {
        this.executorService = executorService;
        this.sampleRate = sampleRate;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
        this.fftJobQueue = fftJobQueue;
        if (!ConcurrencyUtils.isPowerOf2(bufferSize)) {
            this.bufferSize = ConcurrencyUtils.nextPow2(bufferSize);
        } else {
            this.bufferSize = bufferSize;
        }
        this.soundBuffer = new short[bufferSize];
        this.audioSource = audioSource;
        this.audioRecorder = new AudioRecord(this.audioSource, this.sampleRate, this.channelConfig, this.audioFormat, this.bufferSize);
    }

    @Override
    public void run() {
        audioRecorder.startRecording();
        while (!Thread.currentThread().isInterrupted()) {
            int offset = 0;
            while (offset < bufferSize) {
                int bytesRead = audioRecorder.read(soundBuffer, offset, bufferSize - offset);
                offset += bytesRead;
            }
            FFTJob fftJob = new FFTJob(soundBuffer.clone());
            try {
                fftJobQueue.put(executorService.submit(fftJob));
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Job queue is interrupted");
            }
            Log.v(LOG_TAG, "Added job: -> " + fftJobQueue.size());
        }
        audioRecorder.stop();
        audioRecorder.release();
        audioRecorder = null;
    }
}
