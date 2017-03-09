package com.drishti.drishti17.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.drishti.drishti17.R;
import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.network.models.EventRegistrationModel;
import com.drishti.drishti17.ui.factory.ProgressDialog;
import com.drishti.drishti17.ui.fragments.FragmentEvent_Contact;
import com.drishti.drishti17.ui.fragments.FragmentEvent_General;
import com.drishti.drishti17.ui.fragments.FragmentEvent_Rules;
import com.drishti.drishti17.util.ApiClient;
import com.drishti.drishti17.util.ApiInterface;
import com.drishti.drishti17.util.AuthUtil;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NetworkUtil;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.firebase.analytics.FirebaseAnalytics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPage extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = EventPage.class.getSimpleName();
    private int position;
    private int tabHeight, paddingHeight;
    private int id;
    private EventModel eventItem;
    ProgressDialog progressDialog;
    Boolean isRegistered=false;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        getArguments();
        position = 0;
        initUI();
        progressDialog=new ProgressDialog(EventPage.this);
        checkRegisterStatus();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Import.recordScreenView(this,"Event Page",mFirebaseAnalytics);
        mFirebaseAnalytics.logEvent(Global.FIRE_EVENT_PAGE_OPEN,new Bundle());


        findViewById(R.id.regbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent(Global.FIRE_REGISTER_CLICK,new Bundle());

                if(!NetworkUtil.isNetworkAvailable(EventPage.this)) {
                    Snackbar.make(findViewById(R.id.content_event_page), "Network Unavailable", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(Global.isguest){
                    new AlertDialog.Builder(EventPage.this).setMessage("You must complete Drishti registration in order to register for event")
                            .setPositiveButton("Register Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(EventPage.this,MainRegister.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return;
                }
                if(isRegistered){
                    Snackbar.make(findViewById(R.id.content_event_page),"Already Registered",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!eventItem.group){
                    progressDialog.showProgressDialog();
                    AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
                        @Override
                        public void tokenObtained(String token) {
                            ApiInterface service= ApiClient.getService();
                            Call<String> call=service.eventRegisterSingle(token,id);
                            Log.d("eventid",id+"");
                            Log.d("tokenid",token);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    progressDialog.disMissProgressDialog();
                                    if(response.code()==200) {
                                        Snackbar.make(findViewById(R.id.content_event_page),"Registered successfully",Snackbar.LENGTH_SHORT).show();
                                        isRegistered=true;
                                        ((Button)findViewById(R.id.register)).setText(R.string.registered);
                                    }
                                    else
                                        Snackbar.make(findViewById(R.id.content_event_page),"Registration Failed",Snackbar.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    progressDialog.disMissProgressDialog();
                                    Snackbar.make(findViewById(R.id.content_event_page),"Registration Failed",Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else{
                    startActivity(new Intent(EventPage.this,EventRegister.class).putExtra("id",id).putExtra("max-count",eventItem.maxPerGroup));
                }
            }
        });

    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
        } else {
            id = 2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setColor();
        checkRegisterStatus();
    }

    private void initUI() {
        final View view = findViewById(R.id.tab_host);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tabHeight = view.getHeight();
                loadEvent();
            }
        });
    }

    void loadEvent() {
        new AsyncLoad().execute(id);
    }


    class AsyncLoad extends AsyncTask<Integer, Void, EventModel> {

        int id;

        @Override
        protected EventModel doInBackground(Integer... integers) {
            Log.d(TAG, "doInBackground: padding " + paddingHeight + " tab " + tabHeight);
            id = integers[0];

            String where = "server_serial_id = " + id;
            EventModel model = EventsTable.getEvent(EventPage.this, where, null, null);

            Log.d(TAG, "doInBackground: no of qualified events " + id);

            return model;
        }

        @Override
        protected void onPostExecute(EventModel eventItem) {
            super.onPostExecute(eventItem);

            if (eventItem == null)
                return;

            EventPage.this.eventItem = eventItem;
            ViewPager mViewPager = (ViewPager) findViewById(R.id.vp);
            EventPagerAdapter eventPagerAdapter = new EventPagerAdapter(getSupportFragmentManager());

            //mViewPager.setVisibility(View.VISIBLE);
            mViewPager.addOnPageChangeListener(EventPage.this);
            mViewPager.setAdapter(eventPagerAdapter);

            NavigationTabStrip mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_tab);
            mTopNavigationTabStrip.setViewPager(mViewPager, 0);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    @Override
    public void onPageSelected(int position) {
        this.position = position;
        setColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setColor() {
        Drawable drawable;
        int color;
        Log.d(TAG, "setColor: position " + position);
        if (position == 0) {
            drawable = getResources().getDrawable(R.drawable.grad_event_1);
            color = R.color.event_tab_back1;
        } else if (position == 1) {
            drawable = getResources().getDrawable(R.drawable.grad_event_2);
            color = R.color.event_tab_back2;
        } else {
            drawable = getResources().getDrawable(R.drawable.grad_event_3);
            color = R.color.event_tab_back3;
        }

        findViewById(R.id.tab_host).setBackground(drawable);
        findViewById(R.id.vp).setBackground(drawable);
        ((Button)findViewById(R.id.register)).setTextColor(getResources().getColor(color));
        Import.setStatusBarColor(this, this, color);

    }

    public class EventPagerAdapter extends FragmentPagerAdapter {

        public EventPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = FragmentEvent_General.getInstance(tabHeight, eventItem);
                    break;
                case 1:
                    fragment = FragmentEvent_Rules.newInstance(tabHeight, eventItem);
                    break;
                case 2:
                    fragment = FragmentEvent_Contact.newInstance(tabHeight, eventItem);
                    break;
            }
            return fragment;
        }


        @Override
        public int getCount() {
            return 3;
        }
    }

    void checkRegisterStatus() {
        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            progressDialog.showProgressDialog();
            AuthUtil.getFirebaseToken(new AuthUtil.Listener() {
                @Override
                public void tokenObtained(String token) {
                    ApiInterface service = ApiClient.getService();
                    Call<EventRegistrationModel> call = service.isRegistered(token, id);
                    call.enqueue(new Callback<EventRegistrationModel>() {
                        @Override
                        public void onResponse(Call<EventRegistrationModel> call, Response<EventRegistrationModel> response) {
                            progressDialog.disMissProgressDialog();
                            if (response.code() == 200) {
                                if (response.body().isRegistered) {
                                    isRegistered = true;
                                    ((Button) findViewById(R.id.register)).setText(R.string.registered);
                                    Toast.makeText(EventPage.this,"After registering via app button, please make sure that you also follow registration or payment links present in the description or rules",Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Snackbar.make(findViewById(R.id.content_event_page),"Network Error",Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EventRegistrationModel> call, Throwable t) {
                            progressDialog.disMissProgressDialog();
                            Snackbar.make(findViewById(R.id.content_event_page),"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }else {
            Snackbar.make(findViewById(R.id.content_event_page),"Network Unavailable",Snackbar.LENGTH_LONG).show();
        }
    }

}
