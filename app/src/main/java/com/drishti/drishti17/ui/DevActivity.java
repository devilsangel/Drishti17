package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.adapters.DevListAdapter;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DevActivity.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        UIUtil.setToolBar(this, "The Team");

        ButterKnife.bind(this);
        fab.setOnClickListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Import.recordScreenView(this,"Dev page",mFirebaseAnalytics);
        mFirebaseAnalytics.logEvent(Global.FIRE_DEV_OPEN,new Bundle());

        setUI();
    }

    private void setUI() {
        RecyclerView devlist = (RecyclerView) findViewById(R.id.dev_list);
        devlist.setHasFixedSize(true);
        devlist.setLayoutManager(new LinearLayoutManager(this));
        devlist.setAdapter(new DevListAdapter(this,this));

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
}
