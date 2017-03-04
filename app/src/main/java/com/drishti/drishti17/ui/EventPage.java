package com.drishti.drishti17.ui;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.drishti.drishti17.R;
import com.drishti.drishti17.db.EventsTable;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.ui.fragments.FragmentEvent_Contact;
import com.drishti.drishti17.ui.fragments.FragmentEvent_General;
import com.drishti.drishti17.ui.fragments.FragmentEvent_Rules;
import com.drishti.drishti17.util.Import;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class EventPage extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = EventPage.class.getSimpleName();
    private int position;
    private int tabHeight, paddingHeight;
    private int id;
    private EventModel eventItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        getArguments();
        position = 0;
        initUI();

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

            String where = "server_id = " + id;
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
        findViewById(R.id.register).setBackground(drawable);
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

}
