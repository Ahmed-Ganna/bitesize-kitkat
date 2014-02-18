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

package com.shinobicontrols.texteditor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public TextEditorFragment() {
    }

    private OnTextSavedListener mListener;

    private String mText;
    public void setText(String mOriginalText) {
        this.mText = mOriginalText;
        UpdateTextView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTextSavedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTextSavedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        // Send the view back
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.editor_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_text_revert) {
            UpdateTextView();
            return true;
        } else if(item.getItemId() == R.id.edit_text_save) {
            if(mText != null) {
                mListener.SaveText(getEditTextContent());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
