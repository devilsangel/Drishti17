package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.async.services.EventsSyncService;
import com.drishti.drishti17.ui.adapters.HomeFlipAdapter;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;


public class Home extends AppCompatActivity implements View.OnClickListener,
        HomeFlipAdapter.Callback, FlipView.OnOverFlipListener {

    private static final String TAG = Home.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FlipView mFlipView;
    private HomeFlipAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UIUtil.setToolBar(this,"Drishti '17");
        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        Log.d(TAG, "onCreate: going to start");
        Import.fetchRemoteConfig(this,this);
        setupFlip();

    }

    private void setupFlip() {
        mFlipView = (FlipView) findViewById(R.id.flipview);
        mAdapter = new HomeFlipAdapter(this);

        mAdapter.setCallback(this);
        mFlipView.setAdapter(mAdapter);
        mFlipView.peakNext(false);
        mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mFlipView.setEmptyView(findViewById(R.id.empty_view));
        mFlipView.setOnOverFlipListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                NavUtil.openNavigation(this,this,fab);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (NavUtil.isFromNav(requestCode)) {
            NavUtil.handleNavigation(this,this,resultCode);
        }
    }

    @Override
    public void onPageRequested(int page) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode,
                           boolean overFlippingPrevious,
                           float overFlipDistance, float flipDistancePerPage) {
        Log.i("overflip", "overFlipDistance = "+overFlipDistance);
    }
}
