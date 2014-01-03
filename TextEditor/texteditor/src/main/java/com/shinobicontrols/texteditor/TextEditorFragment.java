package com.shinobicontrols.texteditor;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class TextEditorFragment extends Fragment implements AsyncStringReaderCompletionHandler, AsyncStringWriterCompletionHandler {

    private TextEditorFragmentDelegate mDelegate;
    public TextEditorFragment(TextEditorFragmentDelegate delegate) {
        mDelegate = delegate;
    }

    private String mText;
    public void setText(String mOriginalText) {
        this.mText = mOriginalText;
        UpdateTextView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        // Wire up the click handling
        Button saveButton = (Button)rootView.findViewById(R.id.editTextSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelegate.SaveText(getEditTextContent());
            }
        });

        Button revertButton = (Button)rootView.findViewById(R.id.editTextRevertButton);
        revertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTextView();
            }
        });

        // Send the view back
        return rootView;
    }

    private void UpdateTextView() {
        // Update the text view
        setEditTextContent(mText);
    }

    private String getEditTextContent() {
        return getEditText().getText().toString();
    }

    private void setEditTextContent(String content) {
        getEditText().setText(content);
    }

    private EditText getEditText() {
        return (EditText)getView().findViewById(R.id.editText);
    }

    @Override
    public void StringSaved(Boolean success) {
        if(success) {
            mText = getEditTextContent();
        } else {
            UpdateTextView();
        }
    }
}
