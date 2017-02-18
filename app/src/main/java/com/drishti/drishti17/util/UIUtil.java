package com.drishti.drishti17.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.drishti.drishti17.R;

/**
 * Created by droidcafe on 2/9/2017. */

public class UIUtil {


    public static void setToolBar(AppCompatActivity activity, String titleText) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        title.setText(titleText);
    }

    public static void printHTML(TextView textView, String htmlText) {
        textView.setText(parseHTML(htmlText));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Spanned parseHTML(String htmlText) {
        if(Import.isVersionOK(Build.VERSION_CODES.N)){
           return Html.fromHtml(htmlText,Html.FROM_HTML_MODE_COMPACT);
        }else {
            return Html.fromHtml(htmlText);

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int iconId) {
        Drawable drawable;
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)){
            drawable = context.getDrawable(iconId);
        }else {
            drawable = context.getResources().getDrawable(iconId);
        }
        return drawable;
    }
}
