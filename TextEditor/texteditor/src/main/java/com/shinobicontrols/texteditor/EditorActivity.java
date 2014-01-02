package com.shinobicontrols.texteditor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditorActivity extends Activity {

    public static final int READ_REQUEST_CODE = 135;
    private static final String TAG = "TextEditor";
    private TextEditorFragment textEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (savedInstanceState == null) {
            textEditorFragment = new TextEditorFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, textEditorFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_open) {
            Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Only want those items which can be opened
            openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            // Only want to see files of the plain text mime type
            openFileIntent.setType("text/plain");

            startActivityForResult(openFileIntent, READ_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Let's see the URI
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "URI: " + uri.toString());
                // Get hold of the content of the file
                AsyncStringReader stringReader = new AsyncStringReader(getContentResolver(), textEditorFragment);
                stringReader.execute(uri);
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TextEditorFragment extends Fragment implements AsyncStringReaderCompletionHandler {

        public TextEditorFragment() {
        }

        public String getText() {
            return mText;
        }

        public void setText(String mOriginalText) {
            this.mText = mOriginalText;

            // Update the text view
            EditText textView = (EditText)getView().findViewById(R.id.editText);
            textView.setText(this.mText);
        }

        private String mText;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

            // Wire up the click handling
            Button saveButton = (Button)rootView.findViewById(R.id.editTextSaveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Save button pressed");
                }
            });

            Button revertButton = (Button)rootView.findViewById(R.id.editTextRevertButton);
            revertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Revert button pressed");
                }
            });

            // Send the view back
            return rootView;
        }
    }

}
