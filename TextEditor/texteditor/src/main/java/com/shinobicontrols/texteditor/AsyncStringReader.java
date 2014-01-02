package com.shinobicontrols.texteditor;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AsyncStringReader extends AsyncTask<Uri, Void, String> {

    private AsyncStringReaderCompletionHandler mCompletionHandler;
    private ContentResolver mContentResolver;

    public AsyncStringReader(ContentResolver contentResolver,
                             AsyncStringReaderCompletionHandler completionHandler) {
        mContentResolver   = contentResolver;
        mCompletionHandler = completionHandler;
    }

    @Override
    protected String doInBackground(Uri... params) {
        String resultString = "";
        try{
            InputStream inputStream = mContentResolver.openInputStream(params[0]);
            if(inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                try {
                    while((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                resultString = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    @Override
    protected void onPostExecute(String s) {
        mCompletionHandler.setText(s);
    }
}
