package com.shinobicontrols.printshop;

import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PrintDemoFragment extends Fragment {

    public PrintDemoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_print, container, false);

        // Wire up some button handlers
        rootView.findViewById(R.id.print_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintHelper printHelper = new PrintHelper(getActivity());
                printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                // Get hold of the imageview
                ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
                // Get the image
                if ((imageView.getDrawable()) != null) {
                    // Send it to the print helper
                    printHelper.printBitmap("PrintShop", ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                }

            }
        });

        rootView.findViewById(R.id.print_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PrintDemoFragment", "Print page clicked");
            }
        });

        return rootView;
    }
}
