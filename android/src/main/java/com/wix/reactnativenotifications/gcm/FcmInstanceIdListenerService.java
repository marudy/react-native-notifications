package com.wix.reactnativenotifications.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wix.reactnativenotifications.core.notification.IPushNotification;
import com.wix.reactnativenotifications.core.notification.PushNotification;

import java.util.Map;

import static com.wix.reactnativenotifications.Defs.LOGTAG;

/**
 * Instance-ID + token refreshing handling service. Contacts the GCM to fetch the updated token.
 *
 * @author amitd
 */
public class FcmInstanceIdListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message){
        Bundle bundle = message.toIntent().getExtras();
        Log.d(LOGTAG, "New message from GCM: " + bundle);
        /* Modifications so that Android native push modal works along with Braze's payload object structure */
        // Retrieve message title from bundle and put it back to bundle as it is expected from this package. Key "t" is retrieved and key "title" is added
        bundle.putString("title", bundle.getString("t"));
        // Retrieve message body from bundle and put it back to bundle as it is expected from this package. Key "a" is retrieved and key "body" is added
        bundle.putString("body", bundle.getString("a"));

        try {
            final IPushNotification notification = PushNotification.get(getApplicationContext(), bundle);
            notification.onReceived();
        } catch (IPushNotification.InvalidNotificationException e) {
            // A GCM message, yes - but not the kind we know how to work with.
            Log.v(LOGTAG, "GCM message handling aborted", e);
        }
    }
}
