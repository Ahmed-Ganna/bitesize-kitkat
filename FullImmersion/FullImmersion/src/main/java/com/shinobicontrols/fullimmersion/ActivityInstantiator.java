package com.shinobicontrols.fullimmersion;

import android.content.Context;
import android.content.Intent;

public class ActivityInstantiator {
    private Class<?> mActivityClass;
    private String mName;

    public ActivityInstantiator(String name, Class<?> activityClass) {
        mName = name;
        mActivityClass = activityClass;
    }

    public Intent createIntent(Context packageContext) {
        return new Intent(packageContext, mActivityClass);
    }

    @Override
    public String toString() {
        return mName;
    }
}
