package com.example.copd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyActivity extends AppCompatActivity {
    private TextInputEditText modify_text;
    private Button modify_button;
    private String now = "";
    private View focusView = null;
    private ProgressDialog dialog;
    private boolean cancel = false;
    private String info = null;
    private String info1 = null;
    private String type = null;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        ActivityController.addActivity(ModifyActivity.this);

        //初始化控件
        modify_text = (TextInputEditText)findViewById(R.id.modify_text);
        modify_button = (Button)findViewById(R.id.modify_button);
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("PageName");
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
        switch (name) {
            case "machine":
                appbar.setTitle("修改设备");
                modify_text.setHint("设备名称");
                now="设备名称";
                break;
            case "disease":
                appbar.setTitle("修改病史");
                now = "病史";
                break;
            case "fev1":
                appbar.setTitle("修改稳定期肺功能评估结果");
                now = "肺功能评估结果";
                break;
            case "breath":
                appbar.setTitle("修改稳定期呼吸频率");
                now = "呼吸频率";
                break;
            case "heart":
                appbar.setTitle("修改稳定期心率");
                now = "心率";
                break;
            case "boold":
                appbar.setTitle("修改稳定期血压");
                now =  "血压";
                break;
            case "bmi":
                appbar.setTitle("修改稳定期BMI");
                now = "bmi";
                break;
            default:
                break;
        }
        modify_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("onclick listener");
                cancel = false;
                new AlertDialog.Builder(ModifyActivity.this)
                        .setTitle("是否确定修改？")
//                                .setIcon(R.drawable.heart_launch)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityController.finishAll();
                                String text = modify_text.getText().toString();
                                if(TextUtils.isEmpty(modify_text.getText().toString())){
                                    modify_text.setError(now+"不能为空");
                                    focusView = modify_text;
                                    cancel = true;
                                }
                                if(now.equals("病史")){
                                    boolean r = checkIsNumber(text,"[0-5]");
                                    if(!r){
                                        modify_text.setError(now+"范围为0~5");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                if(now.equals("肺功能评估结果")){
                                    boolean r = checkIsNumber(text,"^([1-9]\\d)|([1-9]\\d+\\.\\d)$");
                                    System.out.println("result:"+r);
                                    if(!r){
                                        modify_text.setError(now+"范围为10~99.9");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                if(now.equals("呼吸频率")){
                                    boolean r = checkIsNumber(text,"^[1-7][0-9]|80$");
                                    if(!r){
                                        modify_text.setError(now+"范围为10~80");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                if(now.equals("心率")){
//                    boolean r = checkIsNumber(text,"^[1][0-9][0-9]| 200$");
                                    boolean r1 = checkIsNumber(text,"^([1-9]\\d)|([1]\\d{2})$");
                                    if(!r1){
                                        modify_text.setError(now+"范围为10~199");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                if(now.equals("血压")){
                                    boolean r1 = checkIsNumber(text,"^(([1-9]\\d)|([1]\\d{2}))/(([1-9]\\d)|([1][0-2]\\d))$");
                                    if(!r1){
                                        modify_text.setError(now+"格式为：收缩压（10-199）/舒张压(10-129)");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                if(now.equals("bmi")){
                                    boolean r = checkIsNumber(text,"^([1-4]\\d)|([1-4]\\d\\.\\d)$");
                                    if(!r){
                                        modify_text.setError(now+"范围为10~49.9");
                                        focusView = modify_text;
                                        cancel = true;
                                    }
                                }
                                doCheck(name,cancel);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void doCheck(String name,boolean cancel){
        if(cancel){
            focusView.requestFocus();
        }else{
            dialog = new ProgressDialog(ModifyActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("修改中，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            /**
             * 修改并跳转
             */

            switch (name){

                case "machine":
                    new Thread(new ModifyActivity.MyThread()).start();
                    break;
                case "disease":
                    new Thread(new ModifyActivity.MyThreadBirth1()).start();
                    break;
                case "fev1":
                    type= "0";
                    break;
                case "heart":
                    type = "1";
                    break;

                case "breath":
                    type = "2";
                    break;
                case "boold":
                    type="3";
                    break;
                case "bmi":
                    type = "4";
                    break;
                default:
                    break;
            }
            if(type !=null){
                new Thread(new ModifyActivity.MyThreadChangeNormal()).start();
            }
        }
    }

    private boolean checkIsNumber(String text,String text1) {
        Pattern pattern = Pattern.compile(text1);
        Matcher matcher = pattern.matcher((CharSequence)text);
        return matcher.matches();
    }


    /**
     * 修改设备线程
     */
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info1 = WebService.executeHttpCheckMachine(modify_text.getText().toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (info1.contains("true")) {
                        System.out.println("1111111111111");
                        new Thread(new ModifyActivity.MyThread1()).start();

                    } else {
                        System.out.println("info1:" + info1);
                        modify_text.setError("设备不存在");
                        focusView = modify_text;
                        focusView.requestFocus();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    public class MyThread1 implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpChangeMachine(User.me,modify_text.getText().toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    User.bindID = modify_text.getText().toString();
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("result",modify_text.getText().toString());
                    if (info.contains("true")) {
                        setResult(2,intent);
                    }else{
                        setResult(-1,intent);
                    }
                    finish();
                }
            });
        }
    }




    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
    /**
     * 修改出生日期线程
     */

    public class MyThreadBirth1 implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpChangeDisease(User.me,modify_text.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    User.bindID = modify_text.getText().toString();
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("result",modify_text.getText().toString());
                    if (info.contains("true")) {
                        setResult(4,intent);
                    }else{
                        setResult(-1,intent);
                    }
                    finish();
                }
            });
        }
    }
    public class MyThreadChangeNormal implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpChangeNormal(User.bindID,type,modify_text.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("result",modify_text.getText().toString());
                    intent.putExtra("type",type);
                    if (info.contains("true")) {
                        setResult(5,intent);
                    }else{
                        setResult(-1,intent);
                    }
                    finish();
                }
            });
        }
    }
}
