package com.example.copd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.copd.Service.MyService;

public class test extends AppCompatActivity {
    private Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        button = (Button)findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.test:
                        showNotification();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void showNotification() {
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);//NotificationManager实例对通知进行管理
        Notification notification=new Notification(R.drawable.logo,"通知",System.currentTimeMillis());//创建Notification对象
//        notification.setLatestEventInfo(this, "通知标题", "通知内容",null);
        //Uri soundUri=Uri.fromFile(new File("/system/media/audio/ringtones/Basic_tone.ogg"));
        //notification.sound=soundUri;
//        notification.ledARGB=Color.GREEN;//控制通知的led灯颜色
//        notification.ledOnMS=1000;//通知灯的显示时间
//        notification.ledOffMS=1000;
//        notification.vibrate = Notification.DEFAULT_VIBRATE;
//        notification.flags=Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1,notification);//调用NotificationManager的notify方法使通知显示
//        break;
    }

}
