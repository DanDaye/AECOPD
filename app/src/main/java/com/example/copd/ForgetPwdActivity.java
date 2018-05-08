package com.example.copd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.copd.Base.Base64;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.util.regex.Pattern;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username ;
    private EditText pwd;
    private EditText repeat_pwd;
    private Button button;
    private Toolbar toolbar;
    private View focusView = null;
    private String info;
    private static Handler handler = new Handler();
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        /*
        *初始化界面
         */
        username = (EditText)findViewById(R.id.forget_user_name);
        pwd = (EditText)findViewById(R.id.forget_change_pwd);
        repeat_pwd = (EditText)findViewById(R.id.forget_repeat_pwd);
        button = (Button)findViewById(R.id.forget_pwd_button);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("忘记密码");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }

        });
        button.setOnClickListener(this);
    }
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.forget_pwd_button:
                boolean cancel = isValid(username,pwd,repeat_pwd);
                if (cancel){

                    focusView.requestFocus();
                }else{
                    dialog = new ProgressDialog(ForgetPwdActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("修改中，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    /**
                     * 修改并跳转
                     */

                    new Thread(new ForgetPwdActivity.MyThread1()).start();
                }
                break;
            default:break;
        }
    }
    public boolean isValid(EditText user,EditText pwd,EditText repear_pwd){
        String user_name = user.getText().toString();
        String password = pwd.getText().toString();
        String repeat_password = repear_pwd.getText().toString();
        if (TextUtils.isEmpty(user_name)){
            username.setError("用户名不能为空");
            focusView = username;
            return true;
        }
//        if(TextUtils.isEmpty(password)){
//            pwd.setError("新密码不能为空");
//            focusView = pwd;
//            return  true;
//        }
        if(!TextUtils.isEmpty(password) && !isPwdValid(password)){
            pwd.setError("密码只能由数字字母下划线组成");
            focusView = pwd;
            return true;
        }

        if (!isPwdLenthLegal(password)) {
            pwd.setError("密码长度应为 6-16 位");
            focusView = pwd;
            return true;
        }
        if(TextUtils.isEmpty(repeat_password)){
            repear_pwd.setError("重复密码不能为空");
            focusView = repear_pwd;
            return true;
        }
        if(!password.equals(repeat_password)){
            focusView = repear_pwd;
            repear_pwd.setError("密码不一致");
            return true;
        }
        return false;
    }

    private boolean isPwdLenthLegal(String password) {
        return (pwd.length() > 5) && (pwd.length() < 17);
    }

    private boolean isPwdValid(String password) {
        String reg = "\\w+([_]\\w+)*";
        // 创建 Pattern 对象
        Pattern p = Pattern.compile(reg);
        return p.matcher(password).matches();
    }

    /**
     * changepwd
     */
    public class MyThread1 implements Runnable {
        @Override
        public void run() {
            info = WebService.executeHttpChangePwd(username.getText().toString(),new String(Base64.encode(pwd.getText().toString().getBytes())));
            System.out.println("here is info:"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    if (info.contains("true")) {
                        System.out.print("info:"+info);
//                        new Thread(new ChangePwdActivity().MyThread1()).start();
                        dialog.setMessage("修改成功");
                        dialog.dismiss();
                        Toast.makeText(ForgetPwdActivity.this,"修改成功", Toast.LENGTH_SHORT).show();
                        finish();


                    }else{
                        if(info.equals("1")){
                            username.setError("用户名不存在");
                            focusView = username;
                            focusView.requestFocus();
                        }else {
                            Toast.makeText(ForgetPwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
//                    finish();
                }
            });
        }
    }

}
