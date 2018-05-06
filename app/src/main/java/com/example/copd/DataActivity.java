package com.example.copd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Model.Latest;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DataActivity extends Fragment {
    private String info=null;
//    private TextView current_time;
    private TextView heart;
    private TextView boold;
    private TextView breath;
    private TextView BMI;
    private TextView cough;
    private TextView temperature;
    private TextView relivate;
    private TextView fev1;
    private final int REQUESTCODE = 2;

    private static final int UPDATEVIEW = 1;

//    private static Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment1 = inflater.inflate(R.layout.activity_data, null);
        new Thread(new DataActivity.MyThread()).start();
        return fragment1;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        current_time = (TextView)getActivity().findViewById(R.id.update_time);
//        current_time.setText("检测中");
        fev1 = (TextView)getActivity().findViewById(R.id.fev1_data_tv) ;
        if (Latest.fev1 == null) {
            fev1.setText("检测中");
        }else {
            fev1.setText(Latest.fev1+"%");
        }
        heart = (TextView)getActivity().findViewById(R.id.heart_data_tv);
        if(Latest.heart_rate == null) {
            heart.setText("检测中");
        }else {
            heart.setText(Latest.heart_rate+"次/min");
        }
        boold =(TextView)getActivity().findViewById(R.id.boold_data_tv);
        if (Latest.boold_rate_up == null) {
            boold.setText("检测中");
        }else {
            boold.setText(Latest.boold_rate_up);
        }
        breath = (TextView)getActivity().findViewById(R.id.breath_date_tv);
        if (Latest.breath_rate == null) {
            breath.setText("检测中");
        }else {
            breath.setText(Latest.breath_rate+"次/min");
        }
        BMI = (TextView)getActivity().findViewById(R.id.BMI_data_tv);
        if (Latest.BMI == null) {
            BMI.setText("检测中");
        }else{
            BMI.setText(Latest.BMI);
        }
        cough = (TextView)getActivity().findViewById(R.id.cough_data_tv);
        if (Latest.cough_level == null) {
            cough.setText("检测中");
        }else{
            if(Latest.cough_level.equals("1")){
                cough.setText("加重");
            }else {
                cough.setText("正常");
            }
        }

        temperature = (TextView)getActivity().findViewById(R.id.temperature_data_tv);
        if(Latest.tempurature == null) {
            temperature.setText("检测中");
        }else{
            temperature.setText(Latest.tempurature+"°C");
        }

        relivate = (TextView)getActivity().findViewById(R.id.relative_data_tv);
        if(Latest.relivate == null) {
            relivate.setText("检测中");
        }else{
            relivate.setText(Latest.relivate+"%");
        }
        relivate.setText("检测中");

        final LinearLayout breath_button = (LinearLayout) getActivity().findViewById(R.id.breath_rate_data);
        LinearLayout heart_button = (LinearLayout) getActivity().findViewById(R.id.heart_rate_data);
        LinearLayout boold_button = (LinearLayout)getActivity().findViewById(R.id.boold_rate_data) ;
        LinearLayout BMI_button = (LinearLayout)getActivity().findViewById(R.id.BMI_data);
        LinearLayout fev1_button = (LinearLayout)getActivity().findViewById(R.id.fev1_rate_data);
        LinearLayout temperature_button = (LinearLayout)getActivity().findViewById(R.id.temperature_data) ;
        LinearLayout relative_button = (LinearLayout)getActivity().findViewById(R.id.relative_data) ;
        LinearLayout cough_button = (LinearLayout)getActivity().findViewById(R.id.cough_data);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.breath_rate_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","breath");
                        startActivity(intent);
                        break;
                    case R.id.heart_rate_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","heart");
                        startActivity(intent);
                        break;
                    case R.id.boold_rate_data:
                        intent.setClass(getActivity(),BooldDetailActivity.class);
//                        intent.putExtra("PageName","boold");
                        startActivity(intent);
                        break;
                    case R.id.BMI_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","BMI");
                        startActivity(intent);
                        break;
                    case R.id.temperature_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","temperature");
                        startActivity(intent);
                        break;
                    case R.id.relative_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","relative");
                        startActivity(intent);
                        break;
                    case R.id.fev1_rate_data:
                        intent.setClass(getActivity(),DataDetailActivity.class);
                        intent.putExtra("PageName","fev1");
                        startActivity(intent);
                        break;
                    case R.id.cough_data:
                        intent.setClass(getActivity(),Change_Desease_HistoryActivity.class);
                        startActivityForResult(intent,REQUESTCODE);
                    default:
                        break;
                }
            }
        };

        breath_button.setOnClickListener(clickListener);
        heart_button.setOnClickListener(clickListener);
        boold_button.setOnClickListener(clickListener);
        BMI_button.setOnClickListener(clickListener);
        fev1_button.setOnClickListener(clickListener);
        temperature_button.setOnClickListener(clickListener);
        relative_button.setOnClickListener(clickListener);
        cough_button.setOnClickListener(clickListener);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEVIEW:
                    new Thread(new DataActivity.MyThread()).start();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
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
                             //获取当前时间
//                            Date date = new Date(System.currentTimeMillis());
//                            current_time.setText(simpleDateFormat.format(date));

                            String time = jsonObj.getString("Dat");
//                            String temp = t.toString();
//                            System.out.println("temp:"+temp);
                            String[] te = time.split(" ");
//                            System.out.println(te[1]);
                            String tmp = te[1].substring(0, 8);
                            System.out.println(tmp);
//                            String [] t = time.split(".");
//                            current_time.setText(time);
                            Latest.setCurrent_time(te[0]+" "+ tmp);
                            int risk = jsonObj.getInt("level");
                            if(Integer.valueOf(jsonObj.getString("breath_rate")) > 30){
                                risk +=1;
                            }
                            switch (risk){
                                case 0:
//                                    level.setText("优");
                                    Latest.setLevel("优");
//                                    level.setTextColor(Color.BLUE);
                                    break;
                                case 1:
//                                    level.setText("低");
                                    Latest.setLevel("低");
//                                    level.setTextColor(Color.YELLOW);
                                    break;
                                case 2:
//                                    level.setText("中");
                                    Latest.setLevel("中");
//                                    level.setTextColor(getResources().getColor(R.color.low));
                                    break;
                                default:
//                                    level.setText("高");
                                    Latest.setLevel("高");
//                                    level.setTextColor(Color.RED);
                                    break;
                            }

                            heart.setText(jsonObj.getString("heart_rate")+"次/min");
                            Latest.setHeart_rate(jsonObj.getString("heart_rate"));

                            boold.setText(jsonObj.getString("boold_rate_up")+"/"+jsonObj.getString("boold_rate_down")+" mmHg");
                            Latest.setBoold_rate_up(boold.getText().toString());

                            breath.setText(jsonObj.getString("breath_rate")+"次/min");
                            Latest.setBreath_rate(jsonObj.getString("breath_rate"));

                            BMI.setText(jsonObj.getString("BMI"));
                            Latest.setBMI(BMI.getText().toString());

                            fev1.setText(jsonObj.getString("fev1")+"%");
                            Latest.setFev1(jsonObj.getString("fev1"));

                            int level = jsonObj.getInt("cough_level");
                            if(level == 0){
                                cough.setText("正常");
                            }else{
                                cough.setText("加重");
                            }
                            Latest.setCough_level(level+"");

                            temperature.setText(jsonObj.getString("temperature")+"°C");
                            Latest.setTempurature(jsonObj.getString("temperature"));

                            relivate.setText(jsonObj.getString("relivate")+"%");
                            Latest.setRelivate(jsonObj.getString("relivate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(info);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 7:
                if (requestCode == 2){
                    String result = data.getStringExtra("result");
                    String r ="";
                    if(r.equals("1")){
                        r = "加重";
                    }else{
                        r="正常";
                    }
                    cough.setText(r);
                    Latest.cough_level = r;
                    Toast.makeText(getContext(),"修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

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
