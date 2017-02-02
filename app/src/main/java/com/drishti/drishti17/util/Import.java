package com.drishti.drishti17.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by droidcafe on 2/2/2017.
 */

public class Import {

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void snack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void settypefaces(Context context, String typefaceName, TextView textview) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
        textview.setTypeface(typeface);

    }

    @SuppressLint("NewApi")
    public static void setStatusBarColor(Context context, Activity activity, int color) {
        Window window = activity.getWindow();

        if (isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(color));

        }
    }


    public static boolean isVersionOK(int base_version) {
        return (Build.VERSION.SDK_INT >= base_version);
    }


}
