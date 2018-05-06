package com.example.copd;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by asus on 2018/3/26.
 */

public class ContextUtil extends Application {
    private static Context context;

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }
}
