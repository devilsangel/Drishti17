package com.drishti.drishti17;

import android.app.Application;
import android.content.Context;

/**
 * Created by nirmal on 2/12/2017
 */
public class Drishti extends Application{

    private  Context sContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

       // Stetho.initializeWithDefaults(this);
        sContext = getApplicationContext();
        
    }



}
