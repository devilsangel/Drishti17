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
            } else if (dataReceived.get("type").equals("HIGHLIGHT_SYNC")) {
                highLightUpdated(dataReceived);
            }
        }


    }

    private void highLightUpdated(Map<String, String> dataReceived) {

        Log.d(TAG, "highLightUpdated: ");
        String current_Version = Import.getStringSharedPerf(this, Global.PREF_HIGHLIGHT_UPDATE_VERSION);
        if (current_Version != null) {
            int current = Integer.parseInt(current_Version);
            current_Version = String.valueOf(++current);
        } else
            current_Version = "1";

        Import.setSharedPref(this, Global.PREF_HIGHLIGHT_UPDATE_VERSION, current_Version);
        if (FirebaseRemoteConfig.getInstance().getBoolean("highlight_instant_sync"))
            HighlightSyncService.startDownload(this);

    }

    private void eventUpdated(Map<String, String> dataReceived) {
        String current_Version = Import.getStringSharedPerf(this, Global.PREF_EVENT_UPDATE_VERSION);
        if (current_Version != null) {
            int current = Integer.parseInt(current_Version);
            current_Version = String.valueOf(++current);
        } else
            current_Version = "1";

        Import.setSharedPref(this, Global.PREF_EVENT_UPDATE_VERSION, current_Version);
        if (FirebaseRemoteConfig.getInstance().getBoolean("event_instant_sync"))
            EventsSyncService.startDownload(this);

    }
}
