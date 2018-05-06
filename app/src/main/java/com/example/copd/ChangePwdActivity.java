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

import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class ChangePwdActivity extends AppCompatActivity {
    private EditText initialpwd;
    private EditText change_pwd;
    private EditText re_pwd;
    private Button change_button;
    private View focusView = null;
    private String info = null;
    private String info1 = null;
    private static Handler handler = new Handler();
    private ProgressDialog dialog;
    private boolean cancel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ActivityController.addActivity(ChangePwdActivity.this);
        //初始化控件
        initialpwd = (EditText)findViewById(R.id.pwd);
        change_pwd = (EditText)findViewById(R.id.change_pwd);
        re_pwd = (EditText)findViewById(R.id.repeat_pwd);
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        change_button =(Button)findViewById(R.id.pwd_button) ;
        /**
         * 点击修改按钮事件
         */
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = false;
                /**
                 * 判断原始密码是否正确
                 */
                if(TextUtils.isEmpty(initialpwd.getText().toString())){
                    initialpwd.setError("原始密码不能为空");
                    focusView = initialpwd;
                    cancel = true;
                }
                if(!initialpwd.getText().toString().equals(User.password)){
                    initialpwd.setError("原始密码不正确");
                    focusView = initialpwd;
                    cancel = true;
                }

                /**
                 * 判断新密码是否符合要求
                 */
                if (TextUtils.isEmpty(change_pwd.getText().toString())){
                    change_pwd.setError("密码不能为空");
                    focusView = change_pwd;
                    cancel = true;
                }
                if(!TextUtils.isEmpty(change_pwd.getText().toString()) && !isPwdValid(change_pwd.getText().toString())){
                    change_pwd.setError("密码只能由数字字母下划线组成");
                    focusView = change_pwd;
                    cancel = true;
                }

                if (!isPwdLenthLegal(change_pwd.getText().toString())) {
                    change_pwd.setError("密码长度应为 6-16 位");
                    focusView = change_pwd;
                    cancel = true;
                }
                /**
                 * 判断新密码和重复密码是否一致
                 */
                if(!change_pwd.getText().toString().equals(re_pwd.getText().toString())){
                    re_pwd.setError("与新密码不一致");
                    focusView = re_pwd;
                    cancel = true;
                }




                if(cancel){
                    focusView.requestFocus();
                }else{
                    dialog = new ProgressDialog(ChangePwdActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("注册中，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    /**
                     * 修改并跳转
                     */

                    new Thread(new ChangePwdActivity.MyThread()).start();


                }
            }
        });
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
        appbar.setTitle("修改密码");
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
    //验证密码长度是否在 6 - 16位
    private boolean isPwdLenthLegal(String pwd) {
        //TODO: Replace this with your own logic
        return (pwd.length() > 5) && (pwd.length() < 17);
    }


    //判断用户密码是否修改成功
    public class MyThread implements Runnable {
        @Override
        public void run() {
            info1 = WebService.executeHttpCheckPwd(User.me, initialpwd.getText().toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (info1.contains("true")) {
                        System.out.println("1111111111111");
                        new Thread(new ChangePwdActivity.MyThread1()).start();

                    } else {
                        System.out.println("info1:" + info1);
                        initialpwd.setError("原始密码错误");
                        focusView = initialpwd;
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
            info = WebService.executeHttpChangePwd(User.me, change_pwd.getText().toString());
            System.out.println("here is info:"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    if (info.contains("true")) {
                        System.out.print("info:"+info);
//                        new Thread(new ChangePwdActivity().MyThread1()).start();
                        dialog.setMessage("注册成功");
                        dialog.dismiss();

                        setResult(1,intent);

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
}
