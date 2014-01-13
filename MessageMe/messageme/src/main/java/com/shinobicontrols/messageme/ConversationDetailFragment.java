package com.shinobicontrols.messageme;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;


import com.shinobicontrols.messageme.models.Conversation;
import com.shinobicontrols.messageme.models.DataProvider;
import com.shinobicontrols.messageme.models.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a single Conversation detail screen.
 * This fragment is either contained in a {@link ConversationListActivity}
 * in two-pane mode (on tablets) or a {@link ConversationDetailActivity}
 * on handsets.
 */
public class ConversationDetailFragment extends ListFragment implements Observer {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Conversation mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationDetailFragment() {
    }

    private ArrayAdapter<Message> messageArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DataProvider.getInstance().getConversationMap().get(getArguments().getString(ARG_ITEM_ID));
            messageArrayAdapter = new ArrayAdapter<Message>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    mItem.getMessages()
            );
            setListAdapter(messageArrayAdapter);
        }

        // Register as a listener
        DataProvider.getInstance().addObserver(this);
    }

    @Override
    public void onDestroy() {
        // Remove ourself as a listener
        DataProvider.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        if(messageArrayAdapter != null) {
            messageArrayAdapter.notifyDataSetChanged();
        }
    }
}
