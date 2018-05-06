package com.example.copd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActivityController.addActivity(ResultActivity.this);
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        appbar.setTitle("修改结果");
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
    }
    public void onDestory(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
