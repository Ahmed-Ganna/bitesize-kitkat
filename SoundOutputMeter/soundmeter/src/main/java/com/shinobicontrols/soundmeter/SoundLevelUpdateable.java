package com.shinobicontrols.soundmeter;

/**
 * Created by sdavies on 15/01/2014.
 */
public interface SoundLevelUpdateable {
    void setPeakValue(int peakValue);
    void setRmsValue(int rmsValue);
}
