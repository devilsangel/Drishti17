package com.drishti.drishti17.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.drishti.drishti17.R;
import com.drishti.drishti17.ui.Events;
import com.drishti.drishti17.ui.Expo;
import com.drishti.drishti17.ui.Home;
import com.drishti.drishti17.ui.NavigationActivity;
import com.drishti.drishti17.ui.Schedule;
import com.drishti.drishti17.ui.transition.FabTransform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Util class for Navigation Activity
 */

public class NavUtil {
    private static final int RC_NAVIGATION_ITEM_CLICKED = 21;
    private static final Map<Integer, Class> positionClassMap;

    private static final String TAG = NavUtil.class.getSimpleName();

    static {
        Map<Integer, Class> tempMap = new HashMap<>();
        tempMap.put(1, Schedule.class);
        tempMap.put(2, Home.class);
        tempMap.put(3, Events.class);
        tempMap.put(4, Home.class);
        tempMap.put(5, Expo.class);
        tempMap.put(6, Home.class);
        tempMap.put(7, Home.class);

        positionClassMap = Collections.unmodifiableMap(tempMap);

    }

    public static void openNavigation(Activity activity, Context context,
                                      View fab) {
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            startFabTransition(activity, context, fab);
        } else {
            activity.startActivityForResult(new Intent(context, NavigationActivity.class), RC_NAVIGATION_ITEM_CLICKED);
        }
    }


    @SuppressLint("NewApi")
    public static void returnParent(Activity activity, int activitySelected) {

        activity.setResult(activitySelected);
        if (Import.isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            activity.finishAfterTransition();
        } else {
            activity.finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void startFabTransition(Activity activity, Context context, View fab) {
        Intent intent = new Intent(context, NavigationActivity.class);
        FabTransform.addExtras(intent,
                ContextCompat.getColor(context, R.color.colorPrimary_nav), R.drawable.ic_menu_white_24dp);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, fab,
                context.getString(R.string.transition_navigation_icon));
        activity.startActivityForResult(intent, RC_NAVIGATION_ITEM_CLICKED, options.toBundle());
    }


    public static boolean isFromNav(int requestCode) {
        return requestCode == RC_NAVIGATION_ITEM_CLICKED;
    }

    public static void handleNavigation(Activity activity, Context context,
                                        int resultCode) {

        Log.d(TAG, "handleNavigation: result "+resultCode);
        if (resultCode == -1) {
            Log.d(TAG, "handleNavigation: cancelled action");
            return;
        }

        Class intentClass = positionClassMap.get(resultCode);
        if (activity.getClass() == intentClass) {
            Log.d(TAG, "handleNavigation: same class");
            return;
        }
        if (intentClass != null)
            activity.startActivity(new Intent(context, intentClass));
    }
}
