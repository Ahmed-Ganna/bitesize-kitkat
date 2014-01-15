package com.shinobicontrols.soundmeter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SoundMeterFragment extends Fragment implements SoundLevelUpdateable {

    public SoundMeterFragment() {
    }


    private SamplingSoundMeter samplingSoundMeter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sound_meter, container, false);

        samplingSoundMeter = new SamplingSoundMeter(this);

        // Add some button handlers
        rootView.findViewById(R.id.soundmeter_start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samplingSoundMeter.Start(500);
            }
        });

        rootView.findViewById(R.id.soundmeter_stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samplingSoundMeter.Stop();
            }
        });

        return rootView;
    }

    @Override
    public void setPeakValue(int peakValue) {
        ((TextView)getView().findViewById(R.id.textViewPeak)).setText("Peak: " + peakValue);
    }

    @Override
    public void setRmsValue(int rmsValue) {
        ((TextView)getView().findViewById(R.id.textViewRMS)).setText("RMS: " + rmsValue);
    }
}
