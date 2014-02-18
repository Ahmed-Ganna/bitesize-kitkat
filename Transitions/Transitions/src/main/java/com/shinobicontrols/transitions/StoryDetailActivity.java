package com.shinobicontrols.transitions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinobicontrols.transitions.data.StoryContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a single Story detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StoryListActivity}.
 */
public class StoryDetailActivity extends FragmentActivity {

    private List<Scene> sceneList;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_STORY_ID = "story_id";

    /**
     * The story item this fragment is presenting.
     */
    private StoryContent.StoryItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        if (savedInstanceState == null) {
            // Load the data from the intent on first pass
            Intent intent = getIntent();
            String story_id = intent.getStringExtra(ARG_STORY_ID);
            mItem = StoryContent.STORY_MAP.get(story_id);
        }

        // Get hold of some relevant content
        ViewGroup container = (ViewGroup)findViewById(R.id.container);

        // What are the layouts we should be able to transition between
        List<Integer> sceneLayouts = Arrays.asList(R.layout.content_scene_00,
                                                   R.layout.content_scene_01,
                                                   R.layout.content_scene_02);
        // Create the scenes
        sceneList = new ArrayList<Scene>();
        LayoutInflater layoutInflater = getLayoutInflater();
        for(int layout : sceneLayouts) {
            final ViewGroup sceneViewGroup = (ViewGroup)layoutInflater.inflate(layout, container, false);
            addContentToViewGroup(sceneViewGroup);
            sceneList.add(new Scene(container, sceneViewGroup));
        }


        // Show the Up button in the action bar.
        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Specify we want some tabs
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Create a listener to cope with tab changes
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // If there's a scene for this tab index, then transition to it
                    if(tab.getPosition() <= sceneList.size()) {
                        TransitionManager.go(sceneList.get(tab.getPosition()));
                    }
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // Can ignore this event
                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // Can ignore this event
                }
            };

            // Add some tabs
            for (int i=0; i<sceneList.size(); i++) {
                actionBar.addTab(
                        actionBar.newTab()
                        .setText("Scene " + i)
                        .setTabListener(tabListener));
            }
        }

        // Load the first scene
        sceneList.get(0).enter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, StoryListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Given a view group, then populate with the content from the node which has been loaded
     * into mItem
     * @param viewGroup The ViewGroup to populate with the content
     */
    private void addContentToViewGroup(ViewGroup viewGroup)
    {
        if (mItem != null) {
            TextView contentTextView = (TextView) viewGroup.findViewById(R.id.story_content);
            if(contentTextView != null) {
                contentTextView.setText(getResources().getText(mItem.contentResourceId));
            }
            TextView titleTextView = (TextView) viewGroup.findViewById(R.id.story_title);
            if(titleTextView != null) {
                titleTextView.setText(mItem.title);
            }
            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.story_image);
            if(imageView != null) {
                imageView.setImageResource(mItem.imageResourceId);
            }
        }
    }
}
