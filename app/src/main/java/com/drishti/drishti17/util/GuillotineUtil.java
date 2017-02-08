package com.drishti.drishti17.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.yalantis.guillotine.animation.GuillotineAnimation;

/**
 * Created by droidcafe on 2/9/2017
 */

public class GuillotineUtil implements View.OnClickListener {

    private static final long RIPPLE_DURATION = 250;
    private OnNavigationClickListener mListener;
    private View guillotineMenu;
    private GuillotineAnimation navAnimation;
    private TextView lastSelected ;


    public GuillotineUtil(OnNavigationClickListener mListener) {
        this.mListener = mListener;
    }

    public void setUpNav(FrameLayout rootView, View openingView, Toolbar toolbar,
                  Context context) {
        guillotineMenu = LayoutInflater.from(context).inflate(R.layout.guillotine, null);
        rootView.addView(guillotineMenu);

        navAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu
                , guillotineMenu.findViewById(R.id.guillotine_hamburger), openingView)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        lastSelected = (TextView) guillotineMenu.findViewById(R.id.competition);
        setSelectColor(lastSelected);

        guillotineMenu.findViewById(R.id.competitions_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.workshops_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.informals_group).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        TextView textView = null;
        setNormalColor(lastSelected);

        switch (view.getId()) {
            case R.id.competitions_group:
                textView = (TextView) guillotineMenu.findViewById(R.id.competition);
                break;
            case R.id.workshops_group:
                textView = (TextView) guillotineMenu.findViewById(R.id.workshop);
                break;
            case R.id.informals_group:
                textView = (TextView) guillotineMenu.findViewById(R.id.informal);
                break;

        }
        lastSelected = textView;
        setSelectColor(textView);
        mListener.onNavigationClick(view.getId());
        navAnimation.close();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setSelectColor(TextView selectedView) {
        if (Import.isVersionOK(Build.VERSION_CODES.M)) {
            selectedView.setTextColor(selectedView.getContext().getColor(R.color.selected_item_color));
        } else {
            selectedView.setTextColor(selectedView.getResources().getColor(R.color.selected_item_color));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setNormalColor(TextView selectedView) {
        if (Import.isVersionOK(Build.VERSION_CODES.M)) {
            selectedView.setTextColor(selectedView.getContext().getColor(R.color.white));
        } else {
            selectedView.setTextColor(selectedView.getResources().getColor(R.color.white));
        }
    }

    public interface OnNavigationClickListener {
        void onNavigationClick(int id);
    }

}
