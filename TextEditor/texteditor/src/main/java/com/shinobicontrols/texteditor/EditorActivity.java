package com.shinobicontrols.texteditor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class EditorActivity extends Activity implements TextEditorFragmentDelegate {

    public static final int READ_REQUEST_CODE = 135;
    private static final String TAG = "TextEditor";
    private TextEditorFragment textEditorFragment;
    private Uri currentOpenFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (savedInstanceState == null) {
            textEditorFragment = new TextEditorFragment(this);
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
            currentOpenFileUri = null;
            if (data != null) {
                currentOpenFileUri = data.getData();
                Log.i(TAG, "URI: " + currentOpenFileUri.toString());
                // Get hold of the content of the file
                AsyncStringReader stringReader = new AsyncStringReader(getContentResolver(), textEditorFragment);
                stringReader.execute(currentOpenFileUri);
            }
        }
    }

    @Override
    public void SaveText(String text) {
        Log.i(TAG, "Save the updated text");
        AsyncStringWriter stringWriter = new AsyncStringWriter(getContentResolver(), textEditorFragment);
        AsyncStringWriterParams params = new AsyncStringWriterParams(currentOpenFileUri, text);
        stringWriter.execute(params);
    }
}
