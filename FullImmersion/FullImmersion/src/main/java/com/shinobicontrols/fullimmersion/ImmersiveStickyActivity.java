package com.shinobicontrols.fullimmersion;

import android.view.View;

public class ImmersiveStickyActivity extends AbstractFullScreenLayoutActivity {
    @Override
    protected int getMainViewID() {
        return R.id.immersiveStickyView;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.immersive_sticky;
    }

    @Override
    protected void enableFullScreen(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getDecorView().setSystemUiVisibility(newVisibility);
    }
}
