package com.girmiti.mobilepos.util;

import android.app.Application;
import android.content.Context;

public class FacadeApplication extends Application {

    private static volatile Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Context contxt=getApplicationContext();
        initilizeApplicationContext(contxt);
     }

    private static void initilizeApplicationContext(Context context) {
        FacadeApplication.context = context;
    }

    public static Context getAppContext() {
        return FacadeApplication.context;
    }
}
