package com.drishti.drishti17.async.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NetworkUtil;

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

        String currentVersion = Import.getStringSharedPerf(context, Global.PREF_EVENT_CURRENT_VERSION);
        String remoteVersion = Import.getStringSharedPerf(context, Global.PREF_EVENT_UPDATE_VERSION);
        return (currentVersion == null) || remoteVersion == null || !currentVersion.equals(remoteVersion);
    }

    public static void startDownload(Context context) {
        Log.d(TAG, "startDownload: starting");
        Intent intent = new Intent(context, EventsSyncService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Import.setEventDownloadingStatus(true);
        if (NetworkUtil.isNetworkAvailable(this))
            download();
        else {
            sendBroadcast(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Import.setEventDownloadingStatus(false);

    }

    void download() {
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
                        EventsTable.insert(EventsSyncService.this, model);
                    }
                    sendBroadcast(true);

                } else {
                    Log.d(TAG, "onResponse: response failed");
                    sendBroadcast(false);
                }

            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed call", t);
                sendBroadcast(false);
            }
        });

        String remoteVersion = Import.getStringSharedPerf(this, Global.PREF_EVENT_UPDATE_VERSION);
        if (remoteVersion == null) {
            Import.setSharedPref(this, Global.PREF_EVENT_UPDATE_VERSION, 1);
            Import.setSharedPref(this, Global.PREF_EVENT_CURRENT_VERSION, 1);
        } else {
            Import.setSharedPref(this, Global.PREF_EVENT_CURRENT_VERSION, remoteVersion);
        }
    }

    private void cleanTable() {
        EventsTable.deleteAll(this);
    }

    private void sendBroadcast(boolean isSyncSucess) {
        Intent intent = new Intent();
        intent.setAction("com.drishti.drishti17.EVENT_LIST_UPDATED");
        intent.putExtra("isSyncSucess", isSyncSucess);
        sendBroadcast(intent);
    }


}
