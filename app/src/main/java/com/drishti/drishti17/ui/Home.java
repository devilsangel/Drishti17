package com.drishti.drishti17.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.async.services.EventsSyncService;
import com.drishti.drishti17.async.services.HighlightSyncService;
import com.drishti.drishti17.db.DBOpenHelper;
import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.db.HighlightsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.network.models.HighLightModel;
import com.drishti.drishti17.ui.adapters.HomeFlipAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;


public class Home extends AppCompatActivity implements View.OnClickListener,
        HomeFlipAdapter.Callback, FlipView.OnOverFlipListener, UIUtil.OnPromptActionCompleted {

    private static final String TAG = Home.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    ProgressDialog progressDialog;
    List<HighLightModel> flipList;
    private boolean isFirstTime = false;

    private FlipView mFlipView;
    private HomeFlipAdapter mAdapter;
    BroadcastReceiver downloadCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UIUtil.setToolBar(this, "Drishti '17");
        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        Log.d(TAG, "onCreate: going to start");

        performInitial();
        registerReceivers();
//        EventTable.deleteAll(EventTable.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flipList == null) {
            if (Import.isHighlightDownloading())
                progressDialog.showProgressDialog();
            else
                loadFlip();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.disMissProgressDialog();
        }
        if (downloadCompleteReceiver != null)
            unregisterReceiver(downloadCompleteReceiver);
    }

    private void performInitial() {
        Import.fetchRemoteConfig(this, this);
        DBOpenHelper.newInstance(this).returnSQl();

        if (EventsSyncService.checkDownload(this)) {
            EventsSyncService.startDownload(this);
        }

        if (HighlightSyncService.checkDownload(this))
            HighlightSyncService.startDownload(this);

        FirebaseMessaging.getInstance().subscribeToTopic("drishti");

    }

    private void registerReceivers() {
        downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("com.drishti.drishti17.HIGHLIGHT_LIST_UPDATED")) {
                    Log.d(TAG, "onReceive: event flipList download finished");
                    progressDialog.disMissProgressDialog();
                    if (intent.getBooleanExtra("isSyncSucess", false))
                        loadFlip();
                }
            }
        };
        registerReceiver(downloadCompleteReceiver, new IntentFilter("com.drishti.drishti17.HIGHLIGHT_LIST_UPDATED"));

    }

    private void loadFlip() {
        new AsyncLoad().execute();
    }

    private void onSuccess(List<HighLightModel> flipList) {
        progressDialog.disMissProgressDialog();

        handleFlip();
        this.flipList = flipList;
        mFlipView = (FlipView) findViewById(R.id.flipview);
        mAdapter = new HomeFlipAdapter(this, flipList);

        mAdapter.setCallback(this);
        mFlipView.setAdapter(mAdapter);
        mFlipView.peakNext(false);
        mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mFlipView.setEmptyView(findViewById(R.id.cube));
        mFlipView.setOnOverFlipListener(this);

        doHelpAction();
    }

    private void onFailure() {
        Log.d(TAG, "handleEmptyList");
        progressDialog.disMissProgressDialog();

        findViewById(R.id.flipview).setVisibility(View.GONE);
        findViewById(R.id.cube).setVisibility(View.VISIBLE);
        findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);

        doHelpAction();
    }

    private void syncFlip() {
        Log.d(TAG, "syncFlip: ");
        ApiInterface service = ApiClient.getService();
        service.getHightlightList().enqueue(new Callback<List<HighLightModel>>() {
            @Override
            public void onResponse(Call<List<HighLightModel>> call, Response<List<HighLightModel>> response) {
                if (response.isSuccessful()) {
                    List<HighLightModel> highLightModels = response.body();
                    Log.d(TAG, "onResponse: size of highligths " + highLightModels.size());
                    onSuccess(highLightModels);
                } else {
                    Log.e(TAG, "onResponse: response unsucessful ");
                    Home.this.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<HighLightModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed highlight sync", t);
                Home.this.onFailure();
            }
        });
    }

    private void handleFlip() {
        findViewById(R.id.cube).setVisibility(View.GONE);
        findViewById(R.id.empty_text).setVisibility(View.GONE);
        findViewById(R.id.empty_view).setVisibility(View.GONE);
        findViewById(R.id.flipview).setVisibility(View.VISIBLE);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                NavUtil.openNavigation(this, this, fab);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavUtil.isFromNav(requestCode)) {
            NavUtil.handleNavigation(this, this, resultCode);
        }
    }

    @Override
    public void onPageRequested(int page) {
        Log.d(TAG, "onPageRequested: page " + page);
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode,
                           boolean overFlippingPrevious,
                           float overFlipDistance, float flipDistancePerPage) {
        Log.i("overflip", "overFlipDistance = " + overFlipDistance);
    }


    private void doHelpAction() {
        if (Import.checkShowPrompt(this, Global.PREF_HOME_PROMPT_SHOWN)) {
            isFirstTime = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    if (isFirstTime)
                        UIUtil.showPrompt(Home.this, fab, getString(R.string.prompt_home_title),
                                getString(R.string.prompt_home_desp),
                                getResources().getColor(R.color.white), Home.this);
                }
            }, 1000);
        }
    }

    @Override
    public void actionPerformed() {
        if (isFirstTime) {
            isFirstTime = !isFirstTime;
            Import.setPromptShown(this, Global.PREF_HOME_PROMPT_SHOWN);
        }
    }

    class AsyncLoad extends AsyncTask<Void, Void, List<HighLightModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.showProgressDialog();

        }

        @Override
        protected List<HighLightModel> doInBackground(Void... strings) {

            List<HighLightModel> models = HighlightsTable.getHightLight(Home.this, null, null, null);
            Log.d(TAG, "loadhightl: total no of event " + models.size());

            if (models.size() == 0) {
                List<EventModel> eventModels = EventsTable.getAllEventsMinified(Home.this, null, null, null);
                for (EventModel item : eventModels) {
                    HighLightModel flipModel = new HighLightModel(item.name, item.description,
                            item.image, item.id, item.server_id);

                    models.add(flipModel);
                }
            }
            return models;
        }

        @Override
        protected void onPostExecute(List<HighLightModel> highLightModels) {
            super.onPostExecute(highLightModels);

            if (highLightModels.size() == 0) {
                    syncFlip();
            } else {
                onSuccess(highLightModels);
            }
        }
    }

}
