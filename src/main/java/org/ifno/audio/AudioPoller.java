package org.ifno.audio;

import android.media.AudioRecord;
import android.util.Log;

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
    private final byte[] soundBuffer;
    private final ArrayBlockingQueue<Future<float[]>> fftJobQueue;
    private final String LOG_TAG = this.getClass().getSimpleName();


    public AudioPoller(ExecutorService executorService, ArrayBlockingQueue<Future<float[]>> fftJobQueue, int audioSource, int sampleRate, int channelConfig, int audioFormat, int bufferSize) {
        this.executorService = executorService;
        this.sampleRate = sampleRate;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
        this.bufferSize = bufferSize;
        this.fftJobQueue = fftJobQueue;
        this.soundBuffer = new byte[bufferSize];
        this.audioRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize);
        this.audioSource = audioSource;
    }

    @Override
    public void run() {
        audioRecorder.startRecording();
        while (!Thread.currentThread().isInterrupted()) {
            audioRecorder.read(soundBuffer, 0, bufferSize);
            FFTJob fftJob = new FFTJob(soundBuffer);
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
