package com.drishti.drishti17.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.drishti.drishti17.R;

/**
 * Created by droidcafe on 2/9/2017.
 */

public class UIUtil {

    ;
    public static void setToolBar(AppCompatActivity activity, String titleText) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        title.setText(titleText);
    }
}
