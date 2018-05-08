package com.example.copd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Model.Latest;
import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import java.util.ArrayList;
import java.util.List;

public class Change_Desease_HistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TextView editText ;
    private Button button;
    private Spinner spinner;
    private ArrayAdapter<String> adapter ;
    private String select;
    private ProgressDialog dialog;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__desease__history);
        ActivityController.addActivity(Change_Desease_HistoryActivity.this);

        //初始化控件
        editText = (TextView)findViewById(R.id.modify_desease_history);
        editText.setText("咳嗽(咳痰)情况：");
        button = (Button)findViewById(R.id.modify_desease_button);
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
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("type");
        appbar.setTitle("修改咳痰(咳嗽)");
        spinner = (Spinner)findViewById(R.id.Spinner21);
        adapter = new ArrayAdapter<String>(Change_Desease_HistoryActivity.this,android.R.layout.simple_spinner_dropdown_item,getDataSource());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //初始化位置
        if (Latest.cough_level.equals("1")){
            spinner.setSelection(1);
        }else{
            spinner.setSelection(0);
        }
        button.setOnClickListener(this);

    }

    public List<String> getDataSource(){
        List<String> list = new ArrayList<String>();
        list.add("正常");
        list.add("加重");
        return  list;
    }




    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = spinner.getItemAtPosition(position).toString();
        select = position+"";
//        Toast.makeText(Change_Desease_HistoryActivity.this,item,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        int option;
        if(select.equals("加重")){
            option = 1;
        }else{
            option = 0;
        }
        dialog = new ProgressDialog(Change_Desease_HistoryActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("注册中，请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        new Thread(new Change_Desease_HistoryActivity.MyThread()).start();
    }

    public class MyThread implements Runnable{
        String info = null;
        public void run() {
            info = WebService.executeHttpChangeCough_Level(User.bindID,select);
//            System.out.println("select:"+select);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.putExtra("result",select);
                    if (info.contains("true")) {
                        System.out.print("info:"+info);
//                        new Thread(new ChangePwdActivity().MyThread1()).start();

                        dialog.setMessage("修改成功");
                        dialog.dismiss();

                        setResult(7,intent);
                    }else{
                        setResult(-1,intent);
                    }
                    finish();
                }
            });
        }
    }

}
