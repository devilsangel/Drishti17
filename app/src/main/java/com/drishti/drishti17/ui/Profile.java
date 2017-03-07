package com.drishti.drishti17.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.drishti.drishti17.util.NetworkUtil;
import com.google.firebase.auth.FirebaseAuth;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
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
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,Login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
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
        SharedPreferences sharedPreferences=getSharedPreferences("drishti", Context.MODE_PRIVATE);
        Bitmap myBitmap = QRCode.from(sharedPreferences.getString("id","blank")).withColor(0xff1B232E,0xffffffff).withSize(250,250).bitmap();
        qr.setImageBitmap(myBitmap);
        name.setText(Global.user);
        college.setText(Global.college);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        if(Global.isguest){
            new AlertDialog.Builder(Profile.this).setMessage("You must complete Drishti registration in order to see Profile")
                    .setPositiveButton("Register Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Profile.this,MainRegister.class));
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }
        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            progressDialog.showProgressDialog();
            AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
                @Override
                public void tokenObtained(String token) {
                    ApiInterface service = ApiClient.getService();
                    Call<List<EventModel>> call = service.getUserEvents(token);
                    call.enqueue(new Callback<List<EventModel>>() {
                        @Override
                        public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                            progressDialog.disMissProgressDialog();
                            ArrayList<String> eventList1 = new ArrayList<String>();
                            ArrayList<String> timeList1 = new ArrayList<String>();
                            ArrayList<Integer> idList1=new ArrayList<Integer>();
                            ArrayList<String> eventList2 = new ArrayList<String>();
                            ArrayList<String> timeList2 = new ArrayList<String>();
                            ArrayList<Integer> idList2=new ArrayList<Integer>();
                            ArrayList<String> eventList3 = new ArrayList<String>();
                            ArrayList<String> timeList3 = new ArrayList<String>();
                            ArrayList<Integer> idList3=new ArrayList<Integer>();

                            ArrayList<EventModel> events = (ArrayList<EventModel>) response.body();
                            int workshopCount = 0, eventCount = 0;
                            for (EventModel e : events) {
                                if (e.isWorkshop)
                                    workshopCount++;
                                else
                                    eventCount++;
                                if (e.day.equals("1") || e.day.equals("17")) {
                                    eventList1.add(e.name);
                                    timeList1.add(e.time);
                                    idList1.add(e.server_id);
                                } else if (e.day.equals("2") || e.day.equals("18")) {
                                    eventList2.add(e.name);
                                    timeList2.add(e.time);
                                    idList2.add(e.server_id);
                                } else {
                                    eventList3.add(e.name);
                                    timeList3.add(e.time);
                                    idList3.add(e.server_id);
                                }
                            }
                            wc.setText(workshopCount + "");
                            ec.setText(eventCount + "");
                            d1.setAdapter(new ProfileScheduleAdapter(Profile.this, eventList1, timeList1,idList1));
                            CardView c1 = (CardView) findViewById(R.id.c1);
                            ViewGroup.LayoutParams params = c1.getLayoutParams();
                            params.height = 250 + 120 * eventList1.size();
                            c1.setLayoutParams(params);

                            d2.setAdapter(new ProfileScheduleAdapter(Profile.this, eventList2, timeList2,idList2));
                            CardView c2 = (CardView) findViewById(R.id.c2);
                            params = c2.getLayoutParams();
                            params.height = 250 + 120 * eventList2.size();
                            c2.setLayoutParams(params);

                            d3.setAdapter(new ProfileScheduleAdapter(Profile.this, eventList3, timeList3,idList3));
                            CardView c3 = (CardView) findViewById(R.id.c3);
                            params = c3.getLayoutParams();
                            params.height = 250 + 120 * eventList3.size();
                            c3.setLayoutParams(params);
                        }

                        @Override
                        public void onFailure(Call<List<EventModel>> call, Throwable t) {
                            progressDialog.disMissProgressDialog();
                        }
                    });
                }
            });
        }else {
            Snackbar.make(findViewById(R.id.content_profile),"Network Unavailable",Snackbar.LENGTH_LONG).show();
        }
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
