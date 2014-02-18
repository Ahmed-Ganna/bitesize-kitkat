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

package com.shinobicontrols.fullimmersion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImmersiveActivity extends AbstractFullScreenLayoutActivity implements
    View.OnSystemUiVisibilityChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDecorView().setOnSystemUiVisibilityChangeListener(this);
    }

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

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        Button immersiveButton = (Button)findViewById(R.id.enableImmersiveButton);
        if((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0) {
            // Hide button
            immersiveButton.setVisibility(View.INVISIBLE);
        } else {
            immersiveButton.setVisibility(View.VISIBLE);
        }
    }

    public void immersiveButtonClickHandler(View view) {
        enableFullScreen(true);
    }
}
