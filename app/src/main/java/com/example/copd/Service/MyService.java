package com.example.copd.Service;

/**
 * Created by asus on 2018/4/24.
 */

import java.util.Date;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import android.util.Log;

import com.example.copd.HealthCenterActivity;
import com.example.copd.MainActivity;
import com.example.copd.Model.Latest;
import com.example.copd.Model.User;
import com.example.copd.R;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;


public class MyService extends IntentService{
    private String info;
    private PowerManager.WakeLock wakeLock = null;
    private Handler handler = new Handler();
    private String level ;
    public MyService(){
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        System.out.println("Start executed");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {

                        if (Latest.level == null){
                            new Thread(new MyThread()).start();
                        }else{
                            level = Latest.level;
                            if(!level.equals("优")) {
                                showNotification();
                            }
                        }
                        Thread.sleep(1000*60);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }
    public void showNotification(){
        if(!level.equals("0")){
//            Intent intent = new Intent(getApplicationContext(), MyService.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(MyService.this, 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(MyService.this)
                    .setTicker("急性加重")
                    .setSmallIcon(R.drawable.logo)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("急性加重风险等级")
                    .setContentText(level);
            Intent appIntent = new Intent(MyService.this,MainActivity.class);
            appIntent.setAction(Intent.ACTION_MAIN);
            appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );
            PendingIntent contentIntent = PendingIntent.getActivity(MyService.this,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notice;
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            long[] vibrates = {0,2000,1000,2000,1000,2000};
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN){
//                PendingIntent content;
                notice = builder.setContentIntent(contentIntent)
                        .build();
                AudioManager audio = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
                switch (audio.getRingerMode()){
                    case AudioManager.RINGER_MODE_SILENT:
                        System.out.println("1");
                        notice.sound = null;
                        notice.defaults |= Notification.DEFAULT_VIBRATE;
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        System.out.println("2");
//                        notice.sound = Notification.DEFAULT_SOUND;
//                        notice.defaults = Notification.DEFAULT_ALL;
                        notice.sound = uri;
//                        notice.defaults +=Notification.DEFAULT_VIBRATE;
                        notice.vibrate =vibrates;
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        System.out.println("3");
                        notice.sound = null;
//                        notice.defaults |= Notification.DEFAULT_VIBRATE;
                        notice.vibrate = vibrates;
                        break;
                    default:
                        break;
                }
//                notice.defaults = Notification.DEFAULT_ALL;
                notice.ledARGB = Color.GREEN;
                notice.ledOnMS = 1000;
                notice.ledOffMS = 1000;
                notice.flags = Notification.FLAG_SHOW_LIGHTS ;
//                notice.flags =Notification.FLAG_AUTO_CANCEL;
                manager.notify(1,notice);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MyService.class.getName());
        wakeLock.acquire();
    }

    @Override
    public void onDestroy() {

//        System.out.println("Destory");
        if(wakeLock!=null){
            wakeLock.release();
            wakeLock = null;
        }
        super.onDestroy();
    }


    public class MyThread implements Runnable{
        @Override
        public void run() {
            System.out.println("username:"+ User.me);
            String path = "http://123.207.20.100:8080"+"/aecopdDB/latestServlet";
            path = path+"?machine_id="+User.bindID+"&username="+User.me;
            System.out.println(path);
            info = WebService.executeHttpGetLatest(path);
            System.out.println("hello:----"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    dialog.dismiss();
                    if(info!=null){
                        try {
                            JSONObject jsonObj = new JSONObject(info);

                            int risk = jsonObj.getInt("level");
//                            if(Integer.valueOf(jsonObj.getString("breath_rate"))>30){
//                                risk +=1;
//                            }
                            int breat = Integer.valueOf(jsonObj.getString("breath_rate"));
                            if(breat>=30){
                                risk = risk+1;
                            }
                            System.out.println("come here now");
                            switch (risk){

                                case 0:
                                    level="优";
                                    break;
                                case 1:
                                    level = "低";
                                    break;
                                case 2:
                                    level = "中";
                                    break;
                                default:
                                    level = "高";
                                    break;
                            }
                            if(!level.equals("优")){
                                Log.d("isn't not ","优");
                                showNotification();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}