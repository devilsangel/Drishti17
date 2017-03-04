package com.drishti.drishti17.async.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drishti.drishti17.db.HighlightsTable;
import com.drishti.drishti17.network.models.HighLightModel;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HighlightSyncService extends IntentService {

    private static final String TAG = HighlightSyncService.class.getSimpleName();

    public HighlightSyncService() {
        super("HighlightSyncService");
    }

    public static boolean checkDownload(Context context) {

        String currentVersion = Import.getStringSharedPerf(context, Global.PREF_HIGHLIGHT_UPDATE_VERSION);
        String remoteVersion = Import.getStringSharedPerf(context, Global.PREF_HIGHLIGHT_CURRENT_VERSION);
        return (currentVersion == null) || remoteVersion == null || !currentVersion.equals(remoteVersion);
    }

    public static void startDownload(Context context) {
        Log.d(TAG, "startDownload: starting");
        Intent intent = new Intent(context, HighlightSyncService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent: starting");
        Import.setHighlightDownloadingStatus(true);
        if (NetworkUtil.isNetworkAvailable(this))
            download();
        else {
            Log.d(TAG, "onHandleIntent: no net");
            sendBroadcast(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Import.setHighlightDownloadingStatus(false);

    }

    void download() {
        Log.d(TAG, "download: ");
        ApiInterface service = ApiClient.getService();
        service.getHightlightList().enqueue(new Callback<List<HighLightModel>>() {
            @Override
            public void onResponse(Call<List<HighLightModel>> call, Response<List<HighLightModel>> response) {
                Log.d(TAG, "onResponse");

                if (response.isSuccessful()) {
                    cleanTable();
                    List<HighLightModel> highLightModels = response.body();
                    Log.d(TAG, "onResponse: highlight list " + highLightModels.size());
                    for (HighLightModel model : highLightModels) {
                        HighlightsTable.insert(HighlightSyncService.this, model);
                    }
                    sendBroadcast(true);

                } else {
                    Log.d(TAG, "onResponse: response failed");
                    sendBroadcast(false);
                }

            }

            @Override
            public void onFailure(Call<List<HighLightModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed call", t);
                sendBroadcast(false);
            }
        });

        String remoteVersion = Import.getStringSharedPerf(this, Global.PREF_HIGHLIGHT_UPDATE_VERSION);
        if (remoteVersion == null) {
            Import.setSharedPref(this, Global.PREF_HIGHLIGHT_CURRENT_VERSION, "1");
            Import.setSharedPref(this, Global.PREF_HIGHLIGHT_UPDATE_VERSION, "1");
        } else {
            Import.setSharedPref(this, Global.PREF_HIGHLIGHT_CURRENT_VERSION, remoteVersion);
        }
    }

    private void cleanTable() {
        HighlightsTable.deleteAll(this);
    }

    private void sendBroadcast(boolean isSyncSucess) {
        Log.d(TAG, "sendBroadcast "+isSyncSucess);
        Intent intent = new Intent();
        intent.setAction("com.drishti.drishti17.HIGHLIGHT_LIST_UPDATED");
        intent.putExtra("isSyncSucess", isSyncSucess);
        sendBroadcast(intent);
    }


}
