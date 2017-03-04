package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.db.DBOpenHelper;
import com.drishti.drishti17.db.EventsTable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;


public class Home extends AppCompatActivity implements View.OnClickListener,
        HomeFlipAdapter.Callback, FlipView.OnOverFlipListener {

    private static final String TAG = Home.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    ProgressDialog progressDialog;
    List<HighLightModel> flipList;
    private boolean isFirstTime = false;

    private FlipView mFlipView;
    private HomeFlipAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UIUtil.setToolBar(this, "Drishti '17");
        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        Log.d(TAG, "onCreate: going to start");
        Import.fetchRemoteConfig(this, this);

        doHelpAction();
        DBOpenHelper.newInstance(this).returnSQl();
//        EventTable.deleteAll(EventTable.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flipList == null) {
            syncFlip();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.disMissProgressDialog();
        }
    }

    private void syncFlip() {
        Log.d(TAG, "syncFlip: ");
        progressDialog.showProgressDialog();
        ApiInterface service = ApiClient.getService();
        service.getHightlightList().enqueue(new Callback<List<HighLightModel>>() {
            @Override
            public void onResponse(Call<List<HighLightModel>> call, Response<List<HighLightModel>> response) {
                if (response.isSuccessful()) {
                    List<HighLightModel> highLightModels = response.body();
                    Log.d(TAG, "onResponse: size of highligths " + highLightModels.size());
                    setupFlip(highLightModels);
                } else {
                    Log.e(TAG, "onResponse: response unsucessful ");
                    handleEmptyFlip();
                }
            }

            @Override
            public void onFailure(Call<List<HighLightModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed highlight sync", t);
                handleEmptyFlip();
            }


        });
    }

    private void setupFlip(List<HighLightModel> flipList) {

        if (progressDialog != null)
            progressDialog.disMissProgressDialog();

        if (flipList == null || flipList.isEmpty()) {
            handleEmptyList();
            return;
        }
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
    }

    private void handleEmptyFlip() {
        List<EventModel> eventModels = EventsTable.getAllEventsMinified(this,null,null,null);
        List<HighLightModel> flipList = new ArrayList<>();
        for (EventModel item : eventModels) {
            HighLightModel flipModel = new HighLightModel(item.name, item.description,
                    item.image, item.server_id);

            flipList.add(flipModel);
        }

        Log.d(TAG, "loadEvents: event no " + eventModels.size());
        setupFlip(flipList);
    }

    private void handleFlip() {
        findViewById(R.id.cube).setVisibility(View.GONE);
        findViewById(R.id.empty_text).setVisibility(View.GONE);
        findViewById(R.id.empty_view).setVisibility(View.GONE);
        findViewById(R.id.flipview).setVisibility(View.VISIBLE);
    }

    private void handleEmptyList() {
        Log.d(TAG, "handleEmptyList");
        findViewById(R.id.flipview).setVisibility(View.GONE);
        findViewById(R.id.cube).setVisibility(View.VISIBLE);
        findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                NavUtil.openNavigation(this, this, fab);
                if (isFirstTime) {
                    isFirstTime = !isFirstTime;
                    Import.setPromptShown(this, Global.PREF_HOME_PROMPT_SHOWN);
                }
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
                                getString(R.string.prompt_home_desp));
                }
            }, 5000);
        }
    }
}
