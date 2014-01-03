package com.shinobicontrols.texteditor;

import android.net.Uri;

public class AsyncStringWriterParams {
    public Uri uri;
    public String newContent;

    public AsyncStringWriterParams(Uri uri, String newContent) {
        this.uri = uri;
        this.newContent = newContent;
    }
}
