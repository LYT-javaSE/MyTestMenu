package com.example.mytestmenu.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestmenu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mbtn1;
    private Button mbtn2;
    private EditText et_password_first; // 声明一个编辑框对象
    private EditText et_password_second; // 声明一个编辑框对象
    private EditText et_verifycode; // 声明一个编辑框对象
    private String mVerifyCode; // 验证码
    private String mPhone; // 手机号码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        editPwd();
    }

    public void editPwd(){
        mbtn1=findViewById(R.id.code_button);
        mbtn2=findViewById(R.id.reset_password_button);
        et_password_first=findViewById(R.id.pwd_text);
        et_password_second=findViewById(R.id.repwd_text);
        et_verifycode=findViewById(R.id.code_text);
        mbtn1.setOnClickListener(this);
        mbtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OkHttpClient client = new OkHttpClient();
        Intent intent = getIntent();
        if (intent != null) {
            // 从 Intent 中获取用户类型参数
            Boolean userType = intent.getBooleanExtra("user_type", true);
            // 从上一个页面获取要修改密码的手机号码
            mPhone = intent.getStringExtra("phone");
            Log.d("传过来的手机号：", mPhone);
            // 根据用户类型参数进行相应的处理
            if (userType) {
//                用户修改密码
                Log.d("用户类型", "重置密码界面: " + userType);
                if (v.getId() == R.id.code_button) { // 点击了“获取验证码”按钮
                    // 生成六位随机数字的验证码
                    mVerifyCode = String.format("%06d", new Random().nextInt(999999));
                    // 以下弹出提醒对话框，提示用户记住六位验证码数字
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请记住验证码");
                    builder.setMessage("手机号" + mPhone + "，本次验证码是" + mVerifyCode + "，请输入验证码");
                    builder.setPositiveButton("好的", null);
                    AlertDialog alert = builder.create();
                    alert.show(); // 显示提醒对话框
                } else if (v.getId() == R.id.reset_password_button) { // 点击了“重置密码”按钮
                    String password_first = et_password_first.getText().toString();
                    String password_second = et_password_second.getText().toString();
                    if (password_first.equals("") || password_second.equals("")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"输入内容不许为空", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    if (!password_first.equals(password_second)) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    if (!et_verifycode.getText().toString().equals(mVerifyCode)) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                            //                        与后端进行交互
                            String user_url = Base_URL + "/users/editPwd";
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("userPhone", mPhone)
                                    .add("password", password_first)
                                    .add("conPassword",password_second)
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
                                    runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, "修改失败，请检查网络连接", Toast.LENGTH_SHORT).show());
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        // 响应成功
                                        try {
                                            JSONObject responseBody = new JSONObject(response.body().string());
                                            int code = responseBody.getInt("code");
                                            String message=responseBody.optString("message","未知错误");
                                            runOnUiThread(() -> {
                                                if (code==200) {
                                                    Toast.makeText(ForgetPwdActivity.this, "重置成功，请登录！", Toast.LENGTH_SHORT).show();
                                                    Log.d("显示响应成功信息:", "修改密码成功");
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                } else {
                                                    Toast.makeText(ForgetPwdActivity.this, message, Toast.LENGTH_SHORT).show();
                                                    Log.d("理应显示响应成功但结果异常的信息:", message);
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                }
                                            });
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show());
                                        }
                                    }
                                    else {
                                        // 处理错误响应
                                        String errorMessage = response.message();
                                        // 显示错误提示
                                        runOnUiThread(() -> {
                                            Toast.makeText(ForgetPwdActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            Log.d("理应显示响应错误信息:", errorMessage);
                                        });
                                    }
                                }
                        });
                    }
                }
            } else {
                // 医生修改密码
                Log.d("用户类型", "注册界面: " + userType);
                if (v.getId() == R.id.code_button) { // 点击了“获取验证码”按钮
                    // 生成六位随机数字的验证码
                    mVerifyCode = String.format("%06d", new Random().nextInt(999999));
                    // 以下弹出提醒对话框，提示用户记住六位验证码数字
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请记住验证码");
                    builder.setMessage("手机号" + mPhone + "，本次验证码是" + mVerifyCode + "，请输入验证码");
                    builder.setPositiveButton("好的", null);
                    AlertDialog alert = builder.create();
                    alert.show(); // 显示提醒对话框

                } else if (v.getId() == R.id.reset_password_button) { // 点击了“重置密码”按钮
                    String password_first = et_password_first.getText().toString();
                    String password_second = et_password_second.getText().toString();
                    if (password_first.equals("") || password_second.equals("")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"输入内容不许为空", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    if (!password_first.equals(password_second)) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    if (!et_verifycode.getText().toString().equals(mVerifyCode)) {
                        runOnUiThread(() -> {
                            Toast.makeText(this,"请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            //                        与后端进行交互
                            String doct_url = Base_URL + "/docts/editPwd";
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("doctPhone", mPhone)
                                    .add("password", password_first)
                                    .add("conPassword",password_second)
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
                                    runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, "修改失败，请检查网络连接", Toast.LENGTH_SHORT).show());
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        // 响应成功
                                        try {
                                            JSONObject responseBody = new JSONObject(response.body().string());
                                            int code = responseBody.getInt("code");
                                            String message=responseBody.optString("message","未知错误");
                                            runOnUiThread(() -> {
                                                if (code==200) {
                                                    Toast.makeText(ForgetPwdActivity.this, "重置成功，请登录！", Toast.LENGTH_SHORT).show();
                                                    Log.d("显示响应成功信息:", "修改密码成功");
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                } else {
                                                    Toast.makeText(ForgetPwdActivity.this, message, Toast.LENGTH_SHORT).show();
                                                    Log.d("理应显示响应成功但结果异常的信息:", message);
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                }
                                            });
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            runOnUiThread(() -> Toast.makeText(ForgetPwdActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show());
                                        }
                                    }
                                    else {
                                        // 处理错误响应
                                        String errorMessage = response.message();
                                        // 显示错误提示
                                        runOnUiThread(() -> {
                                            Toast.makeText(ForgetPwdActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            Log.d("理应显示响应错误信息:", errorMessage);
                                        });
                                    }
                                }
                            });
                        });
                    }
                }
            }
        }
    }

    public void goback(View view){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}