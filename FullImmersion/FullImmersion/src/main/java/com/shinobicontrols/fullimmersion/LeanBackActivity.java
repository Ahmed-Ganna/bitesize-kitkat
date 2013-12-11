package com.shinobicontrols.fullimmersion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity to demonstrate the 'leanback' paradigm of fullscreen usage in Android
 */
public class LeanBackActivity extends Activity implements
        View.OnSystemUiVisibilityChangeListener, View.OnClickListener {

    private View mDecorView;
    private final Handler mLeanBackHandler = new Handler();

    private int mLastSystemUIVisibility;

    private final Runnable mEnterLeanback = new Runnable() {
        @Override
        public void run() {
            setLeanBack(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leanback);

        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(this);

        View mainView = findViewById(R.id.leanBackView);
        mainView.setClickable(true);
        mainView.setOnClickListener(this);

        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setLeanBack(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setLeanBack(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    |  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Want to hide again after 3s
        if(!enabled) {
            // First cancel any queued events - i.e. resetting the countdown clock
            mLeanBackHandler.removeCallbacks(mEnterLeanback);
            // And fire the event in 3s time
            mLeanBackHandler.postDelayed(mEnterLeanback, 3000);
        }

        // Set the visibility
        mDecorView.setSystemUiVisibility(newVisibility);
    }


    @Override
    public void onClick(View v) {
        // If the `mainView` receives a click event then reset the leanback-mode clock
        setLeanBack(false);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        int diff = mLastSystemUIVisibility ^ visibility;
        mLastSystemUIVisibility = visibility;
        if((diff&View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                && (visibility&View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            setLeanBack(false);
        }
    }

}
