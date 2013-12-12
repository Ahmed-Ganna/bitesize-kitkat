package com.shinobicontrols.fullimmersion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ImmersiveActivity extends Activity {

    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.immersive);

        mDecorView = getWindow().getDecorView();

        View mainView = findViewById(R.id.immersiveView);
        mainView.setClickable(true);

        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        enableImmersive(true);
    }

    private void enableImmersive(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        mDecorView.setSystemUiVisibility(newVisibility);
    }

    public void immersiveButtonClickHandler(View view) {
        enableImmersive(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
