/*
Copyright 2014 Scott Logic Ltd

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

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
        visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
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
