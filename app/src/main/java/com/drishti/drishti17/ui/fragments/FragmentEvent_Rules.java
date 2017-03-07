package com.drishti.drishti17.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.UIUtil;
import com.google.firebase.analytics.FirebaseAnalytics;


public class FragmentEvent_Rules extends Fragment {

    private static final String TAG = FragmentEvent_Rules.class.getSimpleName();
    private int paddingHeight;
    private EventModel eventItem;
    private FirebaseAnalytics mFirebaseAnalytics;

    public FragmentEvent_Rules() {
    }

    public static FragmentEvent_Rules newInstance(int tabHeight, EventModel eventItem) {
        FragmentEvent_Rules fragment = new FragmentEvent_Rules();
        fragment.setEventItem(eventItem);

        Bundle bundle = new Bundle();
        bundle.putInt("padding_height", tabHeight);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paddingHeight = getArguments().getInt("padding_height");
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Import.recordScreenView(getActivity(),"Fragment rules",mFirebaseAnalytics);
        mFirebaseAnalytics.logEvent(Global.FIRE_EVENT_RULES_OPEN,new Bundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_event__rules, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: view created "+paddingHeight);
        changePadding(paddingHeight);
    }


    public void changePadding(int paddingHeight) {
        this.paddingHeight = paddingHeight;
        final View view1 = getActivity().findViewById(R.id.content_rules);
        view1.setPadding(0,paddingHeight,0,
                (int)getResources().getDimension(R.dimen.event_view_pager_padding_bottom));
        loadUI();
    }

    private void loadUI() {
        if (eventItem == null) {
            return;
        }

        String rules = eventItem.format;
        rules = preCheckRules(rules);

        Log.d(TAG, "loadUI: ");
        TextView textView = (TextView) getActivity().findViewById(R.id.rules);
        UIUtil.printHTML(textView,rules);
    }

    private String preCheckRules(String rules) {
        if (rules == null || rules.equals("")) {
            rules = "Hurray!! No rules for this event. Go Wild!!";
        }

        //TODO handle img regex search here
        return rules;
    }

    private void setEventItem(EventModel eventItem) {
        this.eventItem = eventItem;
    }

}
