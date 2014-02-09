# Android KitKat: finger-by-finger 

## Using the new Android Printing Framework

One of the new frameworks present in KitKat is a full-stack printing framework,
which allows app developers to hand off the printing of a variety of content.
The framework supports all kinds of printers - from wifi and bluetooth, though
to internet-enabled printers via Google CloudPrint. Part of the new framework
allows providers to integrate their printing services with the framework, allowing
users to choose a suitable printer at runtime. The framework provides developers
with the ability to create content to send to the print service, with helper
methods for imagery, and a full API for creating custom content.

In this article we'll take a look at how to create content to provide to the
print service - at how to add printing capabilities to your KitKat-enabled app.

There is an accompanying demo app with complete source code available on GitHub - 
at GITHUB_LINK_HERE. It was written and tested in AndroidStudio 0.4.4, so you
should be able to get it running as a gradle project in either AndroidStudio or
Eclipse.


### Printing Images

The print framework provides an easy-to-use helper API for printing images - i.e.
photos for a photo printer or the suchlike. The Android support library includes
a `PrintHelper` class which enables the printing of images. The following code
snippet (attached to a button's `onClickListener`) demonstrates how to use it:

    PrintHelper printHelper = new PrintHelper(getActivity());
    printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
    // Get the image
    Bitmap image = getImage();
    if (image != null) {
        // Send it to the print helper
        printHelper.printBitmap("PrintShop", image);
    }

Creating a `PrintHelper` requires a `Context` object, and since we're calling it
from inside a fragment, we provide the `Context` via `getActivity()`. A
`PrintHelper` has 3 important properties - `ScaleMode`, `ColorMode` and
`Orientation`. The former describes how the image should be resized to fit the
print page output, `ColorMode` determines whether the picture should print in
full color, or in monochrome, and `Orientation` specifies whether the image should
be printed in portrait or landscape.

The `getImage()` method is a utility method for obtaining the image from the
fragment, which we'll take a look at below. Provided we have a `Bitmap` then
the `printBitmap` method is called on the `PrintHelper` object, passing in a
`String` for the name of the job, and the `Bitmap` we wish to print.

The `getBitmap()` method is as follows:

    public Bitmap getImage() {
        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
        Bitmap image = null;
        // Get the image
        if ((imageView.getDrawable()) != null) {
            // Send it to the print helper
            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }
        return image;
    }

This finds the `ImageView` with the specified name, and then extracts a bitmap
from it. This is returned so that it can be set to the `PrintHelper`.

Once `printBitmap` has been called, then the system will present a print dialog
to the user, allowing the details of the print job to be specified.

PUT SOME PICTURES HERE



### Custom Print Adapter

Using the `PrintHelper` is all well and good for printing simple images, but
for more complicated content we have to create a custom print adapter, which
specifies exactly how the print content should be laid out.

`PrintDocumentAdapter` is an abstract class which, via subclassing, can be used
to layout a page for printing. It has 4 methods which can be overridden, describing
the workflow associated with the printing process:

- `onStart()`
- `onLayout()`
- `onWrite()`
- `onFinish()`

In the simple example provided in __PrintShop__, we actually only need to override
2 of these methods. We'll look at `onLayout()` first:

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

        // Create the PDF document we'll use later
        pdfDocument = new PrintedPdfDocument(context, newAttributes);


        // Has the layout actually changed?
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

The first thing to do as part of `onLayout()` is to register a cancellation listener,
so that if the user chooses to cancel the print, then the layout process will
cancel. This is made possible since the `onLayout()` method is passed a
`CancellationSignal` object, which we can register a `OnCancelListener` to. Here
we'll just callback that we've canceled the layout process. In practice you can
use this as an opportunity to tidy up any expensive objects that you have around,
or indeed kill any worker threads that you've kicked off.

In order to demonstrate how the layout can change, we're calculating the number of
pages based on the size of the page the printer is going to use. Here we've got 
a really simple check based on the magic value of 1000mm. 

`pdfDocument` is a member variable which will be used with the `onWrite()` method,
and we prepare it here, whilst we've been passsed the context and the attributes.



### Conclusion

