package com.drishti.drishti17.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drishti.drishti17.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEvent_Contact extends Fragment {


    public FragmentEvent_Contact() {
        // Required empty public constructor
    }

    public static FragmentEvent_Contact newInstance() {
        FragmentEvent_Contact fragment = new FragmentEvent_Contact();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_event__contact, container, false);
    }

}
