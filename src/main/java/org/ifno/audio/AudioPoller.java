package org.ifno.audio;

import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;
import edu.emory.mathcs.utils.ConcurrencyUtils;

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
        this.soundBuffer = new short[this.bufferSize];
        this.audioSource = audioSource;
        this.audioRecorder = new AudioRecord(this.audioSource, this.sampleRate, this.channelConfig, this.audioFormat, this.bufferSize);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
        audioRecorder.startRecording();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int offset = 0;
                while (offset < bufferSize) {
                    Thread.sleep(16);
                    int bytesRead = audioRecorder.read(soundBuffer, offset, bufferSize - offset);
                    if (bytesRead == AudioRecord.ERROR_BAD_VALUE) {
                        audioRecorder.stop();
                        audioRecorder.release();
                        audioRecorder = null;
                        throw new IllegalStateException("Can't read from data from audio record, something goes wrong.");
                    }
                    offset += bytesRead;
                }
                FFTJob fftJob = new FFTJob(soundBuffer.clone());
                fftJobQueue.put(executorService.submit(fftJob));
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "AudioPoller result processor was interrupted");
                audioRecorder.stop();
                audioRecorder.release();
                audioRecorder = null;
                return;
            }
            Log.v(LOG_TAG, "Added job: -> " + fftJobQueue.size());
        }
        audioRecorder.stop();
        audioRecorder.release();
        audioRecorder = null;
    }
}
