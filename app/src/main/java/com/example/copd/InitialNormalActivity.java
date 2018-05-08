package com.example.copd;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import com.example.copd.Model.Normal;
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
    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText disease_times;
    private EditText birthdate;
    private Button button ;
    private EditText temperature;
    private EditText relivate;
    private View focusView = null;
    private ProgressDialog dialog;
    final int DATE_DIALOG =1;
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
        birthdate = (EditText)findViewById(R.id.birthdate) ;
        disease_times = (EditText)findViewById(R.id.disease_history) ;
        temperature = (EditText)findViewById(R.id.temperature);
        relivate = (EditText)findViewById(R.id.relivate);
        Toolbar appbar = (Toolbar)findViewById(R.id.toolbar);
        appbar.setTitle("患者信息初始化");
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
                if (TextUtils.isEmpty(temperature.getText().toString())){
                    temperature.setError("温度不能为空");
                    focusView = temperature;
                    cancel = true;
                }else if(!checkIsNumber(temperature.getText().toString(),"\\d|[1-4][0-9]$")){
                    temperature.setError("温度范围为:0-49");
                    focusView = temperature;
                    cancel = true;
                }

                if(TextUtils.isEmpty(relivate.getText().toString())){
                    relivate.setError("相对湿度不能为空");
                    focusView = relivate;
                    cancel = true;
                }else if(!checkIsNumber(relivate.getText().toString(),"\\d|[1-9][0-9]|100$")){
                    relivate.setError("相对湿度范围为：0-100");
                    focusView = relivate;
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
                if(TextUtils.isEmpty(birthdate.getText().toString())) {
                    birthdate.setError("出生日期不能为空");
                    focusView = birthdate;
                    cancel = true;
                }
                if(TextUtils.isEmpty(disease_times.getText().toString())) {
                    disease_times.setError("急性加重病史不能为空");
                    focusView = disease_times;
                    cancel = true;
                }
                if (cancel){
                    focusView.requestFocus();
                }else{
                    Normal.fev1 = fev1.getText().toString();
                    Normal.bmi = bmi.getText().toString();
                    Normal.heartRate = heart.getText().toString();
                    Normal.breathRate = breath.getText().toString();
                    dialog = new ProgressDialog(InitialNormalActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("新增中，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Thread(new InitialNormalActivity.MyThread()).start();

                }
            }
        });

        birthdate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(DATE_DIALOG);
//            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(DATE_DIALOG);
                return true;
            }
            //            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showDialog(DATE_DIALOG);
//            }
        });
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);


    }
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG:
                return new DatePickerDialog(InitialNormalActivity.this, mdateListener,mYear,mMonth,mDay);
        }
        return null;
    }
    public void display() {
        birthdate.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpAddNormal(User.bindID,fev1.getText().toString(),breath.getText().toString(),heart.getText().toString(),boold.getText().toString(),bmi.getText().toString(),temperature.getText().toString(),relivate.getText().toString(),birthdate.getText().toString(),disease_times.getText().toString());

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
