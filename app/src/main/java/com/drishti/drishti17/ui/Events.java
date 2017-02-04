package com.drishti.drishti17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.fragments.DeptListFragment;
import com.drishti.drishti17.util.NavUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Events extends AppCompatActivity implements DeptListFragment.OnDepartmentSelectListener, View.OnClickListener {

    FragmentManager fragmentManager;
    private static final String TAG = Events.class.getSimpleName();
    @BindView(R.id.fab)  FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        setUpFragmentUi();

        fab.setOnClickListener(this);

    }

    private void setUpFragmentUi() {

        Log.d(TAG, "setUpFragmentUi: setting up fragments");
        Fragment inputMobile = fragmentManager.findFragmentByTag("dept");
        if ((inputMobile == null) || !inputMobile.isAdded()) {
            Fragment deptListFragment = DeptListFragment.newInstance("events",this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content_events, deptListFragment, "dept");
            fragmentTransaction.commit();
        } else {
            Log.d(TAG, "setUpFragmentUi: some fragment already present");
        }

    }

    @Override
    public void onDepartmentSelect(String deptId) {
        Log.d(TAG, "onDepartmentSelect: department clicked "+deptId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
}
