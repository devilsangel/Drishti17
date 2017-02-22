package com.drishti.drishti17.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.async.services.Constants;
import com.drishti.drishti17.async.services.RadioService;
import com.drishti.drishti17.ui.factory.Blur.Blur;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.NavUtil;
import com.ohoussein.playpause.PlayPauseView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Cetalks extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.play_pause_view)PlayPauseView view;
    @BindView(R.id.marque_scrolling_text)TextView songName;
    IntentFilter intentFilter;
    ProgressDialog progressDialog;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetalks);
        intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.ACTION.CHANGE_STATE);
        intentFilter.addAction(Constants.ACTION.LOADED);
        intentFilter.addAction(Constants.ACTION.SONG_CHANGE);
        progressDialog=new ProgressDialog(Cetalks.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        songName.setSelected(true);
        songName.setHorizontallyScrolling(true);
        if(RadioService.iSRunning)
            view.toggle();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(Cetalks.this, RadioService.class);
                if (!RadioService.iSRunning) {
                    service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    progressDialog.showProgressDialog();
                    RadioService.iSRunning = true;
                } else {
                    service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                    RadioService.iSRunning = false;

                }

                startService(service);
            }
        });
        Blur blur=new Blur((ImageView)findViewById(R.id.radiobg),getApplicationContext(),5,5);
        blur.applyBlur(true,(findViewById(R.id.l1)));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
//        Intent service = new Intent(Cetalks.this, RadioService.class);
//        service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
//        startService(service);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    private BroadcastReceiver receiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Constants.ACTION.LOADED))
                if(progressDialog.isShowing())
                    progressDialog.disMissProgressDialog();
            if(intent.getAction().equals(Constants.ACTION.CHANGE_STATE)){
                view.toggle();
            }
            if(intent.getAction().equals(Constants.ACTION.SONG_CHANGE)){
                songName.setText(intent.getStringExtra("song"));
            }
        }
    };

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
}
