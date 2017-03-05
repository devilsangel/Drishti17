package com.drishti.drishti17.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.drishti.drishti17.BuildConfig;
import com.drishti.drishti17.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * Created by nirmal on 2/2/2017.
 */

public class Import {

    private static final String TAG = Import.class.getSimpleName();

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


    public static boolean isEventDownloading() {
        return Global.isDownloading;
    }
    public static boolean isHighlightDownloading() {
        return Global.isHighlightDownloading;
    }

    public static void setEventDownloadingStatus(boolean isDownloading) {
        Global.isDownloading = isDownloading;

    }

    public static void setHighlightDownloadingStatus(boolean isDownloading) {
        Global.isHighlightDownloading = isDownloading;

    }

    public static String daytoDate(String day) {
        if (day == null) return null;
        switch (day) {
            case "17":
                return "17/3/17";
            case "18":
                return "18/3/17";
            case "19":
                return "19/3/17";
        }
        return null;
    }

    /**
     * helper function for composing mail
     */
    public static void composeEmail(Activity activity, String[] addresses, String subject) {
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

    public static String getStringSharedPerf(Context context, String sharedKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        return sharedPreferences.getString(sharedKey, null);
    }

    public static boolean getBooleanSharedPref(Context context, String sharedKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        return sharedPreferences.getBoolean(sharedKey, false);
    }

    public static int getIntSharedPref(Context context, String sharedKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        return sharedPreferences.getInt(sharedKey, -1);
    }

    public static void setSharedPref(Context context, String sharedKey, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedKey, value);
        editor.apply();
    }

    public static void setSharedPref(Context context, String sharedKey, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedKey, value);
        editor.apply();
    }

    public static void setSharedPref(Context context, String sharedKey, int value) {
        Log.d(TAG, "setSharedPref: setting int "+value);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Global.SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sharedKey, value);
        editor.apply();
    }

    public static void fetchRemoteConfig(final Context context, Activity activity) {

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettings(configSettings);

        remoteConfig.setDefaults(R.xml.remote_config_default_map);

        long cacheExpiration = 720;
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        Log.d(TAG, "fetchRemoteConfig: cache expiration " + cacheExpiration);
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: before activating " + remoteConfig.getString("event_current_version"));
                    remoteConfig.activateFetched();
                    Log.d(TAG, "onComplete: after activating " + remoteConfig.getString("event_current_version"));
                    onConfigActivated(context);
                }
            }
        });
    }

    public static void onConfigActivated(Context context) {
        Log.d(TAG, "onConfigActivated: config activated");
    }

    public static boolean checkShowPrompt(Context context, String promptKey) {
        Log.d(TAG, "checkShowPrompt: "+getBooleanSharedPref(context, promptKey));
        return !getBooleanSharedPref(context, promptKey);
    }

    public static void setPromptShown(Context context, String promptKey) {
        setSharedPref(context, promptKey, true);
    }

    public static int getColorId(Context context, String colorId) {
        String uri = "color/" + colorId;
        return context.getResources().getIdentifier(uri, null, context.getPackageName());
    }
}
