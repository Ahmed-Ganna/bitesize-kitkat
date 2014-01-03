package com.shinobicontrols.texteditor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class TextEditorFragment extends Fragment implements AsyncStringReaderCompletionHandler, AsyncStringWriterCompletionHandler {

    // Container activity must implement this
    public static interface OnTextSavedListener {
        public void SaveText(String text);
    }

    private OnTextSavedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTextSavedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTextSavedListener");
        }
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
                mListener.SaveText(getEditTextContent());
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
