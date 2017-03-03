package com.drishti.drishti17.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.drishti.drishti17.async.services.EventsSyncService;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.ui.fragments.CompetitionListFragment;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.GuillotineUtil;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.UIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventList extends AppCompatActivity implements
        GuillotineUtil.OnNavigationClickListener, View.OnClickListener {

    private static final String TAG = EventList.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    String dept;

    boolean isWorkshop, isFirstTime;
    int currentFragment = 0;
    BroadcastReceiver downloadCompleteReceiver;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        progressDialog = new ProgressDialog(this);
        getArguments();
        handleNavigation();

        registerReceivers();

        setUpUI();
        doHelpAction();
        findViewById(R.id.reload).setOnClickListener(this);
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


    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dept = bundle.getString("dept");
            if (dept.equals("WKSHOP")) {
                isWorkshop = true;
            }
        }
    }

    private void registerReceivers() {
        downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("com.drishti.drishti17.EVENT_LIST_UPDATED")) {
                    Log.d(TAG, "onReceive: event flipList download finished");
                    progressDialog.disMissProgressDialog();
                    if (intent.getBooleanExtra("isSyncSucess", false))
                        setUpFragmentUi(currentFragment);
                }
            }
        };
        registerReceiver(downloadCompleteReceiver, new IntentFilter("com.drishti.drishti17.EVENT_LIST_UPDATED"));
    }

    private void handleNavigation() {
        if (isWorkshop) {
            contentHamburger.setVisibility(View.GONE);
            TextView title = (TextView) toolbar.findViewById(R.id.title);
            title.setText("Workshops");
        } else {
            new GuillotineUtil(this).setUpNav(root, contentHamburger, toolbar, this);
        }
    }

    private void setUpUI() {
        if (Import.isEventDownloading()) {
            progressDialog.showProgressDialog();
        } else {
            setUpFragmentUi(currentFragment);
        }
    }


    private void setUpFragmentUi(int isEventWorkshop) {

        String where = isWorkshop ? "is_workshop = 1 " :
                "category = ? and is_workshop = " + isEventWorkshop;

        Log.d(TAG, "setUpFragmentUi: setting up fragments");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment competitions = fragmentManager.findFragmentByTag("competitions");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if ((competitions != null) && competitions.isAdded()) {
            Log.d(TAG, "setUpFragmentUi: some fragment already present");
            fragmentTransaction.remove(competitions);
        }
        Fragment deptListFragment = CompetitionListFragment.newInstance(dept, where);
        fragmentTransaction.add(R.id.content_event_list, deptListFragment, "competitions");
        fragmentTransaction.commit();

    }

    @Override
    public void onNavigationClick(int id) {
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        String text = "";
        switch (id) {
            case R.id.competitions_group:
                text = getString(R.string.competitions);
                currentFragment = 0;
                setUpFragmentUi(currentFragment);
                break;
            case R.id.workshops_group:
                text = getString(R.string.workshops);
                currentFragment = 1;
                setUpFragmentUi(currentFragment);
                break;
            case R.id.informals_group:
                text = getString(R.string.informals);
                break;
        }

        title.setText(text);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reload:
                reload();
                break;
        }
    }

    void reload() {
        progressDialog.showProgressDialog();
        EventsSyncService.startDownload(this);

    }

    private void doHelpAction() {
        if (Import.checkShowPrompt(this, Global.PREF_EVENTS_LIST_PROMPT_SHOWN)) {
            isFirstTime = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    if (isFirstTime && !isWorkshop)
                        UIUtil.showPrompt(EventList.this, contentHamburger,
                                getString(R.string.prompt_event_list_title),
                                getString(R.string.prompt_event_list_desp,dept));
                }
            }, 4000);
        }
    }

}

