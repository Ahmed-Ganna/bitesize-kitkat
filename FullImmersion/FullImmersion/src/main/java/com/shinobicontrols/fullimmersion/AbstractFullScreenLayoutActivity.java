package com.shinobicontrols.fullimmersion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public abstract class AbstractFullScreenLayoutActivity extends Activity {
    private View mDecorView;
    private View mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutID());

        mDecorView = getWindow().getDecorView();

        mMainView = findViewById(this.getMainViewID());
        mMainView.setClickable(true);

        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        enableFullScreen(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected final View getDecorView() {
        return mDecorView;
    }

    protected final View getMainView() {
        return mMainView;
    }

    abstract protected int getMainViewID();
    abstract protected int getLayoutID();
    abstract protected void enableFullScreen(boolean enabled);
}
