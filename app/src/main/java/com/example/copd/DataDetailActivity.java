package com.example.copd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Curers.ChartView;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DataDetailActivity extends AppCompatActivity {

    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Integer> yValue = new ArrayList<>();
    //折线对应的数据
    private Map<String, Integer> value = new HashMap<>();
    private int type = -1;
    private String info;
    private static final int UPDATEVIEW = 1;
    private ChartView chartView;
    private TextView data_tv;
    private String type_name = null;
    private TextView label ;
    private String cell;
    private TextView data_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_detail);
        ActivityController.addActivity(this);
        chartView = (ChartView) findViewById(R.id.data_tv_h);
        data_tv = (TextView)findViewById(R.id.data_tv);
        label = (TextView)findViewById(R.id.label_tv);
        data_view = (TextView)findViewById(R.id.view_data) ;
        Bundle bundle = this.getIntent().getExtras();
        String name = bundle.getString("PageName");
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        /**
         * 设置返回按钮
         */
        appbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(DataDetailActivity.this,"大家好",0).show();
                        finish();
                    }
                }
        );
        /**
         * 设置标题名称
         */
        if (name.equals("breath")) {
            appbar.setTitle("呼吸状况");
            label.setText("呼吸：");
            cell="次/min";
            type = 0;
            type_name ="breath_rate";
        } else if (name.equals("heart")) {
            appbar.setTitle("心率状况");
            label.setText("心率：");
            cell = "次/min";
            type = 1;
            type_name = "heart_rate";
        } else if (name.equals("boold")) {
            appbar.setTitle("血压状况");
            label.setText("血压：");
            cell = "mmHg";
            type = 2;
            type_name = "boold_rate";
        } else if (name.equals("BMI")) {
            appbar.setTitle("BMI状况");
            label.setText("BMI：");
            cell="";
            type = 3;
            type_name = "bmi";
        } else if (name.equals("temperature")) {
            appbar.setTitle("室内温度状况");
            label.setText("室温：");
            cell = "°C";
            type = 4;
            type_name = "temperature";
        } else if (name.equals("relative")) {
            label.setText("湿度：");
            appbar.setTitle("相对湿度状况");
            cell = "%";
            type = 5;
            type_name = "relivate";
        }else if(name.equals("fev1")){
            label.setText("肺功能评估：");
            appbar.setTitle("肺功能评估结果");
            cell = "%";
            type = 6;
            type_name = "fev1";
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
        }, 1000, 60000);
    }



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEVIEW:
                    new Thread(new DataDetailActivity.MyThread()).start();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

        /**
         * 创建线程连接数据库
         */


        public class MyThread implements Runnable{
            @Override
            public void run() {
                System.out.println("username:"+ User.me);
                String path = "http://123.207.20.100:8080"+"/aecopdDB/DataDetailServlet";
                path = path+"?machine_id="+User.bindID+"&type="+type;
                System.out.println(path);
                info = WebService.executeHttpGetLatest(path);
                System.out.println("hello:----"+info);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(info!=null){
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//                            // 获取当前时间
//                            Date date = new Date(System.currentTimeMillis());
//                            data_view.setText(simpleDateFormat.format(date));
                            if(info.equals(false)){
                                data_tv.setText("未检测出");
                            }else{
                                xValue= new ArrayList<>();
                                yValue = new ArrayList<Integer>();
                                value = new ArrayMap<String, Integer>();
                                try {
                                    JSONArray array = new JSONArray(info);
                                    JSONObject object =null;
                                    int  max = 0;
                                    if(type_name.equals("bmi") || type_name.equals("fev1")){
                                        for (int i = 0; i < array.length(); i++) {
                                            object = array.getJSONObject(i);
                                            String dat = object.getString("Dat");
                                            dat = dat.split(" ")[0];
                                            String[] ar = dat.split("-");
                                            dat = ar[1]+"-"+ar[2];
                                            xValue.add(dat);
                                            value.put(dat, object.getInt(type_name));
                                            if (object.getInt(type_name) > max) {
                                                max = object.getInt(type_name);
                                            }
                                        }
                                    }else {
                                        for (int i = 0; i < array.length(); i++) {
                                            object = array.getJSONObject(i);
                                            String dat = object.getString("Dat");
                                            dat = dat.split(" ")[1];
                                            String[] ar = dat.split(":");
                                            dat = ar[0] + ":" + ar[1];
                                            xValue.add(dat);
                                            value.put(dat, object.getInt(type_name));
                                            if (object.getInt(type_name) > max) {
                                                max = object.getInt(type_name);
                                            }
                                        }
                                    }
                                    data_view.setText(object.getString("Dat").substring(0,19));
                                    data_tv.setText(object.getInt(type_name)+cell);
                                    int temp = max/(array.length());
                                    System.out.println(max+":"+temp);
                                    for (int i = 0;i<=(array.length()+2);i++){
                                        yValue.add(i*temp);
                                    }
                                    chartView.setValue(value,xValue,yValue);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
            }
        }

    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
