package com.drishti.drishti17.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.UIUtil;
import com.drishti.drishti17.util.db.EventTable;

public class FragmentEvent_General extends Fragment {


    private static final String TAG = FragmentEvent_General.class.getSimpleName();
    private int paddingHeight;
    private EventTable eventItem;

    public FragmentEvent_General() {
        // Required empty public constructor
    }

    public static FragmentEvent_General getInstance(int tabHeight, EventTable eventItem) {

        FragmentEvent_General fragmentEvent_general = new FragmentEvent_General();

        fragmentEvent_general.setEventItem(eventItem);
        Bundle bundle = new Bundle();
        bundle.putInt("padding_height", tabHeight);
        fragmentEvent_general.setArguments(bundle);
        return fragmentEvent_general;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paddingHeight = getArguments().getInt("padding_height");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_event__general, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: view created "+paddingHeight);
        changePadding(paddingHeight);
    }


    public void changePadding(int paddingHeight) {
        this.paddingHeight = paddingHeight;
        final View view1 = getActivity().findViewById(R.id.content_general);
        view1.setPadding(0,paddingHeight,0,0);
        view1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d(TAG, "onGlobalLayout: setting padding");
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);


            }
        });

        loadUI();
    }

    private void loadUI() {
        if (eventItem == null) {
            return;
        }

        TextView title = (TextView) getActivity().findViewById(R.id.title);
        TextView desp = (TextView) getActivity().findViewById(R.id.description);

        title.setText(eventItem.name);
        UIUtil.printHTML(desp,eventItem.description);
    }


    private void setEventItem(EventTable eventItem) {
        this.eventItem = eventItem;
    }


}
