package com.shinobicontrols.fullimmersion;

import android.view.View;

public class ImmersiveActivity extends AbstractFullScreenLayoutActivity {

    @Override
    protected int getLayoutID() {
        return R.layout.immersive;
    }

    @Override
    protected int getMainViewID() {
        return R.id.immersiveView;
    }

    @Override
    protected void enableFullScreen(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        getDecorView().setSystemUiVisibility(newVisibility);
    }


    public void immersiveButtonClickHandler(View view) {
        enableFullScreen(true);
    }
}
