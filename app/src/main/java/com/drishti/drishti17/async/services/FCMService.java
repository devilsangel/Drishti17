package com.drishti.drishti17.async.services;

import android.util.Log;

import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

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
                eventUpdated(dataReceived);
            }else  if (dataReceived.get("type").equals("HIGHLIGHT_SYNC")) {
                highLightUpdated(dataReceived);
            }
        }


    }

    private void highLightUpdated(Map<String, String> dataReceived) {
        String highlight_version = dataReceived.get("highlight_version");
        Log.d(TAG, "eventUpdated: event version " + highlight_version);

        Import.setSharedPref(this, Global.PREF_HIGHLIGHT_UPDATE_VERSION, highlight_version);
        if (FirebaseRemoteConfig.getInstance().getBoolean("highlight_instant_sync"))
            EventsSyncService.startDownload(this);

    }

    private void eventUpdated(Map<String, String> dataReceived) {
        String event_version = dataReceived.get("event_version");
        Log.d(TAG, "eventUpdated: event version " + event_version);

        Import.setSharedPref(this, Global.PREF_EVENT_UPDATE_VERSION, event_version);
        if (FirebaseRemoteConfig.getInstance().getBoolean("event_instant_sync"))
            HighlightSyncService.startDownload(this);

    }
}
