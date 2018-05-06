package com.example.copd;

import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.copd.Curers.ChartView;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BooldDetailActivity extends AppCompatActivity {
    private TextView boold;
    private ChartView boold_up;
    private ChartView boold_down;
    private String info;
    private TextView time;
    private static final int UPDATEVIEW = 1;
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
//    private List<String> x1Value = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Integer> yValue = new ArrayList<>();
    private List<Integer> y1Value = new ArrayList<>();
    //折线对应的数据
    private Map<String, Integer> value = new HashMap<>();
    private Map<String ,Integer>value1 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boold_detail);
        boold = (TextView)findViewById(R.id.boold_detail_tv);
        boold_up=(ChartView)findViewById(R.id.boold_data_up);
        boold_down=(ChartView)findViewById(R.id.boold_data_down);
        time = (TextView)findViewById(R.id.boold_time_tv);
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        appbar.setTitle("血压状况");
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
                    new Thread(new BooldDetailActivity.MyThread()).start();
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
            path = path+"?machine_id="+User.bindID+"&type="+2;
            info = WebService.executeHttpGetLatest(path);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("info:"+info);
                    if(info!=null) {
                        if (info.equals(false)) {
                            boold.setText("未检测出");
                        } else {

                            xValue = new ArrayList<>();
//                            x1Value = new ArrayList<String>();
                            yValue = new ArrayList<Integer>();
                            y1Value = new ArrayList<Integer>();
                            value = new ArrayMap<String, Integer>();
                            value1 = new ArrayMap<String, Integer>();
                            try {
                                JSONArray array = new JSONArray(info);
                                JSONObject object = null;
                                int max = 0;
                                int min = 0;
                                String type_name = "boold_rate_up";
                                String typename = "boold_rate_down";
                                for (int i = 0; i < array.length(); i++) {
                                    object = array.getJSONObject(i);
                                    String dat = object.getString("Dat");
                                    dat = dat.split(" ")[1];
                                    String[] ar = dat.split(":");
                                    dat = ar[0] + ":" + ar[1];
                                    xValue.add(dat);

                                    value.put(dat, object.getInt(type_name));
                                    value1.put(dat,object.getInt(typename));
                                    if (object.getInt(type_name) > max) {
                                        max = object.getInt(type_name);
                                    }

                                    if(object.getInt(typename)>min){
                                        min = object.getInt(typename);
                                    }
                                }
                                time.setText(object.getString("Dat").substring(0,19));
                                boold.setText(object.getInt(type_name) +"/"+object.getInt(typename) + "mmHg");
                                int temp = max / (array.length());
                                int temp2= min/(array.length());
                                System.out.println(max + ":" + temp);
                                for (int i = 0; i <= (array.length() + 2); i++) {
                                    yValue.add(i * temp);
                                }
                                for(int i = 0;i<=(array.length()+2);i++){
                                    y1Value.add(i*temp2);
                                }
                                boold_up.setValue(value, xValue, yValue);
                                boold_down.setValue(value1,xValue,y1Value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }
}
