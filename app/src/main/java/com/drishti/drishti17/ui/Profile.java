package com.drishti.drishti17.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.ui.adapters.ProfileScheduleAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.AuthUtil;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.db.EventTable;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.college)TextView college;
    @BindView(R.id.name)TextView name;
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.qr)ImageView qr;
    @BindView(R.id.wc)TextView wc;
    @BindView(R.id.ec)TextView ec;
    @BindView(R.id.d1)ListView d1;
    @BindView(R.id.d2)ListView d2;
    @BindView(R.id.d3)ListView d3;
    @BindView(R.id.appbar)AppBarLayout appBarLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");
        Bitmap myBitmap = QRCode.from(Global.id).withColor(0xff000000,0x00000000).withSize(300,300).bitmap();
        qr.setImageBitmap(myBitmap);
        name.setText(Global.user);
        college.setText(Global.college);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.showProgressDialog();
        AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
            @Override
            public void tokenObtained(String token) {
                ApiInterface service= ApiClient.getService();
                Call<List<EventModel>> call=service.getUserEvents(token);
                call.enqueue(new Callback<List<EventModel>>() {
                    @Override
                    public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                        progressDialog.disMissProgressDialog();
                        ArrayList<String> eventList1=new ArrayList<String>();
                        ArrayList<String> timeList1=new ArrayList<String>();
                        ArrayList<String> eventList2=new ArrayList<String>();
                        ArrayList<String> timeList2=new ArrayList<String>();
                        ArrayList<String> eventList3=new ArrayList<String>();
                        ArrayList<String> timeList3=new ArrayList<String>();
                        ArrayList<EventModel> events= (ArrayList<EventModel>) response.body();
                        int workshopCount=0,eventCount=0;
                        for(EventModel e:events){
                            if(e.isWorkshop)
                                workshopCount++;
                            else
                                eventCount++;
                            if(e.day.equals("1")) {
                                eventList1.add(e.name);
                                timeList1.add(e.time);
                            }else if(e.day.equals("2")){
                                eventList2.add(e.name);
                                timeList2.add(e.time);
                            }else{
                                eventList3.add(e.name);
                                timeList3.add(e.time);
                            }
                        }
                        wc.setText(workshopCount+"");
                        ec.setText(workshopCount+"");
                        d1.setAdapter(new ProfileScheduleAdapter(Profile.this,eventList1,timeList1));
                        CardView c1=(CardView)findViewById(R.id.c1);
                        ViewGroup.LayoutParams params=c1.getLayoutParams();
                        params.height=250+120*eventList1.size();
                        c1.setLayoutParams(params);

                        d2.setAdapter(new ProfileScheduleAdapter(Profile.this,eventList2,timeList2));
                        CardView c2=(CardView)findViewById(R.id.c2);
                        params=c2.getLayoutParams();
                        params.height=250+120*eventList1.size();
                        c2.setLayoutParams(params);

                        d3.setAdapter(new ProfileScheduleAdapter(Profile.this,eventList3,timeList3));
                        CardView c3=(CardView)findViewById(R.id.c3);
                        params=c3.getLayoutParams();
                        params.height=250+120*eventList1.size();
                        c3.setLayoutParams(params);
                    }

                    @Override
                    public void onFailure(Call<List<EventModel>> call, Throwable t) {
                        progressDialog.disMissProgressDialog();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                NavUtil.openNavigation(this,this,fab);
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
