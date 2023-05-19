package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.Users;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddDoctMsgActivity extends AppCompatActivity{

    private TextView mTextView1;
    private EditText mTextView2;
    private EditText mTextView3;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private String name;
    private String mName;
    private String num;
    private String sex;
    private int age;
    private int mAge;
    private Button mButton;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doct_msg);


        radioButton1=findViewById(R.id.radioButtonMale);
        radioButton2=findViewById(R.id.radioButtonFemale);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());

        mButton=findViewById(R.id.buttonSave);
        mTextView1=findViewById(R.id.tv_num);
        mTextView2=findViewById(R.id.tv_edit_name);
        mTextView3=findViewById(R.id.tv_edit_age);
        // 使用Intent对象得到传过来的参数
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        num = intent.getStringExtra("num");
        phone = intent.getStringExtra("phone");
        sex = intent.getStringExtra("sex");
        age = intent.getIntExtra("age",0);
        if (!sex.equals("")) {
            if (sex.equals("男")) {
                radioButton1.setChecked(true); // 让男性单选圈亮起
            } else {
                radioButton2.setChecked(true); // 让女性单选圈亮起
            }
        }
        mTextView1.setText(num);
        mTextView2.setText(name);
        mTextView3.setText(String.valueOf(age));

        radioGroup = findViewById(R.id.radioGroupGender);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
            sex = radioButton.getText().toString();
            Log.d("TAG", "选择的性别为: " + sex);
        });

        mButton.setOnClickListener(this::goUpdate);
    }

    private void goUpdate(View view) {
        OkHttpClient client = new OkHttpClient();
        String user_url = Base_URL + "/docts/editInfo";
        mName = mTextView2.getText().toString().trim();
        mAge= Integer.parseInt(mTextView3.getText().toString().trim());
        Doctors doctor = new Doctors();
        doctor.setDoctPhone(phone);
        doctor.setDoctName(mName);
        doctor.setDoctAge(mAge);
        doctor.setDoctSex(sex);

        // 将用户对象转换为 JSON 字符串
        String json = new Gson().toJson(doctor);
        // 设置请求体的 MediaType
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        // 创建 RequestBody 对象
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(user_url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(AddDoctMsgActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 响应成功
                    try {
                        JSONObject responseBody = new JSONObject(response.body().string());
                        int code = responseBody.getInt("code");
                        String message = responseBody.optString("message", "未知错误");
                        runOnUiThread(() -> {
                            if (code == 200) {
                                Toast.makeText(AddDoctMsgActivity.this, "完善成功！", Toast.LENGTH_SHORT).show();
//                                跳到DoctMineFragment最好

                            } else {
                                Toast.makeText(AddDoctMsgActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(AddDoctMsgActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // 处理错误响应
                    String errorMessage = response.message();
                    // 显示错误提示
                    runOnUiThread(() -> {
                        Toast.makeText(AddDoctMsgActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

//    private void goUpperPage(){
//        Intent intent = new Intent(AddDoctMsgActivity.this, DocBaseActivity.class);
//        startActivity(intent);
//    }
}