package com.drishti.drishti17.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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


    public static int getBackgroundImage(Context context,String imageName) {
        String uri = "drawable/" + imageName;
        return context.getResources().getIdentifier(uri, null, context.getPackageName());
    }

    public static boolean isVersionOK(int base_version) {
        return (Build.VERSION.SDK_INT >= base_version);
    }


    public static boolean isEventDownloading() {
        return Global.isDownloading;
    }

    public static void setEventDownloadingStatus(boolean isDownloading) {
        Global.isDownloading = isDownloading;

    }

    public static String daytoDate(String day) {
        if (day == null) return null;
        switch (day){
            case "1":
                return "19/3/17";
            case "2":
                return "20/3/17";
            case "3":
                return "21/3/17";
        }
        return null;
    }

    /**
     * helper function for composing mail
     */
    public static void composeEmail(Activity activity, String[] addresses, String subject){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * launch helper intent to start default dialer app
     */
    public static void callIntent(Activity activity, String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNo));
        activity.startActivity(intent);
    }

}
