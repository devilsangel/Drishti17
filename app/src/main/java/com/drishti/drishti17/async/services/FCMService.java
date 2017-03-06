package com.drishti.drishti17.async.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.Login;
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
        if(remoteMessage.getNotification()!=null){
            Log.d("bloo","bloo");
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
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
    private void sendNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
