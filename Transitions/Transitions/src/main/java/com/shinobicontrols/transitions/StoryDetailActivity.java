package com.shinobicontrols.transitions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinobicontrols.transitions.data.StoryContent;

/**
 * An activity representing a single Story detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StoryListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link StoryDetailFragment}.
 */
public class StoryDetailActivity extends FragmentActivity {

    private ViewGroup container;
    private Scene current;
    private Scene other;

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
        container = (ViewGroup)findViewById(R.id.container);

        final ViewGroup scene0group = (ViewGroup)getLayoutInflater().inflate(R.layout.content_scene_00, container, false);
        addContentToViewGroup(scene0group);
        current = new Scene(container, scene0group);

        final ViewGroup scene1group = (ViewGroup)getLayoutInflater().inflate(R.layout.content_scene_01, container, false);
        addContentToViewGroup(scene1group);
        other = new Scene(container, scene1group);

        // Show the Up button in the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Specify we want some tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a listener to cope with tab changes
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Scene nextScene;
                switch (tab.getPosition()) {
                    case 0:
                        nextScene = current;
                        break;
                    case 1:
                        nextScene = other;
                        break;
                    default:
                        nextScene = current;
                        break;
                }

                TransitionManager.go(nextScene);
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
        for (int i=0; i<2; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                    .setText("Scene " + i)
                    .setTabListener(tabListener));
        }

        current.enter();
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

    private void addContentToViewGroup(ViewGroup vg)
    {
        if (mItem != null) {
            ((TextView) vg.findViewById(R.id.story_content)).setText(getResources().getText(mItem.contentResourceId));
            TextView titleTextView = (TextView)vg.findViewById(R.id.story_title);
            if(titleTextView != null) {
                titleTextView.setText(mItem.title);
            }
            ((ImageView) vg.findViewById(R.id.story_image)).setImageResource(mItem.imageResourceId);
        }
    }
}
