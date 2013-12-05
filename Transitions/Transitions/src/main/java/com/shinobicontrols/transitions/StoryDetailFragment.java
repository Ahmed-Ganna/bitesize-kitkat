package com.shinobicontrols.transitions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinobicontrols.transitions.data.StoryContent;

/**
 * A fragment representing a single Story detail screen.
 * This fragment is either contained in a {@link StoryListActivity}
 * in two-pane mode (on tablets) or a {@link StoryDetailActivity}
 * on handsets.
 */
public class StoryDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_STORY_ID = "story_id";

    /**
     * The story item this fragment is presenting.
     */
    private StoryContent.StoryItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STORY_ID)) {
            String story_id = getArguments().getString(ARG_STORY_ID);
            mItem = StoryContent.STORY_MAP.get(story_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.story_detail)).setText(getResources().getText(mItem.contentResourceId));
        }

        return rootView;
    }
}
