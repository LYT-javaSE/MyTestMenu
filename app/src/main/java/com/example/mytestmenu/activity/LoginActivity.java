package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestmenu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity{
    private Button mbtn1;
    private TextView tvReg;
    private TextView tvPwd;
    private EditText medtPho;
    private EditText medtPwd;
    private RadioGroup radioGroup;
    private boolean isPatient=true; // 记录点击的按钮是否为患者按钮
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
        tvReg=findViewById(R.id.register_text);
        tvPwd=findViewById(R.id.forgot_password_text);
        medtPho=findViewById(R.id.phone_text);
        medtPwd=findViewById(R.id.password_text);
//      绑定身份
        radioGroup=findViewById(R.id.rg_role);
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
        //        点击去注册
        tvReg.setOnClickListener(this::goreg);
        //        点击去修改密码
        tvPwd.setOnClickListener(this::gopwd);
    }
    public void gohome(View view){
        OkHttpClient client = new OkHttpClient();
//        获取输入信息
        String phone=medtPho.getText().toString().trim();
        String pwd=medtPwd.getText().toString().trim();
//        用户端登录
        if (isPatient) {
            if (phone.equals("") || pwd.equals("")) {
                Toast.makeText(LoginActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                return;
            }

//          发送登录请求
            String user_url = Base_URL + "/users/login";
            RequestBody requestBody = new FormBody.Builder()
                    .add("userPhone", phone)
                    .add("password", pwd)
                    .build();
            Request request = new Request.Builder()
                    .url(user_url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 请求失败，处理异常
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 请求成功，处理响应
                    if (response.isSuccessful()) {
                        try {
//                            将数据进行解析
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.d("显示传入的数据——", String.valueOf(jsonObject));

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            JSONObject dataObject = jsonObject.optJSONObject("data");
                            if (dataObject != null) {
                                JSONObject subDataObject = dataObject.optJSONObject("data");
                                if (subDataObject != null){
                                    int userPhone = subDataObject.getInt("phone");
                                    String userName = subDataObject.getString("name");
                                    String userPassword = subDataObject.getString("pwd");
                                    if (code == 200) {
                                        Log.d("状态码：", String.valueOf(code));
                                        // 登录成功，解析用户信息并跳转到主界面
                                        runOnUiThread(() -> {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("userPhone", userPhone);
                                            intent.putExtra("userName", userName);
                                            intent.putExtra("userPassword", userPassword);
                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            Log.d("成功登录提示,欢迎：", userName);
                                        });
                                    }
                                }else {
                                    subDataObject = new JSONObject();
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                                    Log.d("报错提示:", message);
                                }
                            }else {
                                dataObject = new JSONObject();
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                                Log.d("报错提示——", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败，服务器返回数据格式错误", Toast.LENGTH_SHORT).show());
                        }
                    }
                    else {
                        // 处理错误响应
                        String errorMessage = response.message();
                        // 显示错误提示
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.d("理应显示响应错误信息:", errorMessage);
                        });
                    }
                }
            });
        }

//        医生端登录
        else {
//            医生端登录
            if (phone.equals("") || pwd.equals("")) {
                Toast.makeText(LoginActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //          发送登录请求
            String doct_url = Base_URL + "/docts/login";
            RequestBody requestBody = new FormBody.Builder()
                    .add("doctPhone", phone)
                    .add("password", pwd)
                    .build();
            Request request = new Request.Builder()
                    .url(doct_url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 请求失败，处理异常
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 请求成功，处理响应
                    if (response.isSuccessful()) {
                        try {
//                            将数据进行解析
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            JSONObject dataObject = jsonObject.optJSONObject("data");
                            if (dataObject != null) {
                                JSONObject subDataObject = dataObject.optJSONObject("data");
                                if (subDataObject != null){
                                    int doctPhone = subDataObject.getInt("phone");
                                    String doctName = subDataObject.getString("name");
                                    String doctPassword = subDataObject.getString("pwd");
                                    if (code == 200) {
                                        // 登录成功，解析用户信息并跳转到主界面
                                        runOnUiThread(() -> {
                                            Intent intent = new Intent(LoginActivity.this, DocBaseActivity.class);
                                            intent.putExtra("doctPhone", doctPhone);
                                            intent.putExtra("doctName", doctName);
                                            intent.putExtra("doctPassword", doctPassword);
                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }else {
                                    subDataObject = new JSONObject();
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                                }
                            }else {
                                dataObject = new JSONObject();
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录失败，服务器返回数据错误", Toast.LENGTH_SHORT).show());
                        }
                    }
                    else {
                        // 处理错误响应
                        String errorMessage = response.message();
                        // 显示错误提示
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.d("理应显示响应错误信息:", errorMessage);
                        });
                    }
                }
            });
        }
    }
    public void gopwd(View view){
        Intent intent=new Intent(this, ForgetPwdActivity.class);
//        把用户类型传过去
        intent.putExtra("user_type", isPatient);
//        把相应用户输入的手机号数据传过去，只传数据的话不用考虑是哪种类型，因为使用者知道自己的类型，输入的手机号肯定在相应的类型中，只是密码忘了
        String phone=medtPho.getText().toString().trim();
        intent.putExtra("phone", phone);
        Log.d("传过去的手机号：", phone);
        startActivity(intent);
        finish();
    }
    public void goreg(View view){
        Intent intent=new Intent(this, RegisterActivity.class);
        intent.putExtra("user_type", isPatient);
        Log.d("传Boolean值","goreg: "+isPatient);
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