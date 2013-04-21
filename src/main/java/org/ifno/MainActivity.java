package org.ifno;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Spinner;
import edu.emory.mathcs.utils.ConcurrencyUtils;
import org.ifno.audio.AudioPoller;
import org.ifno.audio.FFTResultProcessor;

import java.util.concurrent.*;

public class MainActivity extends Activity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Display display;
    private Rect displayRect = new Rect();
    private Spinner spinner;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.main_window);
        display = getWindowManager().getDefaultDisplay();
        spinner = (Spinner) findViewById(R.id.visualtisationList);
    }

    private Rect getScreenDimensions() {
        display.getRectSize(displayRect);
        return displayRect;
    }

    public void startSpectrumAnalyzer(View view) {
        String selectedItem = String.valueOf(spinner.getSelectedItem());
        final String[] valuesList = getResources().getStringArray(R.array.visualisation_type);
        if (selectedItem.equals(valuesList[0])) {
            Intent intent = new Intent(this, SpectrogramActivity.class);
            startActivity(intent);
        }
    }
}

