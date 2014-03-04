# Android KitKat: finger-by-finger 

## Creating a replacement default SMS app

### Introduction

Before KitKat it has been possible to build fully-featured SMS apps on Android,
but it has involved using hidden APIs - somewhat less than ideal. In KitKat
Android opens up these APIs to allow developers to create apps which replace
the default SMS app - in the same way the Google Hangouts can.

In this week's article we'll take a look at what you need to do to be able to
create your own SMS app on KitKat, but first we'll have a look at how you can
receive SMS messages without taking the responsibility of being the default SMS
app on the device.

The code for the app which accompanies this article is available on Github at
GITHUB_LINK_HERE. It was developed in Android Studio 0.4.4 and therefore is a
gradle project. Any problems then feel free to give me a shout or create a pull
request to fix it :)


### Reading SMS messages

Right from the beginnings of Android, developers have been able to register to
receive SMS messages in their app - using the `RECEIVE_SMS` permission. We'll
take a brief look at how to achieve this so we can get a handle on what functionality
KitKat has added.

In order to handle incoming SMS messages we need to create a `BroadcastReceiver`,
which we will then wire up in the app manifest.

    public class SMSBroadcastReceiver extends BroadcastReceiver {
        public SMSBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for(Object currentObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) currentObj);
                        Message message = new Message(
                                currentMessage.getDisplayMessageBody(),
                                currentMessage.getDisplayOriginatingAddress(),
                                "ME",
                                new Date()
                        );
                        DataProvider.getInstance().addMessage(message);
                    }
                }
            } catch (Exception e) {
                Log.e("SMS", "Exception: " + e);
            }
        }
    }

When a new message is received the system will pass an `Intent` to the `onReceive()`
method, which will contain the message. It can be decoded into an `SMSMessage`
object using `createFromPdu()` (note, the documentation states that
`createFromPdu(byte[])` will soon be deprecated and instead we should be using
`createFromPdu(byte[], String)` and passing in additional `format` parameter.
However, whilst making this app I was unable to find this new method. Any suggestions
welcome).

We push the detail into a simple `Message` object, which is just one of the models
used inside the demo app we're creating:

    public class Message {

        private String Content;
        private String Sender;
        private String Recipient;
        private Date Time;

        public Message(String content, String sender, String recipient, Date time) {
            Content = content;
            Sender = sender;
            Recipient = recipient;
            Time = time;
        }

        ...

        @Override
        public String toString() {
            return getContent() + "  -  " + getTime().toString();
        }
    }

In our demo app, this message gets added to a singleton data provider. In a real
app this should be persisted carefully, but for the purposes of this demo the
messages are sorted into 'conversations' (i.e. messages to and from a single
address) and then made accessible to the UI:

    public void addMessage(Message message) {
        if(conversationMap.containsKey(message.getSender())) {
            // Can add the message to an existing conversation
            conversationMap.get(message.getSender()).addMessage(message);
        } else {
            // Need to create a new conversation
            Conversation conversation = new Conversation(message.getSender());
            conversation.addMessage(message);
            conversationMap.put(message.getSender(), conversation);
            conversationList.add(conversation);
        }
        // Ensure that everything gets updated
        setChanged();
        notifyObservers();
    }

The `DataProvider` is an `Observable` so that the UI can be updated as new
messages are added:

    @Override
    public void update(Observable observable, Object data) {
        // If the data adapter has changed then we need to reload the list dataadapter
        conversationArrayAdapter.notifyDataSetChanged();
    }

Now that we've created the `BroadcastReceiver` it needs to be registered in the
app manifest - inside the `application` tag:

    <receiver
        android:name="com.shinobicontrols.messageme.receivers.SMSBroadcastReceiver"
        android:enabled="true"
        android:exported="true"
        >
        <intent-filter>
            <action android:name="android.provider.Telephony.SMS_RECEIVED" />
        </intent-filter>
    </receiver>

And we must request the appropriate permissions, inside the manifest section:

    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />

At this stage (since our `DataProvider` self-populates with some sample data) the
app looks like this:

![Sample data](img/sms_sampledata.png)

#### Testing receiving SMS messages

You don't want to have to send lots of SMS messages in order to test whether your
app is capable of receiving them, however, help is at hand on the emulator. By
telnetting in to it then you can send an SMS to it, adn check that your app
responds as expected.

To open a connection:

    telnet localhost 5554

Where the port number is displayed at the top of your emulator. Then you can
use the following command to send an SMS message

    sms send <Sender Number> <Message>

For example:

![Send SMS example](img/sms_console_sms_send.png)

When you run this command then you'll see the message appear inside your app:

![SMS received](img/sms_message_received.png)

Tapping on a conversation takes you to the messages in that conversation:

![SMS conversation](img/sms_message_content.png)

This behavior is pretty standard multi-level list view navigation, with the
`DataProvider` set up to support this kind of operation. It's beyond the scope of
this article to go in to detail about this, but the code is pretty each to
follow.


### Becoming the default SMS app

Receiving SMS messages has always been possible as demonstrated above. However,
new in KitKat is the ability to become the default SMS app. Android will only allow
one app to receive the `SMS_DELIVER_ACTION` intent, which both receives messages
as they arrive and also is the only app which is allowed to write to the SMS
provider.

The kind folks at Google have written a
[great blog post](http://android-developers.blogspot.co.uk/2013/10/getting-your-sms-apps-ready-for-kitkat.html)
which explains what you need to do to be able to receive this intent, but it is
lacking any sample code. We'll follow that as a basis and go through what you
need to be able to do.

Your app must be able to do the following in order that the system allow it to
be marked as the default SMS app:

- Receive SMS messages
- Receive MMS messages
- Handle requests to send messages, with UI
- Provide a headless service to send SMS messages without UI - e.g. for responding
to incoming calls


#### Create a broadcast receiver for `SMS_DELIVER_ACTION`

We've already created a broadcast receiver for SMS - nothing has changed with the
class itself, we just need to wire it slightly differently in the app manifest:

    <!-- BroadcastReceiver that listens to incoming SMS messages -->
    <receiver
        android:name="com.shinobicontrols.messageme.receivers.SMSBroadcastReceiver"
        android:permission="android.permission.BROADCAST_SMS" >
        <intent-filter>
            <action android:name="android.provider.Telephony.SMS_DELIVER" />
        </intent-filter>
    </receiver>

The intent filter has changed from `SMS_RECEIVED` to `SMS_DELIVER`, and the
permission we require is `BROADCAST_SMS`.

#### Create a broadcast receiver for `WAP_PUSH_DELIVER_ACTION`

Creating a broadcast receiver for WAP messages is actually very similar to that
for SMS messages - it's a class which inherits from `BroadcastReceiver` and provides
an implementation for the `onReceive()` method. In this sample project we aren't
actually going to provide a useful implementation, and hence this sample code
wouldn't be appropriate for production use - every time a MMS arrives then the
app would crash:

    public class MMSBroadcastReceiver extends BroadcastReceiver {
        public MMSBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

The manifest file needs updating in a similar way:

    <!-- BroadcastReceiver that listens to incoming MMS messages -->
    <receiver
        android:name="com.shinobicontrols.messageme.receivers.MMSBroadcastReceiver"
        android:permission="android.permission.BROADCAST_WAP_PUSH" >
        <intent-filter>
            <action android:name="android.provider.Telephony.WAP_PUSH_DELIVER" />
            <data android:mimeType="application/vnd.wap.mms-message" />
        </intent-filter>
    </receiver>

Here we need the `BROADCAST_WAP_PUSH` permission and the intent action is
`WAP_PUSH_DELIVER`.

#### Create an activity for sending new SMS/MMS

It's important that if a user chooses to send some content via SMS, then the
messaging app should be able to display the message to them and provide sending
capability. This requires an activity be able to handle a couple of intents - 
`SEND` and `SENDTO`. This is done by updating the manifest as follows:

    <!-- Activity for composing SMS/MMS messages -->
    <activity
        android:name="com.shinobicontrols.messageme.ComposeSMSActivity"
        android:label="@string/title_activity_compose_sms" >
        <intent-filter>
            <action android:name="android.intent.action.SEND" />
            <action android:name="android.intent.action.SENDTO" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="sms" />
            <data android:scheme="smsto" />
            <data android:scheme="mms" />
            <data android:scheme="mmsto" />
        </intent-filter>
    </activity>

The moment that you actually want to send an SMS message you need to get hold of
the `SmsManager` and call `sendTextMessage()` on it:

    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(recipient, "ME", message, null, null);


#### Create a service for sending headless messages

The final piece of the puzzle is creating a service which is able to respond to
requests from the OS to send messages in headless mode. This occurs when a user
chooses to send a pre-written text to somebody when they reject their call. In
this instance this is provided by the aptly named `HeadlessSmsSendService`
class. This extends `Service` and in the implementation provided here, this
doesn't actually do anything. This would obviously need resolving if you were
creating a fully-featured SMS app, but it is beyond the scope of this post.

In order to wire this service, add the following to the `<application>` section
of __AndroidManifest.xml__

    <!-- Service that delivers messages for "Quick Response" -->
    <service
        android:name="com.shinobicontrols.messageme.HeadlessSmsSendService"
        android:exported="true"
        android:permission="android.permission.SEND_RESPOND_VIA_MESSAGE" >
        <intent-filter>
            <action android:name="android.intent.action.RESPOND_VIA_MESSAGE" />
            <category android:name="android.intent.category.DEFAULT" />
            <data android:scheme="sms" />
            <data android:scheme="smsto" />
            <data android:scheme="mms" />
            <data android:scheme="mmsto" />
        </intent-filter>
    </service>

The important parts of this are detailed below:

- The service should be exported: `android:exported="true"`
- The service requires the `SEND_RESPOND_VIA_MESSAGE` permission
- It should respond to the `RESPOND_VIA_MESSAGE` intent, for all the different
different schema.


### Setting the default SMS messaging app

Once you've implemented all four of the above requirements then Android will be
satisfied that your app is suitable to be used as the default SMS app for the
system. There are a couple of options available to you to set this. The first
is using the Android settings app:

![Choose the more section](img/sms_settings_default_app_1.png)
![Default SMS app](sms_settings_default_app_2.png)
![2 Options](sms_settings_default_app_3.png)



### Conclusion
