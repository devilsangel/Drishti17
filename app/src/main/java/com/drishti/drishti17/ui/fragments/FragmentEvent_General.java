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
import com.drishti.drishti17.network.models.EventModel;
import com.drishti.drishti17.util.Import;
import com.drishti.drishti17.util.UIUtil;

public class FragmentEvent_General extends Fragment {


    private static final String TAG = FragmentEvent_General.class.getSimpleName();
    private int paddingTop;
    private EventModel eventItem;

    public FragmentEvent_General() {
        // Required empty public constructor
    }

    public static FragmentEvent_General getInstance(int paddingTop,
                                                    EventModel eventItem) {

        FragmentEvent_General fragmentEvent_general = new FragmentEvent_General();

        fragmentEvent_general.setEventItem(eventItem);
        Bundle bundle = new Bundle();
        bundle.putInt("padding_height_top", paddingTop);
        fragmentEvent_general.setArguments(bundle);
        return fragmentEvent_general;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paddingTop = getArguments().getInt("padding_height_top");
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

        Log.d(TAG, "onViewCreated: view created " + paddingTop);
        setUI(paddingTop);
    }


    public void setUI(int paddingHeight) {
        this.paddingTop = paddingHeight;
        final View view1 = getActivity().findViewById(R.id.content_general);
        view1.setPadding(0, paddingHeight, 0,
                (int) getResources().getDimension(R.dimen.event_view_pager_padding_bottom));
        loadUI();
    }

    private void loadUI() {
        if (eventItem == null) {
            return;
        }

        TextView title = (TextView) getActivity().findViewById(R.id.title);
        TextView desp = (TextView) getActivity().findViewById(R.id.description);

        title.setText(eventItem.name);
        UIUtil.printHTML(desp, eventItem.description);

        setFirstView(R.id.layout_time, Import.daytoDate(eventItem.day), R.drawable.icon_date);
        setSecondView(R.id.layout_time, eventItem.time, R.drawable.icon_time);

        setFirstView(R.id.layout_reg, "Rs " + eventItem.regFee, R.drawable.icon_fees);

         setNoMembers();

        Log.d(TAG, "loadUI: is workshop " + eventItem.isWorkshop);
        if (eventItem.isWorkshop) {
            loadWorkshop();
        } else {
            loadCompetition();
        }
    }

    private void setFirstView(int parentViewId, String text, int iconDrawable) {
        View parentView = getActivity().findViewById(parentViewId);
        if (text == null || text.equals("0")) {
            return;
        }
        Log.d(TAG, "setFirstView: ");
        parentView.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
        setView(parentViewId, R.id.text1, R.id.icon1, text, iconDrawable);
    }

    private void setSecondView(int parentViewId, String text, int iconDrawable) {
        View parentView = getActivity().findViewById(parentViewId);

        if (text == null || text.equals("0")) {
            return;
        }
        // disapperDivider(parentView);
        Log.d(TAG, "setSecondView: setting second view");
        parentView.findViewById(R.id.layout2).setVisibility(View.VISIBLE);
        setView(parentViewId, R.id.text2, R.id.icon2, text, iconDrawable);
    }

    private void setNoMembers() {
        int icon_pic;
        String mem;
        if (eventItem.group) {
            icon_pic = R.drawable.icon_users;
            mem = getResources().getQuantityString(R.plurals.members,
                    eventItem.maxPerGroup, eventItem.maxPerGroup);
        } else {
            icon_pic = R.drawable.icon_user;
            mem = getResources().getQuantityString(R.plurals.members, 1);
        }
        setSecondView(R.id.layout_reg, mem, icon_pic);
    }

    private void setView(int parentViewId, int textViewId, int iconId, String text, int iconDrawable) {

        View parentView = getActivity().findViewById(parentViewId);
        Log.d(TAG, "setView: visibility of parent view " + parentView.isShown());
        if (!parentView.isShown()) parentView.setVisibility(View.VISIBLE);


        ImageView icon = (ImageView) parentView.findViewById(iconId);
        icon.setVisibility(View.VISIBLE);

        Drawable iconPic = UIUtil.getDrawable(getContext(), iconDrawable);
        icon.setImageDrawable(iconPic);

        TextView textView = (TextView) parentView.findViewById(textViewId);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);

    }

    private void disapperDivider(View parentView) {
        parentView.findViewById(R.id.line).setVisibility(View.VISIBLE);
    }

    private void loadWorkshop() {
        getActivity().findViewById(R.id.layout_prize).setVisibility(View.GONE);
    }

    private void loadCompetition() {
        View prize = getActivity().findViewById(R.id.layout_prize);
        if (eventItem.prize1 != 0) {
            View first = prize.findViewById(R.id.include_first);
            setIncludeView(first, R.drawable.icon_first,
                    "Rs " + eventItem.prize1);
        }
        if (eventItem.prize2 != 0) {
            prize.findViewById(R.id.line_prize1).setVisibility(View.VISIBLE);
            View second = prize.findViewById(R.id.include_second);
            setIncludeView(second, R.drawable.icon_second,
                    "Rs. " + eventItem.prize2);
        }
        if (eventItem.prize3 != 0) {
            prize.findViewById(R.id.line_prize2).setVisibility(View.VISIBLE);
            View third = prize.findViewById(R.id.include_third);
            setIncludeView(third, R.drawable.icon_third,
                    "Rs. " + eventItem.prize3);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setIncludeView(View parentView, int iconDrawableId, String text) {

        parentView.setVisibility(View.VISIBLE);
        Drawable drawable;
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            drawable = getContext().getDrawable(iconDrawableId);
        } else {
            drawable = getResources().getDrawable(iconDrawableId);
        }
        ((ImageView) parentView.findViewById(R.id.icon)).setImageDrawable(drawable);
        ((TextView) parentView.findViewById(R.id.text)).setText(text);
    }


    private void setEventItem(EventModel eventItem) {
        this.eventItem = eventItem;
    }


}
