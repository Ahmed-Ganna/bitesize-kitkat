package com.shinobicontrols.texteditor;

import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by sdavies on 03/01/2014.
 */
public class AsyncStringWriter extends AsyncTask<AsyncStringWriterParams, Void, Boolean> {

    private ContentResolver mContentResolver;
    private AsyncStringWriterCompletionHandler mCompletionHandler;
    public AsyncStringWriter(ContentResolver contentResolver,
                             AsyncStringWriterCompletionHandler completionHandler) {
        mContentResolver = contentResolver;
        mCompletionHandler = completionHandler;
    }

    @Override
    protected Boolean doInBackground(AsyncStringWriterParams... params) {
        Boolean success = false;
        try {
            ParcelFileDescriptor pfd = mContentResolver.openFileDescriptor(params[0].uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(params[0].newContent.getBytes(Charset.forName("UTF-8")));
            fileOutputStream.close();
            pfd.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mCompletionHandler.StringSaved(aBoolean);
    }
}
