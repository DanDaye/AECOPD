package com.example.copd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialNormalActivity extends AppCompatActivity {
    private String info;
    private TextInputEditText fev1;
    private TextInputEditText breath;
    private TextInputEditText heart;
    private TextInputEditText boold;
    private TextInputEditText bmi;
    private Button button ;
    private View focusView = null;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_normal);
        ActivityController.addActivity(InitialNormalActivity.this);
        /**
         * 初始化控件
         */
        fev1 = (TextInputEditText)findViewById(R.id.initial_fev1);
        breath = (TextInputEditText)findViewById(R.id.initial_breath);
        heart = (TextInputEditText)findViewById(R.id.initial_heart);
        boold = (TextInputEditText)findViewById(R.id.initial_boold);
        bmi = (TextInputEditText)findViewById(R.id.initial_bmi);
        button = (Button)findViewById(R.id.initial_button);
        Toolbar appbar = (Toolbar)findViewById(R.id.toolbar);
        appbar.setTitle("稳定期状况");
        appbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(DataDetailActivity.this,"大家好",0).show();
                        finish();
                    }
                }
        );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                if(TextUtils.isEmpty(fev1.getText().toString())){
                    fev1.setError("肺功能评估不能为空");
                    focusView = fev1;
                    cancel = true;
                }else if(!checkIsNumber(fev1.getText().toString(),"^([1-9]\\d)|([1-9]\\d+\\.\\d)$")){
                    fev1.setError("肺功能评估结果范围为：10-99.9");
                    focusView = fev1;
                    cancel = true;
                }

                if(TextUtils.isEmpty(breath.getText().toString())){
                    breath.setError("呼吸频率不能为空");
                    focusView = breath;
                    cancel = true;
                }else if(!checkIsNumber(breath.getText().toString(),"^[1-7][0-9]|80$")){

                        breath.setError("呼吸频率范围为：10-80");
                        focusView = breath;
                        cancel = true;
                }

                if(TextUtils.isEmpty(heart.getText().toString())){
                    focusView = heart;
                    heart.setError("心率不能为空");
                    cancel = true;
                }else if(!checkIsNumber(heart.getText().toString(),"^([1-9]\\d)|([1]\\d{2})$")){
                        heart.setError("心率范围为：10-199");
                        focusView = heart;
                        cancel = true;

                }

                if(TextUtils.isEmpty(boold.getText().toString())){
                    boold.setError("血压不能为空");
                    focusView = boold;
                    cancel = true;
                }else  if(!checkIsNumber(boold.getText().toString(),"^(([1-9]\\d)|([1]\\d{2}))/(([1-9]\\d)|([1][0-2]\\d))$")){
                        boold.setError("血压格式为：收缩压（10-199）/舒张压(10-129)");
                        focusView = boold;
                        cancel = true;

                }

                if(TextUtils.isEmpty(bmi.getText().toString())){
                    bmi.setError("BMI不能为空");
                    focusView = bmi;
                    cancel = true;
                }else if(!checkIsNumber(bmi.getText().toString(),"^([1-4]\\d)|([1-4]\\d\\.\\d)$")){
                        bmi.setError("BMI范围为:10-49.9");
                        focusView = bmi;
                        cancel = true;

                }
                if (cancel){
                    focusView.requestFocus();
                }else{
                    dialog = new ProgressDialog(InitialNormalActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("新增中，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Thread(new InitialNormalActivity.MyThread()).start();

                }
            }
        });
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpAddNormal(User.bindID,fev1.getText().toString(),breath.getText().toString(),heart.getText().toString(),boold.getText().toString(),bmi.getText().toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(info.contains("true")){
                        dialog.dismiss();
                        Toast.makeText(InitialNormalActivity.this,"新增成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(InitialNormalActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(InitialNormalActivity.this,"新增失败",Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
    }

    /**
     * 匹配正则表达式
     * @param text
     * @param text1
     * @return
     */
    private boolean checkIsNumber(String text,String text1) {
        Pattern pattern = Pattern.compile(text1);
        Matcher matcher = pattern.matcher((CharSequence)text);
        return matcher.matches();
    }
    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
