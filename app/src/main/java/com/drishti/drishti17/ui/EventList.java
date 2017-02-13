package com.drishti.drishti17.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.fragments.CompetitionListFragment;
import com.drishti.drishti17.util.GuillotineUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventList extends AppCompatActivity implements
        GuillotineUtil.OnNavigationClickListener {

    private static final String TAG = EventList.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    String dept;

    BroadcastReceiver downloadCompleteReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        getArguments();
        new GuillotineUtil(this).setUpNav(root, findViewById(R.id.content_hamburger), toolbar, this);

        registerReceivers();
        setUpFragmentUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(downloadCompleteReceiver != null)
            unregisterReceiver(downloadCompleteReceiver);
    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dept = bundle.getString("dept");

        }
    }

    private void registerReceivers() {
        downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().matches("com.drishti.drishti17.EVENT_LIST_UPDATED")){
                    Log.d(TAG, "onReceive: event list download finished");
                }
            }
        };
        registerReceiver(downloadCompleteReceiver,new IntentFilter("com.drishti.drishti17.EVENT_LIST_UPDATED"));
    }


    private void setUpFragmentUi() {

        Log.d(TAG, "setUpFragmentUi: setting up fragments");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment competitions = fragmentManager.findFragmentByTag("competitions");
        if ((competitions == null) || !competitions.isAdded()) {
            Fragment deptListFragment = CompetitionListFragment.newInstance(dept);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content_event_list, deptListFragment, "competitions");
            fragmentTransaction.commit();
        } else {
            Log.d(TAG, "setUpFragmentUi: some fragment already present");
        }

    }

    @Override
    public void onNavigationClick(int id) {
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        String text ="";
        switch (id) {
            case R.id.competitions_group:
                text = getString(R.string.competitions);
                break;
            case R.id.workshops_group:
                text = getString(R.string.workshops);
                break;
            case R.id.informals_group:
                text = getString(R.string.informals);
                break;
        }

        title.setText(text);
    }











}
