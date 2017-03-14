package com.drishti.drishti17.ui;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.fragments.FragmentSchedule;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.NavUtil;
import com.drishti.drishti17.util.UIUtil;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Schedule extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        UIUtil.setToolBar(this, "Schedule");

        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Import.recordScreenView(this,"Schedule",mFirebaseAnalytics);
        mFirebaseAnalytics.logEvent(Global.FIRE_SCH_OPEN,new Bundle());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp1);
        ScheduleAdapter adapter=new ScheduleAdapter(getSupportFragmentManager());

        //mViewPager.setVisibility(View.VISIBLE);
        mViewPager.addOnPageChangeListener(Schedule.this);
        mViewPager.setAdapter(adapter);

        NavigationTabStrip mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.sched_tab);
        mTopNavigationTabStrip.setViewPager(mViewPager, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab   :
                NavUtil.openNavigation(this, this, fab);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class ScheduleAdapter extends FragmentPagerAdapter{

        public ScheduleAdapter(FragmentManager fm) {
            super(fm);
            Log.d("blooo","blooo");
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            Log.d("blooo","blooo");
            switch (position){
                case 0:
                    fragment= FragmentSchedule.newInstance("1","17","day=?");
                    break;
                case 1:
                    fragment= FragmentSchedule.newInstance("2","18","day=?");
                    break;
                case 2:
                    fragment= FragmentSchedule.newInstance("3","19","day=?");
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
