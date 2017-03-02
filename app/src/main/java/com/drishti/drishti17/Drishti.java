package com.drishti.drishti17;

import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by droidcafe on 2/12/2017
 */
public class Drishti extends SugarApp{

    private  Context sContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }



}
