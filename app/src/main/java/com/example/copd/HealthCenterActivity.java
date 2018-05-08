package com.example.copd;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.copd.Model.Latest;
import com.example.copd.Model.Normal;
import com.example.copd.Model.User;
import com.example.copd.Service.MyService;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HealthCenterActivity extends Fragment {
    private String info=null;
    private TextView level;
    private TextView current_time;
    private TextView heart;
    private TextView boold;
    private TextView breath;
    private TextView BMI;
    private TextView cough;
    private TextView temperature;
    private TextView relivate;
    private TextView fev1;
    private ImageButton question;
    private static final int UPDATEVIEW = 1;
    private AlertDialog.Builder dialog;
    private ImageView fev1_img;
    private ImageView breath_img;
    private ImageView heart_img;
    private ImageView bmi_img;
    private ImageView cough_img;
    private ImageView temperature_img;
    private ImageView relivate_img;
    private Context mContext;
//    NotificationManager notificationManager;
//    Notification.Builder builder ;
//    Notification notification;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment1 = inflater.inflate(R.layout.health_center_fragment, null);
        mContext = getActivity();
        return fragment1;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        fev1_img = (ImageView)getActivity().findViewById(R.id.fev1_img) ;
        breath_img = (ImageView)getActivity().findViewById(R.id.breath_img);
        heart_img = (ImageView)getActivity().findViewById(R.id.heart_img);
        bmi_img = (ImageView)getActivity().findViewById(R.id.BMI_img);
        cough_img = (ImageView)getActivity().findViewById(R.id.cough_img);
        temperature_img = (ImageView)getActivity().findViewById(R.id.img_temperature);
        relivate_img = (ImageView)getActivity().findViewById(R.id.img_relivate);
        question = (ImageButton)getActivity().findViewById(R.id.question);
        level = (TextView)getActivity().findViewById(R.id.risk_rate_tv) ;
        if(Latest.level == null) {
            level.setText("检测中");
        }else{
            level.setText(Latest.level);
            if(Latest.level.equals("优")){
                level.setTextColor(Color.BLUE);
            }else if(Latest.level.equals("低")){
                level.setTextColor(Color.YELLOW);
            }else if(Latest.level.equals("中")){
                level.setTextColor(getResources().getColor(R.color.low));
            }else {
                level.setTextColor(Color.RED);
            }
        }
        current_time = (TextView)getActivity().findViewById(R.id.update_time);
        if(Latest.current_time ==null) {
            current_time.setText("检测中");
        }else{
            current_time.setText(Latest.current_time);
        }


        fev1 = (TextView)getActivity().findViewById(R.id.fev1_tv);
        if(Latest.fev1 == null) {
            fev1.setText("检测中");
        }else {
            fev1.setText(Latest.fev1+"%");
            System.out.println("latest fev1: "+Latest.fev1+" Normal fev1: "+Normal.fev1);
            checkFev1();
        }

        heart = (TextView)getActivity().findViewById(R.id.heart_rate_tv);
        if(Latest.heart_rate == null) {
            heart.setText("检测中");
        }else {
            heart.setText(Latest.heart_rate+"次/min");
            checkHeartRate();
        }

        boold =(TextView)getActivity().findViewById(R.id.boold_pressure_tv);
        if(Latest.boold_rate_up == null) {
            boold.setText("检测中");
        }else{
            boold.setText(Latest.boold_rate_up);
        }

        breath = (TextView)getActivity().findViewById(R.id.breath_rate_tv);
        if(Latest.breath_rate == null) {
            breath.setText("检测中");
        }else{
            breath.setText(Latest.breath_rate+"次/min");
            checkBreathRate();
        }
        BMI = (TextView)getActivity().findViewById(R.id.BMI_tv);
        if(Latest.BMI == null) {
            BMI.setText("检测中");
        }else {
            BMI.setText(Latest.BMI);
            checkBMI();
        }
        cough = (TextView)getActivity().findViewById(R.id.cough_level_tv);
        if (Latest.cough_level == null) {
            cough.setText("检测中");
        }else{
            if(Latest.cough_level.equals("1")){
                cough.setText("加重");
                cough_img.setImageResource(R.mipmap.up);
            }else {
                cough.setText("正常");
                cough_img.setImageResource(R.mipmap.normal);
            }
        }

        temperature = (TextView)getActivity().findViewById(R.id.temperature_tv);
        if(Latest.tempurature == null) {
            temperature.setText("检测中");
        }else{
            temperature.setText(Latest.tempurature+"°C");
            checkTemperature();
        }
        relivate = (TextView)getActivity().findViewById(R.id.relative_tv);
        if(Latest.relivate == null) {
            relivate.setText("检测中");
        }else{
            relivate.setText(Latest.relivate+"%");
            checkRelivate();
        }
    }

    private void checkRelivate() {
        if(Integer.valueOf(Latest.relivate) < 50){
            relivate_img.setImageResource(R.mipmap.down);
        }else if(Integer.valueOf(Latest.relivate) > 60){
            relivate_img.setImageResource(R.mipmap.up);
        }else{
            relivate_img.setImageResource(R.mipmap.normal);
        }
    }

    private void checkTemperature() {
        if(Integer.valueOf(Latest.tempurature) > 25){
            temperature_img.setImageResource(R.mipmap.up);
        }else  if(Integer.valueOf(Latest.tempurature) < 18){
            temperature_img.setImageResource(R.mipmap.down);
        }else{
            temperature_img.setImageResource(R.mipmap.normal);

        }
    }

    /**
     * check BMI
     */
    private void checkBMI() {
        if(Float.valueOf(Latest.BMI) >23.9){
            bmi_img.setImageResource(R.mipmap.up);
        }else  if (Float.valueOf(Latest.BMI) < 18.5){
            bmi_img.setImageResource(R.mipmap.down);
        }else{
            bmi_img.setImageResource(R.mipmap.normal);
        }
    }

    /**
     * checkBreathRate
     */
    private void checkBreathRate() {
        if (Integer.valueOf(Latest.breath_rate) > 30) {
            breath_img.setImageResource(R.mipmap.up);
        } else if (Integer.valueOf(Latest.breath_rate) < 16) {
            breath_img.setImageResource(R.mipmap.down);
        }else {
            breath_img.setImageResource(R.mipmap.normal);
        }
    }

    /**
     * check Heart Rate
     */

    private void checkHeartRate() {
        if(Integer.valueOf(Latest.heart_rate) > 109){
            heart_img.setImageResource(R.mipmap.up);
        }else  if (Integer.valueOf(Latest.heart_rate) < 60){
            heart_img.setImageResource(R.mipmap.down);
        }else{
            heart_img.setImageResource(R.mipmap.normal);
        }
    }

    /**
     * checkFev1
     */

    private void checkFev1() {
        if(Float.valueOf(Latest.fev1)>Float.valueOf(Normal.fev1)){
            fev1_img.setImageResource(R.mipmap.up);
        }else if(Float.valueOf(Latest.fev1)<Float.valueOf(Normal.fev1)){
            fev1_img.setImageResource(R.mipmap.down);
        }else{
            fev1_img.setImageResource(R.mipmap.normal);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEVIEW:
                    new Thread(new MyThread()).start();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("急性加重风险等级说明：");
                    dialog.setMessage("优：患者情况良好\n低：患者可能需要门诊治疗\n中：患者可能需要住院治疗\n高：患者可能会进入ICU治疗");
                    dialog.setCancelable(true);
                    dialog.show();
                }
            });
        }
    };

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
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//                            // 获取当前时间
//                            Date date = new Date(System.currentTimeMillis());
                            String time = jsonObj.getString("Dat");

//                            String temp = t.toString();
//                            System.out.println("temp:"+temp);
                            String[] te = time.split(" ");
//                            System.out.println(te[1]);
                            String tmp = te[1].substring(0, 8);
//                            System.out.println(tmp);
                            current_time.setText(te[0]+" "+tmp);
                            Latest.setCurrent_time(current_time.getText().toString());

                            int risk = jsonObj.getInt("level");
//                            if(Integer.valueOf(jsonObj.getString("breath_rate"))>30){
//                                risk +=1;
//                            }
                            int breat = Integer.valueOf(jsonObj.getString("breath_rate"));
                            if(breat>=30){
                                risk = risk+1;
                            }
                            switch (risk){
                                case 0:
                                    level.setText("优");
//                                    Latest.setLevel("优");
                                    level.setTextColor(Color.BLUE);
                                    break;
                                case 1:
                                    level.setText("低");
//                                    Latest.setLevel("低");
                                    level.setTextColor(Color.YELLOW);
                                    break;
                                case 2:
                                    level.setText("中");
//                                    Latest.setLevel("中");
                                    level.setTextColor(mContext.getResources().getColor(R.color.low));
                                    break;
                                default:
                                    level.setText("高");
//                                    Latest.setLevel("高");
                                    level.setTextColor(Color.RED);
                                    break;
                            }
                            Latest.setLevel(level.getText().toString());
                            heart.setText(jsonObj.getString("heart_rate")+"次/min");
                            Latest.setHeart_rate(jsonObj.getString("heart_rate"));

                            boold.setText(jsonObj.getString("boold_rate_up")+"/"+jsonObj.getString("boold_rate_down")+" mmHg");
                            Latest.setBoold_rate_up(boold.getText().toString());

                            breath.setText(jsonObj.getString("breath_rate")+"次/min");
                            Latest.setBreath_rate(jsonObj.getString("breath_rate"));

                            BMI.setText(jsonObj.getString("BMI"));
                            Latest.setBMI(jsonObj.getString("BMI"));

                            fev1.setText(jsonObj.getString("fev1")+"%");
                            Latest.setFev1(jsonObj.getString("fev1"));

                            int level = jsonObj.getInt("cough_level");
                            if(level == 0){
                                cough.setText("正常");
                                cough_img.setImageResource(R.mipmap.normal);
                            }else{
                                cough.setText("加重");
                                cough_img.setImageResource(R.mipmap.up);
                            }
                            Latest.setCough_level(level+"");
                            checkFev1();
                            checkBMI();
                            checkBreathRate();
                            checkHeartRate();

                            temperature.setText(jsonObj.getString("temperature")+"°C");
                            Latest.setTempurature(jsonObj.getString("temperature"));

                            relivate.setText(jsonObj.getString("relivate")+"%");
                            Latest.setRelivate(jsonObj.getString("relivate"));
                            checkTemperature();
                            checkRelivate();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(info);
                    }
                }
            });
        }
    }
    //定时刷新，一分钟刷新一次
    public void onStart() {
        super.onStart();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Message message = new Message();
                    message.what = UPDATEVIEW;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000, 60000);
    }

}
