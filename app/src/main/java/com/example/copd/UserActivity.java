package com.example.copd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copd.Model.User;
import com.example.copd.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends Fragment {
    private String info;
    private TextView username;
    private TextView machine;
    private  TextView birth;
    private  TextView disease_history;
    private Button change_user;
    private Button exit_app;
    private LinearLayout user_layout;
    private  LinearLayout machine_layout;
    private LinearLayout birth_layout;
    private LinearLayout desease_layout;
    private final int REQUESTCODE = 1;
    private String resulttext = "";
    private Context  mContext;
    private LinearLayout normal;

    private static Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment1 = inflater.inflate(R.layout.activity_user, null);
        new Thread(new UserActivity.MyThread()).start();
        mContext = getActivity();
        return fragment1;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username = (TextView)getActivity().findViewById(R.id.username);
        machine = (TextView)getActivity().findViewById(R.id.device);
        birth = (TextView)getActivity().findViewById(R.id.age);
        disease_history = (TextView)getActivity().findViewById(R.id.history);
        change_user = (Button)getActivity().findViewById(R.id.switch_acount);
        exit_app = (Button)getActivity().findViewById(R.id.exit_app);
        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.switch_acount:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("是否切换账号？")
//                                .setIcon(R.drawable.heart_launch)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        User.me = null;
                                        User.bindID = null;
                                        ActivityController.finishAll();
                                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .setCancelable(false)
                                .show();
                        break;
                    case R.id.exit_app:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("是否退出应用？")
//                                .setIcon(R.drawable.heart_launch)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityController.finishAll();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .setCancelable(false)
                                .show();
                        break;
                    case R.id.username_layout:
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),ChangePwdActivity.class);
                        startActivityForResult(intent,REQUESTCODE);
                        break;
                    case R.id.machine_layout:
                        Intent intent1 = new Intent();
                        intent1.setClass(getActivity(),ModifyActivity.class);
                        intent1.putExtra("PageName","machine");
                        intent1.putExtra("result",resulttext);
                        startActivityForResult(intent1,REQUESTCODE);
                        break;
                    case R.id.birth_layout:
                        Intent intent2 = new Intent();
                        intent2.setClass(getActivity(),ChangeBirthActivity.class);
                        intent2.putExtra("PageName","birth");
                        startActivityForResult(intent2,REQUESTCODE);
                        break;
                    case R.id.disease_layout:
                        Intent intent3 = new Intent();
                        intent3.setClass(getActivity(),ModifyActivity.class);
                        intent3.putExtra("PageName","disease");
                        startActivityForResult(intent3,REQUESTCODE);
                        break;
                    case R.id.normal_layout:
                        Intent intent4 = new Intent();
                        intent4.setClass(getActivity(),NormalActivity.class);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
            }
        };
        change_user.setOnClickListener(clickListener);
        exit_app.setOnClickListener(clickListener);
        user_layout = (LinearLayout)getActivity().findViewById(R.id.username_layout);
        user_layout.setOnClickListener(clickListener);
        machine_layout =(LinearLayout)getActivity().findViewById(R.id.machine_layout);
        machine_layout.setOnClickListener(clickListener);
        birth_layout = (LinearLayout)getActivity().findViewById(R.id.birth_layout);
        birth_layout.setOnClickListener(clickListener);
        desease_layout = (LinearLayout)getActivity().findViewById(R.id.disease_layout);
        desease_layout.setOnClickListener(clickListener);
        normal = (LinearLayout)getActivity().findViewById(R.id.normal_layout);
        normal.setOnClickListener(clickListener);
    }
    //为了获取结果

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
//        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                if (requestCode == REQUESTCODE){
                    Toast.makeText(mContext,"修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(requestCode == REQUESTCODE){
                    String result = data.getStringExtra("result");
                    machine.setText(result);
                    Toast.makeText(mContext,"修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if(requestCode == REQUESTCODE){
                    String result = data.getStringExtra("result");
                    birth.setText(result);
                    Toast.makeText(mContext,"修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if(requestCode == REQUESTCODE){
                    String result = data.getStringExtra("result");
//                    System.out.println(result);
                    disease_history.setText(result);
                    Toast.makeText(mContext,"修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case -1:
                Toast.makeText(mContext,"修改失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    public void onDestroy(){
        super.onDestroy();
    }
    public class MyThread implements Runnable{
        @Override
        public void run() {
            System.out.println("username:"+ User.bindID);
            String path = "http://123.207.20.100:8080"+"/aecopdDB/userDetail";
            path = path+"?machine_id="+User.bindID;
            System.out.println(path);
            info = WebService.executeHttpGetLatest(path);
            System.out.println("hello:----"+info);
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    dialog.dismiss();
                    if(info!=null){
                        try {
                            JSONObject jsonObj = new JSONObject(info);
                            username.setText(User.me);
                            machine.setText(User.bindID);
//                            String[] da = jsonObj.getString("BirthDate").split(",");

                            birth.setText(jsonObj.getString("time"));
                            disease_history.setText(Integer.toString(jsonObj.getInt("disease_history")));
                            System.out.print("check disease history"+disease_history.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        System.out.println(info);
                    }
                }
            });
        }
    }
}
