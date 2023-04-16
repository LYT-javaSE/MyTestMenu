package com.example.mytestmenu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.Users;

import org.litepal.LitePal;

import java.util.List;


public class LoginActivity extends AppCompatActivity{
    private Button mbtn1;
    private EditText medtPho;
    private EditText medtPwd;
    Intent intent=null;
    private boolean isPatient=true; // 记录点击的按钮是否为患者按钮
    private int loginSuccess=0;//判断是否匹配成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //检查系统是否开启了地理位置权限;
        //注意：此时的Manifest的导入包路径import android.Manifest;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


//      找到控件
        mbtn1=findViewById(R.id.login_button);
        medtPho=findViewById(R.id.phone_text);
        medtPwd=findViewById(R.id.password_text);
//      绑定身份
        RadioGroup radioGroup=findViewById(R.id.rg_role);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rb_patient){
                    isPatient=true;
                    Toast.makeText(LoginActivity.this,"用户端登录",Toast.LENGTH_LONG).show();
                }
                else {
                    isPatient=false;
                    Toast.makeText(LoginActivity.this,"医生端登录",Toast.LENGTH_LONG).show();
                }
            }
        });
        //      点击登录
        mbtn1.setOnClickListener(this::gohome);
    }
    public void gohome(View view){
//        获取输入信息
        String phone=medtPho.getText().toString().trim();
        String pwd=medtPwd.getText().toString().trim();
        if (isPatient) {
            if (phone.equals("") || pwd.equals("")) {
                Toast.makeText(LoginActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Users> users = LitePal.findAll(Users.class);
            for (Users patient : users) {
//              打印查到的所有号码
                Log.d("LoginActivity", "数据库里的号码: " + patient.getUserPhone());
                if (patient.getUserPhone().equals(phone) && !patient.getUserPassword().equals(pwd)) {
                    loginSuccess = 0;
                    break;
                } else if(patient.getUserPhone().equals(phone) && patient.getUserPassword().equals(pwd)) {
                    loginSuccess = 1;
                    break;
                }
                else {
                    loginSuccess = 2;
                }
            }
            switch (loginSuccess) {
                case 0: {
                    Toast.makeText(this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1: {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this, MainActivity.class);
//                    查询phone对应的用户名
                    List<Users> u=LitePal.where("userPhone=?",phone).find(Users.class);
//                    传到MainActivity中去
                    intent.putExtra("userName",u.get(0).getUserName());
                    intent.putExtra("userPhone", phone);
                    startActivity(intent);
                    break;
                }
                case 2:{
                    Toast.makeText(this, "用户名不存在，请先注册", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }else {
            if (phone.equals("") || pwd.equals("")) {
                Toast.makeText(LoginActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Doctors> doctors = LitePal.findAll(Doctors.class);
            for (Doctors doctor : doctors) {
//              打印查到的所有号码
                Log.d("LoginActivity", "数据库里的号码: " + doctor.getDoctPhone());
                if (doctor.getDoctPhone().equals(phone) && !doctor.getDoctPassword().equals(pwd)) {
                    loginSuccess = 0;
                    break;
                } else if(doctor.getDoctPhone().equals(phone) && doctor.getDoctPassword().equals(pwd)) {
                    loginSuccess = 1;
                    break;
                }
                else {
                    loginSuccess = 2;
                }
            }
            switch (loginSuccess) {
                case 0: {
                    Toast.makeText(this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1: {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this, DocBaseActivity.class);
                    List<Doctors> d=LitePal.where("doctPhone=?",phone).find(Doctors.class);
                    intent.putExtra("doctName", d.get(0).getDoctName());
                    intent.putExtra("doctPhone", phone);
                    startActivity(intent);
                    break;
                }
                case 2:{
                    Toast.makeText(this, "用户名不存在，请先注册", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }

        }
    }
    public void gopwd(View view){
        Intent intent=new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
        finish();
    }
    public void goreg(View view){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("----","申请权限成功");
            }else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Log.e("----","申请权限失败");
            }
    }

    @Override
    protected void onNewIntent(Intent intent) {
// TODO Auto-generated method stub
        super.onNewIntent(intent);
//退出
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }
}