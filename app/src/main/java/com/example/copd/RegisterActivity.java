package com.example.copd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.copd.Base.Base64;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import android.app.DatePickerDialog;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

public class RegisterActivity extends BaseActivity implements NavigationView.OnClickListener {
    private static final String TAG = "RegisterActivity";
    // UI 组件
    private EditText usernameText;
    private EditText bindIdText;
    private EditText mPasswordView;
    private EditText rePasswordText;
    private View mRegisterForm;
    private View mProgressView;
//    private EditText disease_history;
//    private int mYear;
//    private int mMonth;
//    private int mDay;
//    private EditText birthdate;
    private TextInputLayout birth_layout;
    final int DATE_DIALOG =1;
    private String info;
    private ProgressDialog dialog;
    private View focusView = null;
    private static Handler handler = new Handler();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityController.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        //表单项
        mRegisterForm = findViewById(R.id.register_form);
        usernameText = (EditText) findViewById(R.id.username);
        bindIdText = (EditText) findViewById(R.id.bind_id);
        mPasswordView = (EditText) findViewById(R.id.reg_pwd);
        rePasswordText = (EditText)findViewById(R.id.re_pwd);
        Button registerbtn = (Button) findViewById(R.id.register_button);
//        birthdate = (EditText)findViewById(R.id.birthdate);
//        disease_history = (EditText)findViewById(R.id.disease_history);
        registerbtn.setOnClickListener(this);
//        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        /**
         * 设置返回按钮
         */
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(DataDetailActivity.this,"大家好",0).show();
                        finish();
                    }
                }
        );
//        birthdate.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public void onClick(View v) {
////                showDialog(DATE_DIALOG);
////            }

//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                showDialog(DATE_DIALOG);
//                return true;
//            }
//            //            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                showDialog(DATE_DIALOG);
////            }
//        });
//        Calendar ca = Calendar.getInstance();
//        mYear = ca.get(Calendar.YEAR);
//        mMonth = ca.get(Calendar.MONTH);
//        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mProgressView = findViewById(R.id.register_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    };
//    protected Dialog onCreateDialog(int id){
//        switch (id){
//            case DATE_DIALOG:
//                return new DatePickerDialog(RegisterActivity.this, mdateListener,mYear,mMonth,mDay);
//        }
//        return null;
//    }
//    public void display() {
//        birthdate.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
//    }

//    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//            mYear = year;
//            mMonth = monthOfYear;
//            mDay = dayOfMonth;
//            display();
//        }
//    };


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        usernameText.setError(null);
        bindIdText.setError(null);
        mPasswordView.setError(null);
        rePasswordText.setError(null);
//        birthdate.setError(null);
//        disease_history.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameText.getText().toString();
        String bindid = bindIdText.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = rePasswordText.getText().toString();
//        String birth = birthdate.getText().toString();
//        String disease = disease_history.getText().toString();
        boolean cancel = false;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(username)) {
            usernameText.setError("用户名不能为空");
            focusView = usernameText;
            cancel = true;
        }
        if (TextUtils.isEmpty(bindid)) {
            bindIdText.setError("绑定终端ID不能为空");
            focusView = bindIdText;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("密码不能为空");
            focusView = mPasswordView;
            cancel = true;
        }
        if (!rePassword.equals(password)) {
            rePasswordText.setError("前后密码不一致");
            focusView = rePasswordText;
            cancel = true;
        }

        // Check for a valid
        if (!isTelLenthLegal(bindid)) {
            bindIdText.setError("终端ID为3位数字");
            focusView = bindIdText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(username) && !isUsernameValid(username)) {
            usernameText.setError("用户名只能由数字字母下划线组成");
            focusView = usernameText;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPwdValid(password)) {
            mPasswordView.setError("密码只能由数字字母下划线组成");
            focusView = mPasswordView;
            cancel = true;
        }

        if (!isPwdLenthLegal(password)) {
            mPasswordView.setError("密码长度应为 6-16 位");
            focusView = mPasswordView;
            cancel = true;
        }
//        if(TextUtils.isEmpty(birth)){
//            birthdate.setError("出生日期不能为空");
//            focusView = birthdate;
//            cancel = true;
//        }
//        if(TextUtils.isEmpty(disease)){
//            disease_history.setError("急性加重病史不能为空");
//            focusView = disease_history;
//            cancel = true;
//        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            if(!checkNetwork()){
                Toast toast = Toast.makeText(RegisterActivity.this,"网络未连接",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }else{
                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("注册中，请稍后...");
                dialog.setCancelable(false);
                dialog.show();
                new Thread(new RegisterActivity.MyThread()).start();
            }
        }
    }

    private boolean checkNetwork() {
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conManager.getActiveNetworkInfo() !=null){
            return conManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                attemptRegister();
                break;
            default:
                break;
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            info = WebService.executeHttpRegister(usernameText.getText().toString(),bindIdText.getText().toString(),new String(Base64.encode(rePasswordText.getText().toString().getBytes())));
            System.out.println("hello"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    if(info!=null && info.contains("true")){
                        dialog.setMessage("注册成功");
                        try {
                            sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        System.out.println("info:"+info);
                        if(info.contains("1")){
                            usernameText.setError("用户名已存在");
                            focusView = usernameText;


                        }else{
                            bindIdText.setError("设备不存在");
                            focusView = bindIdText;
                        }
                        focusView.requestFocus();
                        dialog.setMessage("注册失败");
                        dialog.dismiss();
                    }

                }
            });
        }
    }

    private boolean isTelLenthLegal(String bindid) {
        //TODO: Replace this with your own logic
        return bindid.length() == 3;
    }

    /**
     * 用户名只能包含数字，英文，下划线
     *
     * @param username
     * @return
     */
    private boolean isUsernameValid(String username) {
        String reg = "\\w+([-+.]\\w+)*";
        // 创建 Pattern 对象
        Pattern p = Pattern.compile(reg);
        return p.matcher(username).matches();
    }

    //验证密码长度是否在 6 - 16位
    private boolean isPwdLenthLegal(String pwd) {
        //TODO: Replace this with your own logic
        return (pwd.length() > 5) && (pwd.length() < 17);
    }

    /**
     * 密码只能包含数字，英文，下划线
     *
     * @param password
     * @return
     */
    private boolean isPwdValid(String password) {
        String reg = "\\w+([_]\\w+)*";
        // 创建 Pattern 对象
        Pattern p = Pattern.compile(reg);
        return p.matcher(password).matches();
    }
    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

}
