package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Doctors;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctKeyInfoActivity extends AppCompatActivity {
        private EditText mEditText1;
        private EditText mEditText2;
        private EditText mEditText3;
        private EditText mEditText4;
        private Button mButton;
        private String first;
        private String second;
        private String third;
        private String forth;
        private int fifth;
        private String six;

        @Override
        protected void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog_doctor_info);
                //取得启动该Activity的Intent对象
                Intent intent =getIntent();
//                        /*取出Intent中附加的数据*/
                first = intent.getStringExtra("doctPhone");
                second = intent.getStringExtra("doctName");
                third = intent.getStringExtra("doctNum");
                forth = intent.getStringExtra("doctSex");
                fifth = intent.getIntExtra("doctAge",0);
                six = intent.getStringExtra("doctAvatar");
                addKeyInfo();
        }

        private void addKeyInfo(){
                mEditText1=findViewById(R.id.et_doctor_hospital);
                mEditText2=findViewById(R.id.et_doctor_office);
                mEditText3=findViewById(R.id.et_doctor_title);
                mEditText4=findViewById(R.id.et_doctor_talent);
                mButton=findViewById(R.id.btn_submit);
                mButton.setOnClickListener(this::logHome);
        }

        private void logHome(View view) {
                OkHttpClient client = new OkHttpClient();
                String hospital=mEditText1.getText().toString().trim();
                String office=mEditText2.getText().toString().trim();
                String title=mEditText3.getText().toString().trim();
                String talent=mEditText4.getText().toString().trim();
                if (hospital.equals("") ||office.equals("")|| title.equals("")||talent.equals("")) {
                        Log.d("TAG", "Register: "+title);
                        Toast.makeText(DoctKeyInfoActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
                        return;
                }else {
                        String doct_url=Base_URL+"/docts/editKeyInfo";
                        Doctors doctors=new Doctors();
                        doctors.setDoctPhone(first);
                        doctors.setDoctOfHospital(hospital);
                        doctors.setDoctOfOffice(office);
                        doctors.setDoctTile(title);
                        doctors.setDoctTalent(talent);
                        String json = new Gson().toJson(doctors);
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
                                        runOnUiThread(() -> Toast.makeText(DoctKeyInfoActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show());
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
                                                                        Toast.makeText(DoctKeyInfoActivity.this, "完善成功，将进入主页！", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(DoctKeyInfoActivity.this, DocBaseActivity.class);
                                                                        intent.putExtra("doctPhone", first);
                                                                        intent.putExtra("doctName", second);
                                                                        intent.putExtra("doctNum", third);
                                                                        intent.putExtra("doctSex",forth);
                                                                        intent.putExtra("doctAge", fifth);
                                                                        intent.putExtra("doctAvatar", six);

                                                                        startActivity(intent);
                                                                } else {
                                                                        Toast.makeText(DoctKeyInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                                                                }
                                                        });
                                                } catch (JSONException | IOException e) {
                                                        e.printStackTrace();
                                                        runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                        Toast.makeText(DoctKeyInfoActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show();
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
                                                                Toast.makeText(DoctKeyInfoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                                Log.d("理应显示响应错误信息:", errorMessage);
                                                        }
                                                });
                                        }
                                }
                        });
                }

        }
}