package com.drishti.drishti17.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.services.Constants;
import com.drishti.drishti17.services.RadioService;
import com.drishti.drishti17.ui.factory.Blur.Blur;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.ohoussein.playpause.PlayPauseView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class Cetalks extends AppCompatActivity {
    @BindView(R.id.play_pause_view)PlayPauseView view;
    IntentFilter intentFilter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetalks);
        intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.ACTION.CHANGE_STATE);
        intentFilter.addAction(Constants.ACTION.LOADED);
        progressDialog=new ProgressDialog(Cetalks.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ButterKnife.bind(this);

        TextView marque = (TextView) this.findViewById(R.id.marque_scrolling_text);
        marque.setSelected(true);
        marque.setHorizontallyScrolling(true);

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
        Intent service = new Intent(Cetalks.this, RadioService.class);
        service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(service);
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
                progressDialog.disMissProgressDialog();
            if(intent.getAction().equals(Constants.ACTION.CHANGE_STATE)){
                view.toggle();
            }
        }
    };
}
