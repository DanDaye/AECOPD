package com.example.copd;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Model.Normal;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import lecho.lib.hellocharts.model.Line;

public class NormalActivity extends AppCompatActivity {
    private LinearLayout fev1_layout ;
    private TextView fev1;
    private LinearLayout heartRate_layout;
    private TextView heartRate;
    private  LinearLayout breathRate_layout;
    private  TextView breathRate;
    private  LinearLayout booldRate_layout;
    private  TextView booldRate;
    private LinearLayout bmi_layout;
    private final int REQUESTCODE = 1;
    private String resulttext = "";
    private String type = "";
    private  TextView bmi;
    private  String info = null;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ActivityController.addActivity(NormalActivity.this);
        /**
         * 初始化界面
         */
        fev1_layout = (LinearLayout)findViewById(R.id.fev1_normal_layout);
        fev1 = (TextView)findViewById(R.id.fev1_normal);
        heartRate_layout = (LinearLayout)findViewById(R.id.heartRate_normal_layout);
        heartRate = (TextView)findViewById(R.id.heartRate_normal);
        breathRate_layout = (LinearLayout)findViewById(R.id.breathRate_normal_layout);
        breathRate = (TextView)findViewById(R.id.breathRate_normal);
        booldRate_layout = (LinearLayout)findViewById(R.id.booldRate_normal_layout);
        booldRate = (TextView)findViewById(R.id.booldRate_normal);
        bmi_layout = (LinearLayout)findViewById(R.id.bmi_normal_layout);
        bmi = (TextView)findViewById(R.id.bmi_normal);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("稳定期详情");
        new Thread(new NormalActivity.MyThread()).start();
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(DataDetailActivity.this,"大家好",0).show();
                        finish();
                    }
                }
        );

        /**
         * 点击按键触发功能
         */
        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.fev1_normal_layout:
                        Intent intent = new Intent();
                        intent.setClass(NormalActivity.this,ModifyActivity.class);
                        intent.putExtra("PageName","fev1");
                        intent.putExtra("result",resulttext);
                        intent.putExtra("type",type);
                        startActivityForResult(intent,REQUESTCODE);
                        break;
                    case R.id.heartRate_normal_layout:
                        Intent intent1 = new Intent();
                        intent1.setClass(NormalActivity.this,ModifyActivity.class);
                        intent1.putExtra("PageName","heart");
                        intent1.putExtra("result",resulttext);
                        intent1.putExtra("type",type);
                        startActivityForResult(intent1,REQUESTCODE);
                        break;
                    case R.id.breathRate_normal_layout:
                        Intent intent2 = new Intent();
                        intent2.setClass(NormalActivity.this,ModifyActivity.class);
                        intent2.putExtra("PageName","breath");
                        intent2.putExtra("result",resulttext);
                        intent2.putExtra("type",type);
                        startActivityForResult(intent2,REQUESTCODE);
                        break;
                    case R.id.booldRate_normal_layout:
                        Intent intent3 = new Intent();
                        intent3.setClass(NormalActivity.this,ModifyActivity.class);
                        intent3.putExtra("PageName","boold");
                        intent3.putExtra("result",resulttext);
                        startActivityForResult(intent3,REQUESTCODE);
                        break;
                    case R.id.bmi_normal_layout:
                        Intent intent4 = new Intent();
                        intent4.setClass(NormalActivity.this,ModifyActivity.class);
                        intent4.putExtra("PageName","bmi");
                        intent4.putExtra("result",resulttext);
                        intent4.putExtra("type",type);
                        startActivityForResult(intent4,REQUESTCODE);
                        break;
                    default:
                        break;
                }
            }
        };
        fev1_layout.setOnClickListener(clickListener);
        heartRate_layout.setOnClickListener(clickListener);
        breathRate_layout.setOnClickListener(clickListener);
        booldRate_layout.setOnClickListener(clickListener);
        bmi_layout.setOnClickListener(clickListener);

    }

    /**
     * 初始化控件线程
     */
    public class MyThread implements Runnable{
        @Override
        public void run() {
            info = WebService.executeHttpGetNormal(User.bindID);
            System.out.println(User.bindID+"info:"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    dialog.dismiss();
                    if(info!=null){
                        try {
                            JSONObject jsonObj = new JSONObject(info);
                            fev1.setText(jsonObj.getString("fev1"));
                            heartRate.setText(jsonObj.getString("heart_rate"));
                            booldRate.setText(jsonObj.getString("boold_rate_up")+"/"+ jsonObj.getString("boold_rate_down"));
                            breathRate.setText(jsonObj.getString("breath_rate"));
                            bmi.setText(jsonObj.getString("BMI"));
                            System.out.println(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 5:
                String result = data.getStringExtra("result");
                String type = data.getStringExtra("type");
                if(type.equals("0")){
                    fev1.setText(result);
                }else if(type.equals("1")){
                    heartRate.setText(result);

                }else if(type.equals("2")){
                    breathRate.setText(result);

                }else if(type.equals("4")){
                    bmi.setText(result);
                }else if(type.equals("3")){
                    booldRate.setText(result);
                }
                Toast.makeText(NormalActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(NormalActivity.this,"修改失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
