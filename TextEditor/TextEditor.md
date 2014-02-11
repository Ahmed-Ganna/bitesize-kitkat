# Android KitKat: finger-by-finger 

## Creating a text editor for the cloud

The new storage access framework in KitKat provides a unified interface to a
multitude of file storage options - from on disk to cloud storage services. This
means that it's easy for any apps to view, edit and create apps across a wide
range of of storage providers.

In this article we'll take a look at how to access the storage access framework
as a consumer - i.e. providing access to storage endpoints. The framework also
supports creating new storage endpoints - as a cloud storage provider (e.g.
dropbox) might want to. We're going to create a really simple text-editor app,
which will be able to open a text document stored in the cloud, edit it and
save the changes.

The code is available as part of the KitKat: finger-by-finger repo on Github at
GITHUB_LINK_HERE. The project is a gradle project, and should be easy to import
into Android Studio. It has been tested with Android Studio 0.4.4.


### Opening a file


### Saving a file


### Pulling it all together



### Conclusion
