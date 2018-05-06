package com.example.copd.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.copd.MainActivity;

/**
 * Created by asus on 2018/4/24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
            Intent i= new Intent(Intent.ACTION_RUN);
            i.setClass(context, MyService.class);
            context.startService(i);
        }
    }
}