package com.drishti.drishti17.ui.fragments;


import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drishti.drishti17.R;
import com.drishti.drishti17.util.Import;
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
        view1.setPadding(0,paddingHeight,0,
                (int)getResources().getDimension(R.dimen.event_view_pager_padding_bottom));
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

        setView(R.id.icon_date,R.id.date,Import.daytoDate(eventItem.day));
        setView(R.id.icon_time,R.id.time,eventItem.time);
        setView(R.id.icon_fees,R.id.fees,"Rs "+eventItem.regFee);

        setNoMembers();

        Log.d(TAG, "loadUI: is workshop "+eventItem.isWorkshop);
        if (eventItem.isWorkshop) {
            loadWorkshop();
        }else{
            loadCompetition();
        }
    }

    private void setView(int iconId,int textId,String text){
        if (text == null || text.equals("0"))
            return;
        getActivity().findViewById(iconId).setVisibility(View.VISIBLE);
        TextView textView = (TextView) getActivity().findViewById(textId);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
    }

    private void setNoMembers() {
        ImageView icon = (ImageView) getActivity().findViewById(R.id.icon_group);
        TextView text = (TextView) getActivity().findViewById(R.id.group);

        Drawable icon_pic;
        String mem;
        if (eventItem.isgroup) {
            icon_pic = UIUtil.getDrawable(getContext(), R.drawable.icon_users);
            mem = getResources().getQuantityString(R.plurals.members,eventItem.maxPerGroup,eventItem.maxPerGroup);
        } else {
            icon_pic = UIUtil.getDrawable(getContext(),R.drawable.icon_user);
            mem = getResources().getQuantityString(R.plurals.members,1);
        }

        icon.setImageDrawable(icon_pic);
        setView(R.id.icon_group,R.id.group,mem);
    }

    private void loadWorkshop() {
        getActivity().findViewById(R.id.layout_prize).setVisibility(View.GONE);
    }

    private void loadCompetition() {
        View prize = getActivity().findViewById(R.id.layout_prize);
        if (eventItem.prize1 != 0) {
            View first = prize.findViewById(R.id.include_first);
            setIncludeView(first,R.drawable.icon_first,
                    String.valueOf(eventItem.prize1));
        }
        if (eventItem.prize2 != 0) {
            View second = prize.findViewById(R.id.include_second);
            setIncludeView(second,R.drawable.icon_second,
                    String.valueOf(eventItem.prize2));
        }
        if (eventItem.prize3 != 0) {
            View third = prize.findViewById(R.id.include_third);
            setIncludeView(third,R.drawable.icon_third,
                    String.valueOf(eventItem.prize3));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setIncludeView(View parentView, int iconDrawableId, String text) {

        parentView.setVisibility(View.VISIBLE);
        Drawable drawable;
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)){
            drawable = getContext().getDrawable(iconDrawableId);
        }else {
            drawable = getResources().getDrawable(iconDrawableId);
        }
        ((ImageView)parentView.findViewById(R.id.icon)).setImageDrawable(drawable);
        ((TextView)parentView.findViewById(R.id.text)).setText(text);
    }


    private void setEventItem(EventTable eventItem) {
        this.eventItem = eventItem;
    }


}
