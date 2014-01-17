package com.shinobicontrols.printshop;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

/**
 * Created by sdavies on 17/01/2014.
 */
public class PrintShopPrintDocumentAdapter extends PrintDocumentAdapter {

    private ImageAndTextContainer imageAndTextContainer;
    private int pageCount;
    private Canvas outputCanvas;

    public PrintShopPrintDocumentAdapter(ImageAndTextContainer container) {
        imageAndTextContainer = container;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, final LayoutResultCallback callback, Bundle extras) {

        // Register a cancellation listener
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                callback.onLayoutCancelled();
            }
        });

        // Prepare the layout.
        int newPageCount;
        if(newAttributes.getMediaSize().getHeightMils() < 1000) {
            newPageCount = 2;
        } else {
            newPageCount = 1;
        }

        outputCanvas = new Canvas()

        boolean layoutChanged = newPageCount != pageCount;
        pageCount = newPageCount;

        // Create the doc info to return
        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(pageCount)
                .build();

        // Not actually going to do anything for now
        callback.onLayoutFinished(info, layoutChanged);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {

        // Register a cancellation listener
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                callback.onWriteCancelled();
            }
        });
    }

}
