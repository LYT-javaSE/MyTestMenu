package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.Users;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName;
    private EditText registerPhone;
    private EditText registerPassword;
    private EditText registerRePassword;
    private Button register;
    static final String Base_URL = "http://10.0.2.2:8080"; // 后端注册接口地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        performRegister();
//        register.setOnClickListener(v ->
//        {
//            String name=registerName.getText().toString().trim();
//            String phone = registerPhone.getText().toString().trim();
//            String password = registerPassword.getText().toString().trim();
//            String repassword = registerRePassword.getText().toString().trim();
//
//            if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
//                Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//            // 在注册页的代码中，获取传递的 Intent
//            Intent intent = getIntent();
//            if (intent != null) {
//                // 从 Intent 中获取用户类型参数
//                Boolean userType = intent.getBooleanExtra("user_type",true);
//                // 根据用户类型参数进行相应的处理
//                if (userType) {
//
//
//                    // 普通用户注册逻辑
//                    if(password.equals(repassword)){
//                    // 注册为用户，并保存到数据库
//                        List<Users> users = LitePal.findAll(Users.class);
//                        for (Users patient : users) {
//                            if (patient.getUserPhone().equals(phone)) {
//                                Toast.makeText(RegisterActivity.this, "账号不可以重复注册", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                        Users user = new Users();
//                        user.setUserName(name);
//                        user.setUserPhone(phone);
//                        user.setUserPassword(password);
//                        user.setUserRePassword(repassword);
//                        user.save();
//                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//                else{
//                    // 医生注册逻辑
//                    if(password.equals(repassword)){
//                    // 注册为用户，并保存到数据库
//                        List<Doctors> doctors = LitePal.findAll(Doctors.class);
//                        for (Doctors doctor : doctors) {
//                            if (doctor.getDoctPhone().equals(phone)) {
//                                Toast.makeText(RegisterActivity.this, "账号不可以重复注册", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                        Doctors doctor = new Doctors();
//                        doctor.setDoctName(name);
//                        doctor.setDoctPhone(phone);
//                        doctor.setDoctPassword(password);
//                        doctor.setDoctRePassword(repassword);
//                        doctor.save();
//                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//
//
//
//            }
//            else{
//                Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//                performRegister(name,phone,password,repassword)
//        );

    }
    public void performRegister(){
        registerName = findViewById(R.id.name_edit);
        registerPhone = findViewById(R.id.phone_edit);
        registerPassword = findViewById(R.id.password_edit);
        registerRePassword = findViewById(R.id.password_edit_re);
        register=findViewById(R.id.register_btn);
        register.setOnClickListener(v -> register());
    }

    public void register(){
        OkHttpClient client = new OkHttpClient();
        String name=registerName.getText().toString().trim();
        String phone=registerPhone.getText().toString().trim();
        String password=registerPassword.getText().toString().trim();
        String repassword=registerRePassword.getText().toString().trim();
        Intent intent = getIntent();
        if (intent != null) {
                // 从 Intent 中获取用户类型参数
            Boolean userType = intent.getBooleanExtra("user_type",true);
                // 根据用户类型参数进行相应的处理
            if (userType) {
//                用户注册
                Log.d("用户类型", "注册界面: "+userType);
                if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
                    Log.d("TAG", "Register: "+name);
                    Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
//                    // 构建请求体
//                    RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("username", name)
//                        .addFormDataPart("userphone", phone)
//                        .addFormDataPart("password", password)
//                        .addFormDataPart("repassword", repassword)
//                        .build();
//
//                    // 构建注册请求
//                    Request request = new Request.Builder()
//                        .url(REGISTER_URL)
//                        .post(requestBody)
//                        .addHeader("Content-Type", "multipart/form-data")
//                        .build();

                    // 创建用户对象
                    if (!password.equals(repassword)) {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String user_url = Base_URL + "/users/reg";
                        Users user = new Users();
                        user.setUserName(name);
                        user.setUserPhone(phone);
                        user.setUserPassword(password);
                        user.setUserRePassword(repassword);

                        // 将用户对象转换为 JSON 字符串
                        String json = new Gson().toJson(user);
                        // 设置请求体的 MediaType
                        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

                        // 创建 RequestBody 对象
                        RequestBody requestBody = RequestBody.create(mediaType, json);

                        // 构建 POST 请求
                        Request request = new Request.Builder()
                                .url(user_url)
                                .post(requestBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    // 响应成功
                                    try {
                                        JSONObject responseBody = new JSONObject(response.body().string());
                                        int code = responseBody.getInt("code");
                                        String message=responseBody.optString("message","未知错误");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (code==200) {
                                                    Toast.makeText(RegisterActivity.this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                                                    Log.d("显示响应成功信息:", "注册成功");
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                                    Log.d("理应显示响应成功但结果异常的信息:", message);
                                                    Log.d("显示响应成功信息:", String.valueOf(code));
                                                }
                                                startActivity(intent);
                                            }
                                        });
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                else {
                                    // 处理错误响应
                                    String errorMessage = response.message();
                                    // 显示错误提示
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            Log.d("理应显示响应错误信息:", errorMessage);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }else {
//                医生注册
                Log.d("用户类型", "注册界面: "+userType);
                if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
                    Log.d("TAG", "Register: "+name);
                    Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(!password.equals(repassword)){
                        Toast.makeText(RegisterActivity.this,"两次密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String doct_url = Base_URL + "/docts/reg";
                        Doctors doctor = new Doctors();
                        doctor.setDoctName(name);
                        doctor.setDoctPhone(phone);
                        doctor.setDoctPassword(password);
                        doctor.setDoctRePassword(repassword);
                        // 将用户对象转换为 JSON 字符串
                        String json = new Gson().toJson(doctor);
                        // 设置请求体的 MediaType
                        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                        // 创建 RequestBody 对象
                        RequestBody requestBody = RequestBody.create(mediaType, json);
                        // 构建 POST 请求
                        Request request = new Request.Builder()
                                .url(doct_url)
                                .post(requestBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(() ->
                                    Toast.makeText(RegisterActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show());
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject responseBody = new JSONObject(response.body().string());
                                        int code = responseBody.getInt("code");
                                        String message=responseBody.optString("message","未知错误");
                                        runOnUiThread(() -> {
                                            if (code==200) {
                                                Toast.makeText(RegisterActivity.this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                                                Log.d("显示响应成功信息:", "注册成功");
                                                Log.d("显示响应成功信息:", String.valueOf(code));
                                            } else {
                                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                                Log.d("理应显示响应成功但结果异常的信息:", message);
                                                Log.d("显示响应成功信息:", String.valueOf(code));
                                            }
                                            startActivity(intent);
                                        });
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        runOnUiThread(() -> {
                                            Toast.makeText(RegisterActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }else{
            runOnUiThread(() -> {
                Toast.makeText(RegisterActivity.this,"状态异常",Toast.LENGTH_SHORT).show();
            });
        }
    }
//    public void performRegister(String name,String phone,String password,String repassword){
//
////        OkHttpClient client = new OkHttpClient();
//
////        name = registerName.getText().toString().trim();
////        phone= registerPhone.getText().toString().trim();
////        password = registerPassword.getText().toString().trim();
////        repassword = registerRePassword.getText().toString().trim();
////        if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
////            Log.d("TAG", "performRegister: "+name);
////            Log.d("TAG", "performRegister: "+phone);
////            Log.d("TAG", "performRegister: "+password);
////            Log.d("TAG", "performRegister: "+repassword);
////            Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
////            return;
////        }
//        // 在注册页的代码中，获取传递的 Intent
//        Intent intent = getIntent();
//        if (intent != null) {
//                // 从 Intent 中获取用户类型参数
//        Boolean userType = intent.getBooleanExtra("user_type",true);
//                // 根据用户类型参数进行相应的处理
//        if (userType) {
//                    // 普通用户注册逻辑
////                    if(password.equals(repassword)){
////                    // 注册为用户，并保存到数据库
////                        List<Users> users = LitePal.findAll(Users.class);
////                        for (Users patient : users) {
////                            if (patient.getUserPhone().equals(phone)) {
////                                Toast.makeText(RegisterActivity.this, "账号不可以重复注册", Toast.LENGTH_SHORT).show();
////                                return;
////                            }
////                        }
////                        Users user = new Users();
////                        user.setUserName(name);
////                        user.setUserPhone(phone);
////                        user.setUserPassword(password);
////                        user.setUserRePassword(repassword);
////                        user.save();
////                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
////                    }
//            Log.d("用户类型", "Register方法: "+userType);
//            name = registerName.getText().toString().trim();
//            phone= registerPhone.getText().toString().trim();
//            password = registerPassword.getText().toString().trim();
//            repassword = registerRePassword.getText().toString().trim();
//            if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
//                Log.d("TAG", "performRegister: "+name);
//                Log.d("TAG", "performRegister: "+phone);
//                Log.d("TAG", "performRegister: "+password);
//                Log.d("TAG", "performRegister: "+repassword);
//                Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
//                return;
//            }else {
//                OkHttpClient client = new OkHttpClient();
//                // 构建请求体
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("username", name)
//                        .addFormDataPart("userphone", phone)
//                        .addFormDataPart("password", password)
//                        .addFormDataPart("repassword", repassword)
//                        .build();
//
//                // 构建注册请求
//                Request request = new Request.Builder()
//                        .url(REGISTER_URL)
//                        .post(requestBody)
//                        .build();
//
//                // 发送注册请求
//                client.newCall(request).enqueue(new Callback() {
////                    @Override
////                    public void onResponse() throws IOException {
////                        // 处理响应
////                        if (response.isSuccessful()) {
////                            String responseBody = response.body().string();
////                            try {
////                                JSONObject jsonObject = new JSONObject(responseBody);
////                                String result = jsonObject.getString("result");
////                                // 根据注册结果进行相应处理
////                                if ("success".equals(result)) {
////                                    // 注册成功，执行相应操作
////                                    runOnUiThread(() -> {
////                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                        // 注册成功后的操作
////                                    });
////                                } else {
////                                    // 注册失败，执行相应操作
////                                    runOnUiThread(() -> {
////                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
////                                        // 注册失败后的操作
////                                    });
////                                }
////                            } catch (JSONException e) {
////                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
////                                e.printStackTrace();
////                                throw new RuntimeException(e);
////                            }
////                        }
////                    }
////                    @Override
////                    public void onFailure(Request request, IOException e) {
////                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
////                        e.printStackTrace();
////                    }
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(RegisterActivity.this, "请求失败,请检查网络", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();                                    startActivity(intent);
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        }
//
//        else{
//                    // 医生注册逻辑
////                    if(password.equals(repassword)){
////                    // 注册为用户，并保存到数据库
////                        List<Doctors> doctors = LitePal.findAll(Doctors.class);
////                        for (Doctors doctor : doctors) {
////                            if (doctor.getDoctPhone().equals(phone)) {
////                                Toast.makeText(RegisterActivity.this, "账号不可以重复注册", Toast.LENGTH_SHORT).show();
////                                return;
////                            }
////                        }
////                        Doctors doctor = new Doctors();
////                        doctor.setDoctName(name);
////                        doctor.setDoctPhone(phone);
////                        doctor.setDoctPassword(password);
////                        doctor.setDoctRePassword(repassword);
////                        doctor.save();
////                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
////                    }
//
//
//
//
//
//        }
//
//        }
////        else{
////            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
////                return;
////        }
//    }

    public void goback(View view){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}