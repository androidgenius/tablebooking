package com.quandoo.tablebooking.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by gaurav on 30/08/16.
 */

/**
 * Application class
 */
public class QuandooApplication extends Application {

    private static QuandooApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * Simply return the application context
     *
     * @return Context
     */
    public static synchronized Context getAppContext() {
        return instance.getApplicationContext();
    }
}

