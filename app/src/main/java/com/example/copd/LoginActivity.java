package com.example.copd;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Base.Base64;
import com.example.copd.Model.Latest;
import com.example.copd.Model.Normal;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.copd.Model.User.bindID;
import static com.example.copd.Model.User.me;
import static com.example.copd.Model.User.password;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private boolean mChecked;
    private Button logbtn;
    private EditText username;
    private EditText password;
    private Button register;
    private Button forget_pwd;

    //记住密码
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor;
    private CheckBox remberpwd;
//    private ProgressDialog dialog;
    private ProgressDialog dialog;
    private TextView infotv, regtv;
    private String info;
    private String info1;
    private static Handler handler = new Handler();
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取控件
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        infotv = (TextView)findViewById(R.id.information);
        logbtn = (Button)findViewById(R.id.sign_in_button);
        logbtn.setOnClickListener(this);
        remberpwd = (CheckBox)findViewById(R.id.remember_pwd);
        register = (Button)findViewById(R.id.register) ;
        register.setOnClickListener(this);
        forget_pwd = (Button)findViewById(R.id.forget_pwd);
        forget_pwd.setOnClickListener(this);
        //记住密码功能
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        remberpwd = (CheckBox) findViewById(R.id.remember_pwd);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember) {
            //将账号密码设置到文本框
            String username1 = pref.getString("username", "");
            String pwd1 = pref.getString("password", "");
            username.setText(username1);
            password.setText(pwd1);
            remberpwd.setChecked(true);
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                if(!checkNetwork()){
                    Toast toast = Toast.makeText(LoginActivity.this,"网络未连接",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    break;
                }
                //提示框
                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("正在登陆，请稍后...");
                dialog.setCancelable(false);
                dialog.show();
                new Thread(new MyThread()).start();
                break;
            case R.id.register:
                Intent intent = new Intent();
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_pwd:
                Intent inte  = new Intent();
                inte.setClass(this,ForgetPwdActivity.class);
                startActivity(inte);

            default:
                break;
        }
    }
    public class MyThread implements Runnable{
        @Override
        public void run() {
            System.out.println("login activity");
            info = WebService.executeHttpGet(username.getText().toString(),new String(Base64.encode(password.getText().toString().getBytes())));
            System.out.println("info:"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    if(info!=null && info.contains("true")){
//                        User.me=(String)(username.getText());
                        editor = pref.edit();
                        if(remberpwd.isChecked()){
                            System.out.println("ischecked");
                            editor.putBoolean("remember_password",true);
                            editor.putString("username",username.getText().toString());
                            editor.putString("password",password.getText().toString());
                        }else{
                            editor.clear();
                        }
                        editor.apply();
                        User.me = username.getText().toString();
                        System.out.println("connection:success");
                        String []result  = info.split(":");
                        User.bindID = result[1];
                        User.password = password.getText().toString();
                        new Thread(new LoginActivity.MyThreadNormal()).start();
//                        Intent intent = new Intent();
//                        intent.setClass(LoginActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        System.out.println("come here?");
                    }else{
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    public class MyThreadNormal implements Runnable{
        @Override
        public void run() {
            info1 = WebService.executeHttpGetNormal(User.bindID);
            System.out.println(User.bindID+"info1:"+info1);
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    dialog.dismiss();
                    if(info1!=null){
                        if(info1.contains("false")){
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,InitialNormalActivity.class);
                            startActivity(intent);
                        }else {
                            try {
                                JSONObject jsonObj = new JSONObject(info1);
                                Normal.fev1 = jsonObj.getString("fev1");
                                Normal.breathRate = jsonObj.getString("breath_rate");
                                Normal.heartRate = jsonObj.getString("heart_rate");
                                Normal.booldRate = jsonObj.getString("boold_rate_up")+"/"+jsonObj.getString("boold_rate_down");
                                Normal.bmi = jsonObj.getString("BMI");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent =  new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }
    private boolean checkNetwork() {
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conManager.getActiveNetworkInfo() !=null){
            return conManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}

