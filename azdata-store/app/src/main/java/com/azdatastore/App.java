package com.azdatastore;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class App extends Application {

    private static final String TAG = "==App==";


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    // application on create methode for the create and int base values
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MultiDex.install(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
