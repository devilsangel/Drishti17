package com.drishti.drishti17.async.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NetworkUtil;
import com.drishti.drishti17.util.db.EventTable;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventsSyncService extends IntentService {

    private static final String TAG = EventsSyncService.class.getSimpleName();

    public EventsSyncService() {
        super("EventsSyncService");
    }

    public static boolean checkDownload(Context context) {

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String currentVersion = Import.getStringSharedPerf(context, Global.PREF_EVENT_CURRENT_VERSION);
        String remoteVersion = remoteConfig.getString("event_current_version");
        return (currentVersion == null)|| !currentVersion.equals(remoteVersion);
    }

    public static void startDownload(Context context) {
        if (!NetworkUtil.isNetworkAvailable(context))
            return;

        Log.d(TAG, "startDownload: starting");
        Intent intent = new Intent(context, EventsSyncService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
        if (NetworkUtil.isNetworkAvailable(this))
            download();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent();
        intent.setAction("com.drishti.drishti17.EVENT_LIST_UPDATED");
        sendBroadcast(intent);
    }

    void download() {
        Import.setEventDownloadingStatus(true);
        EventTable.findById(EventTable.class, 1);
        Log.d(TAG, "download: starting download");
        ApiInterface service = ApiClient.getService();
        service.getEventList().enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    cleanTable();
                    List<EventModel> eventModels = response.body();
                    Log.d(TAG, "onResponse: event list " + eventModels.size());
                    for (EventModel model : eventModels) {
                        Log.d(TAG, "onResponse: event " + model.name + " " + model.isWorkshop);
                        EventTable eventTable = new EventTable(model);
                        eventTable.save();
                    }
                } else {
                    Log.d(TAG, "onResponse: response failed");
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed call", t);
            }
        });

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String remoteVersion = remoteConfig.getString("event_current_version");

        Import.setSharedPref(this,Global.PREF_EVENT_CURRENT_VERSION,remoteVersion);
        Import.setEventDownloadingStatus(false);
    }

    private void cleanTable() {
        EventTable.deleteAll(EventTable.class);
    }


}
