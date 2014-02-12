# Android KitKat: finger-by-finger 

## Creating a text editor for the cloud

The new storage access framework in KitKat provides a unified interface to a
multitude of file storage options - from on disk to cloud storage services. This
means that it's easy for any apps to view, edit and create apps across a wide
range of of storage providers.

In this article we'll take a look at how to access the storage access framework
as a consumer - i.e. providing access to storage endpoints. The framework also
supports creating new storage endpoints (document providers) - as a cloud storage
provider (e.g. dropbox) might want to. We're going to create a really simple
text-editor app, which will be able to open a text document stored in the cloud,
edit it and save the changes.

The code is available as part of the KitKat: finger-by-finger repo on Github at
GITHUB_LINK_HERE. The project is a gradle project, and should be easy to import
into Android Studio. It has been tested with Android Studio 0.4.4.


### Opening a file

Pre-KitKat, accessing files provided by other apps required selecting an app which
had provided a user-interface for interacting with the files it provides access
to. In KitKat a new intent has been created `ACTION_OPEN_DOCUMENT`, which will
provide persistent access to a document provided by a document provider. This means
that it should be used for opening a file for editing or the suchlike. If you wish
to just import a copy of an image for display then `ACTION_GET_CONTENT` continues
to be the best choice.

The following code demonstrates how simple it is to present the user with an
activity for navigation to and selection of a document:

    Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

    // Only want those items which can be opened
    openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

    // Only want to see files of the plain text mime type
    openFileIntent.setType("text/plain");

    startActivityForResult(openFileIntent, READ_REQUEST_CODE);

First we create the intent, and then add some restrictions to the types of
documents we wish to be shown. We should be able to open the files
(`CATEGORY_OPENABLE`) and we're only interested in plain text files. Then we
start the activity with `startActivityForResult()`. `READ_REQUEST_CODE` is a
static `int` which we have previously defined:

    public static final int READ_REQUEST_CODE = 135;

When the document selection activity completes it will call `onActivityResult()`
on our activity, and therefore this request code is used to identify the specific
request we've kicked off.

This will present an activity as follows:

![Document selection activity](img/file_selection.png)

As you can see, we already have document storage providers for the local disk
and Google Drive. If you have your own storage service then the Storage Access
Framework makes it pretty easy to write a Document Provider to enable access
though this dialog. This is outside the scope of this article, but there is 
good guide available on the android developer site at
[developer.android.com](https://developer.android.com/guide/topics/providers/document-provider.html).

Once the user has completed the file selection in the document provider activity
then our activity will get a callback to `OnActivityResult()`. The following
demonstrates how to open the specified file:

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

Once we've checked that the completed activity is the one we expect (via comparison
of `requestCode`) and that the activity completed successfully (as opposed to being
canceled) then we go ahead an attempt to read the file content. One of the arguments
provided is an `Intent`, which has as its `data` property a URI through which
the document can be accessed. Here we extract the URI, and then instantiate an
`AsyncStringReader` which will read the content from the URI asynchronously. This
reader is a custom class, and we'll take a brief look at it in the next section.


#### Asynchronous String Reader

Although it's not part of the main thrust of this article, the `AsyncStringReader`
class is sufficiently interesting that it warrants further description. Android
provides an abstract class (`AsyncTask`) for performing tasks on a background
thread. It provides facility for starting a task, updating the user on progress,
and firing a completion method back on the main thread, without ever having to
deal with any threading issues.

Here we'll use an `AsyncTask` to read a string from a specified `Uri`:

    public class AsyncStringReader extends AsyncTask<Uri, Void, String>

When subclassing we fix the 3 generic types - defining the task start parameters,
the progress update return type, and the result type. Here, we provide a `Uri` to
read from, don't care about progress updates and want a `String` of the document's
content in return.

To construct an `AsyncStringReader` we require a `ContentResolver` to allow the
file to be opened, and an `AsyncStringReaderCompletionHandler` which will be
notified when the file has been read:

    private AsyncStringReaderCompletionHandler mCompletionHandler;
    private ContentResolver mContentResolver;

    public AsyncStringReader(ContentResolver contentResolver,
                             AsyncStringReaderCompletionHandler completionHandler) {
        mContentResolver   = contentResolver;
        mCompletionHandler = completionHandler;
    }

Where `AsyncStringReaderCompletionHandler` is an interface:

    public interface AsyncStringReaderCompletionHandler {
            public void setText(String s);
    }

There are 4 possible methods we could override, but we actually need only two - 
the first being `doInBackground()`, which describes the task which is performed
on the background thread:

    @Override
    protected String doInBackground(Uri... params) {
        String resultString = "";
        try{
            InputStream inputStream = mContentResolver.openInputStream(params[0]);
            if(inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                inputStream.close();
                resultString = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

Here we use the `ContentResolver` to get an `InputStream` from the provided `Uri`,
before using a `BufferedReader` to construct a `String` which is the content of
the file.

The other method is `onPostExecute()`, which is run back on the main thread, and
we use to send the file content to the completion handler:

    @Override
    protected void onPostExecute(String s) {
        mCompletionHandler.setText(s);
    }

#### Using the string reader

If you cast your mind back to when we used this string reader in the `onActivityResult()`
method you'll remember that we passed in a `Fragment` subclass as the completion
handler:

    public class TextEditorFragment extends Fragment implements AsyncStringReaderCompletionHandler

In the `TextEditorFragment` when we receive new text we save it in a member variable:

    private String mText;
    public void setText(String mOriginalText) {
        this.mText = mOriginalText;
        UpdateTextView();
    }

And ensure that the text view is updated appropriately:

    private void UpdateTextView() {
        // Update the text view
        setEditTextContent(mText);
    }

    private void setEditTextContent(String content) {
        getEditText().setText(content);
    }

    private EditText getEditText() {
        return (EditText)getView().findViewById(R.id.editText);
    }

There are some additional complexities associated with the interaction between
the fragment and the activity, but they're out of scope for this article. If you
run up the app at this point, then you'll be able to open a text document from
Google Drive, and get it to load into view for editing:

![Loading text file from Google Drive](img/text_edit_before.png)


### Saving a file

Once you've edited a file you'll want to save it. This is actually completely
seamless, since the all the cleverness of the storage access framework is hidden
away behind the `Uri` that you've been provided. Therefore, to save our updated
string:

    ParcelFileDescriptor pfd = mContentResolver.openFileDescriptor(params[0].uri, "w");
    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
    fileOutputStream.write(params[0].newContent.getBytes(Charset.forName("UTF-8")));
    fileOutputStream.close();
    pfd.close();

We first get a file descriptor which enables write access from the a content
resolver, before creating a `FileOutputStream` and writing the contents of the
String. This really is simplicity itself - excellent.


#### Asynchronous String Writer

In the same way that we created an async string reader, we're going to use a
writer as well.

    public class AsyncStringWriter extends AsyncTask<AsyncStringWriterParams, Void, Boolean>

The `Param` type is a custom class which encapsulates the `Uri` of the document
to save, and `String` of the new content:

    public class AsyncStringWriterParams {
        public Uri uri;
        public String newContent;

        public AsyncStringWriterParams(Uri uri, String newContent) {
            this.uri = uri;
            this.newContent = newContent;
        }
    }

The constructor will again require a `ContentResolver`, as well as an
`AsyncStringWriterCompletionHandler`:

    private ContentResolver mContentResolver;
    private AsyncStringWriterCompletionHandler mCompletionHandler;
    public AsyncStringWriter(ContentResolver contentResolver,
                             AsyncStringWriterCompletionHandler completionHandler) {
        mContentResolver = contentResolver;
        mCompletionHandler = completionHandler;
    }

where

    public interface AsyncStringWriterCompletionHandler {
        public void StringSaved(Boolean success);
    }

The `doInBackground()` method performs the actual saving of the document, as
detailed above:

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

and on completion, we return a `Boolean` specifying whether or not the save was
successful:

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mCompletionHandler.StringSaved(aBoolean);
    }


#### Using the writer

In our activity we have a `SaveText()` method which creates an `AsyncStringWriter`
and uses it to save the new text to the open file's location:

    @Override
    public void SaveText(String text) {
        Log.i(TAG, "Save the updated text");
        AsyncStringWriter stringWriter = new AsyncStringWriter(getContentResolver(), textEditorFragment);
        AsyncStringWriterParams params = new AsyncStringWriterParams(currentOpenFileUri, text);
        stringWriter.execute(params);
    }

This method is wired in to a button on the activity bar inside the editor fragment:

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_text_save) {
            if(mText != null) {
                mListener.SaveText(getEditTextContent());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

There are some additional details involving the interaction between the fragment
and the activity - you can see them in the source code without much difficulty.

#### Checking it works

So now, if you edit an open document:

![Edited document](img/text_edit_after.png)

and hit the save button, then the document will be saved, irrespective of where
it actually lives. The document above is on my Google Drive, and I can confirm
that it works by taking a look on web:

![Desktop confirmation](img/text_edit_desktop.png)


### Conclusion

I think this is really quite cool - editing cloud documents is now as easy as it
would be to edit a document on the local disk. The storage access framework
nicely abstracts away the complexities of dealing with different storage providers
into a simple `Uri`-based scheme.

There is a lot more that is possible with the SAF - including creating new
documents, deleting documents and handling document metadata. There are extensive
details available on the developer pages at
[developer.android.com](https://developer.android.com/guide/topics/providers/document-provider.html).

Hopefully all the cloud storage providers will take the opportunity to implement
storage providers so that it's really easy for users to interact with their
different sources.

Don't forget that the source code for this simple cloud text editor is all
available on Github at INSERT_GITHUB_LINK. If you have any questions or comments,
don't hesitate to pop them in below, on Github or hit me up on twitter
[@iwantmyrealname](https://twitter.com/iwantmyrealname).

