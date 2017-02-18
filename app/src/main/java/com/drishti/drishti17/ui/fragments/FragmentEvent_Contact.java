package com.drishti.drishti17.ui.fragments;


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
import com.drishti.drishti17.util.db.EventTable;


public class FragmentEvent_Contact extends Fragment {

    private static final String TAG = FragmentEvent_Contact.class.getSimpleName();
    private int paddingHeight;
    private EventTable eventItem;

    public FragmentEvent_Contact() {
        // Required empty public constructor
    }

    public static FragmentEvent_Contact newInstance(int tabHeight, EventTable eventItem) {
        FragmentEvent_Contact fragment = new FragmentEvent_Contact();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_event__contact, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: view created " + paddingHeight);
        changePadding(paddingHeight);
    }


    public void changePadding(int paddingHeight) {
        this.paddingHeight = paddingHeight;
        final View view1 = getActivity().findViewById(R.id.content_contact);
        view1.setPadding(0, paddingHeight, 0,
                (int) getResources().getDimension(R.dimen.event_view_pager_padding_bottom));
        loadUI();
    }

    private void loadUI() {
        if (eventItem == null) {
            return;
        }

        setContact(R.id.contact1, eventItem.contactName1,
                eventItem.contactPhone1, eventItem.contactEmail1,1);
        setContact(R.id.contact2, eventItem.contactName2,
                eventItem.contactPhone2, eventItem.contactEmail2,2);
    }

    private void setContact(int parentView, String name,
                            String phone, String email, final int mode) {

        View parent = getActivity().findViewById(parentView);

        ImageView icon_email = (ImageView) parent.findViewById(R.id.icon_email);
        ImageView icon_phone = (ImageView) parent.findViewById(R.id.icon_phone);
        TextView text_email = (TextView) parent.findViewById(R.id.email);
        TextView text_phone = (TextView) parent.findViewById(R.id.phone);

        icon_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(mode);
            }
        });
        icon_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(mode);
            }
        });
        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(mode);
            }
        });
        text_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(mode);
            }
        });

        setView((ImageView) parent.findViewById(R.id.icon_name),
                (TextView) parent.findViewById(R.id.name), name);
        setView(icon_email, text_email, email);
        setView(icon_phone, text_phone, phone);
    }

    private void setView(ImageView icon, TextView textView, String text) {
        if (text == null || text.equals("0"))
            return;
        icon.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
    }

    private void setEventItem(EventTable eventItem) {
        this.eventItem = eventItem;
    }

    private void sendMail(int mode){
        String address;
        if(mode == 1){
            address = eventItem.contactEmail1;
        }else {
            address = eventItem.contactEmail2;
        }

        if (address == null)return;
        String[] addresses = {address};
        Import.composeEmail(getActivity(),addresses,"Drishti "+eventItem.name);
    }

    private void callPhone(int mode) {
        String phone;
        if (mode == 1) {
            phone = eventItem.contactPhone1;
        } else {
            phone = eventItem.contactPhone2;
        }

        if(phone == null) return;
        Import.callIntent(getActivity(),phone);
    }


}