package com.shinobicontrols.transitions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.shinobicontrols.transitions.data.StoryContent;


/**
 * An activity representing a list of Stories. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StoryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * This activity also implements the required
 * {@link StoryListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class StoryListActivity extends FragmentActivity
        implements StoryListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

    }

    /**
     * Callback method from {@link StoryListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {

        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        Intent detailIntent = new Intent(this, StoryDetailActivity.class);
        String name = StoryDetailActivity.ARG_STORY_ID;

        detailIntent.putExtra(name, id);
        startActivity(detailIntent);

    }
}
