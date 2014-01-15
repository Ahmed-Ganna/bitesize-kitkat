package com.shinobicontrols.soundmeter;

import android.media.audiofx.Visualizer;
import android.os.Handler;

/**
 * Created by sdavies on 15/01/2014.
 */
public class SamplingSoundMeter {

    private SoundLevelUpdateable updateable;
    private Handler handler;
    private Runnable sampler;
    private Visualizer visualizer;

    public SamplingSoundMeter(SoundLevelUpdateable updateableObject) {
        updateable = updateableObject;
        visualizer = new Visualizer(0);
        handler = new Handler();
    }

    public void Start(final int intervalMillis) {
        // Stop it if we're already running
        Stop();

        // Create a new runnable
        sampler = new Runnable() {
            @Override
            public void run() {
                updateStatus();
                handler.postDelayed(sampler, intervalMillis);
            }
        };

        // Enable the visualiser and start the sampler
        visualizer.setEnabled(true);
        sampler.run();
    }

    public void Stop() {
        handler.removeCallbacks(sampler);
        visualizer.setEnabled(false);
    }

    private void updateStatus() {
        // Capture the sample
        Visualizer.MeasurementPeakRms measurementPeakRms = new Visualizer.MeasurementPeakRms();
        visualizer.getMeasurementPeakRms(measurementPeakRms);
        // Notify our updateable
        updateable.setPeakValue(measurementPeakRms.mPeak);
        updateable.setRmsValue(measurementPeakRms.mRms);
    }
}
