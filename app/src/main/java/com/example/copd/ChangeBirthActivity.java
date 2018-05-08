package com.example.copd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class ChangeBirthActivity extends AppCompatActivity implements View.OnClickListener {
    private Button modifyButton;
    private EditText birthText;
    final int DATE_DIALOG =1;
    private int mYear;
    private int mMonth;
    private int mDay;
    private View focusView = null;
    private static Handler handler = new Handler();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_birth);
        /**
         * 初始化控件
         */
        birthText = (EditText)findViewById(R.id.modify_birth);
        modifyButton=(Button)findViewById(R.id.birth_button) ;
        ActivityController.addActivity(ChangeBirthActivity.this);
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
        appbar.setTitle("修改出生日期");
        birthText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(DATE_DIALOG);
                return true;
            }
        });

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        modifyButton.setOnClickListener(this);
    }
    public void display() {
        birthText.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
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
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG:
                return new DatePickerDialog(ChangeBirthActivity.this, mdateListener,mYear,mMonth,mDay);
        }
        return null;
    }
    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.birth_button:
                boolean cancel = false;
                String birth = birthText.getText().toString();
                if(TextUtils.isEmpty(birth)){
                    birthText.setError("出生日期不能为空");
                    focusView = birthText;
                    cancel = true;
                }
                /**
                 * 比较日期
                 */
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                if(birth.compareTo(str) > 0){
                    birthText.setError("出生日期不能是未来时间");
                    focusView = birthText;
                    cancel = true;
                }

                if(cancel){
                    focusView.requestFocus();
                }else{
                    dialog = new ProgressDialog(ChangeBirthActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("注册中，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Thread(new ChangeBirthActivity.MyThread()).start();
                }
                break;
            default:
                break;
        }
    }

    public class MyThread implements Runnable {
        String info = null;
        @Override
        public void run() {
            info = WebService.executeHttpChangeBirth(User.bindID,birthText.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.putExtra("result",birthText.getText().toString());
                    if (info.contains("true")) {
                        System.out.print("info:"+info);
//                        new Thread(new ChangePwdActivity().MyThread1()).start();

                        dialog.setMessage("修改成功");
                        dialog.dismiss();

                        setResult(3,intent);
                    }else{
                        setResult(-1,intent);
                    }
                    finish();
                }
            });
        }
    }
}
