package com.drishti.drishti17.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.Student;
import com.drishti.drishti17.ui.adapters.GrpListAdapter;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.AuthUtil;
import com.drishti.drishti17.util.Global;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventRegister extends AppCompatActivity {
    private int eventId;

    @BindView(R.id.grplst) ListView grpList;
    @BindView(R.id.plusbut)Button plus;
    @BindView(R.id.pno)EditText pno;
    ArrayList<String> group;
    ProgressDialog progressDialog;
    int maxcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(EventRegister.this);
        getArguments();
        group=new ArrayList<String>();
        group.add(Global.id);
        final ArrayList<String > names=new ArrayList<>();
        grpList.setAdapter(new GrpListAdapter(EventRegister.this,names));
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.showProgressDialog();
                ApiInterface service= ApiClient.getService();
                Call<Student> call=service.getDetails(pno.getText().toString());
                call.enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        progressDialog.disMissProgressDialog();
                        if(response.code()==200){
                            if(names.size()==(maxcount-1)){
                                Snackbar.make(findViewById(R.id.content_event_register),"Maximum Group Members Reached. Do not add yourself",Snackbar.LENGTH_SHORT).show();
                            }else {
                                group.add(response.body().id);
                                names.add(response.body().name);
                                grpList.setAdapter(new GrpListAdapter(EventRegister.this,names));
                            }
                        }else
                            Snackbar.make(findViewById(R.id.content_event_register),"Please try again, please make sure your team member has logged in atleast once",Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        progressDialog.disMissProgressDialog();
                        Snackbar.make(findViewById(R.id.content_event_register),"Please try again, please make sure your team member has logged in atleast once",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.grpsub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.showProgressDialog();
                AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
                    @Override
                    public void tokenObtained(String token) {
                        ApiInterface service=ApiClient.getService();
                        if(group.size()==1)
                            group.add(group.get(0));
                        Call<String> call=service.eventRegisterGroup(token,eventId,group);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                progressDialog.disMissProgressDialog();
                                if(response.code()==200) {
                                    Snackbar.make(findViewById(R.id.content_event_register),"Success",Snackbar.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    Snackbar.make(findViewById(R.id.content_event_register),"Please try again",Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressDialog.disMissProgressDialog();
                                Snackbar.make(findViewById(R.id.content_event_register),"Please try again",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }
    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            eventId = bundle.getInt("id");
            maxcount=bundle.getInt("max-count");


        } else {
            eventId = 2;
        }
    }
}
