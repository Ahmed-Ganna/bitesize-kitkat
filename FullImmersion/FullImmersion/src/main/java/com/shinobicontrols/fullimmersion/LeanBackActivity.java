package com.shinobicontrols.fullimmersion;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Activity to demonstrate the 'leanback' paradigm of fullscreen usage in Android
 */
public class LeanBackActivity extends AbstractFullScreenLayoutActivity implements
        View.OnSystemUiVisibilityChangeListener, View.OnClickListener {

    private final Handler mLeanBackHandler = new Handler();
    private int mLastSystemUIVisibility;
    private final Runnable mEnterLeanback = new Runnable() {
        @Override
        public void run() {
            enableFullScreen(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDecorView().setOnSystemUiVisibilityChangeListener(this);
        getMainView().setOnClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.leanback;
    }

    @Override
    protected int getMainViewID() {
        return R.id.leanBackView;
    }

    @Override
    protected void enableFullScreen(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    |  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Want to hide again after 3s
        if(!enabled) {
            resetHideTimer();
        }

        // Set the visibility
        getDecorView().setSystemUiVisibility(newVisibility);
    }

    private void resetHideTimer() {
        // First cancel any queued events - i.e. resetting the countdown clock
        mLeanBackHandler.removeCallbacks(mEnterLeanback);
        // And fire the event in 3s time
        mLeanBackHandler.postDelayed(mEnterLeanback, 3000);
    }

    @Override
    public void onClick(View v) {
        // If the `mainView` receives a click event then reset the leanback-mode clock
        resetHideTimer();
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if((mLastSystemUIVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                     && (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            resetHideTimer();
        }
        mLastSystemUIVisibility = visibility;
    }

}
