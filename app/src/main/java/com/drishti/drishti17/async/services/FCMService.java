package com.drishti.drishti17.async.services;

import android.util.Log;

import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by kevin on 3/3/17
 */

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = FCMService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> dataReceived = remoteMessage.getData();
            if (dataReceived.get("type").equals("EVENT_SYNC")) {
                changeEventSyncConfig(dataReceived);
            }
        }


    }

    private void changeEventSyncConfig(Map<String, String> dataReceived) {
        String event_version = dataReceived.get("event_version");
        Log.d(TAG, "changeEventSyncConfig: event version " + event_version);

        Import.setSharedPref(this, Global.PREF_EVENT_UPDATE_VERSION, event_version);
        EventsSyncService.startDownload(this);

    }
}
