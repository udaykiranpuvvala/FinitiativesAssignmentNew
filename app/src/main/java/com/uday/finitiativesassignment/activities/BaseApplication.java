package com.uday.finitiativesassignment.activities;

import android.app.Application;
import android.content.Context;

import com.uday.finitiativesassignment.entities.CoinsEntity;

import java.util.ArrayList;

public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static Context context;

    private static BaseApplication mInstance;
    public static final ArrayList<CoinsEntity> coinsModelArrayList = new ArrayList<>();

    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


}
