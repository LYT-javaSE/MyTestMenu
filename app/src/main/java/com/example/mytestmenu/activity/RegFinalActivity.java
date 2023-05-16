package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Guahao;
import com.example.mytestmenu.entity_class.Users;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//本activity是创建挂号信息，填写基本信息，选择预约时间
public class RegFinalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    TextView information_today;
    EditText nameIpt;
    EditText ageIpt;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String sex;
    private String dateIpt;
    EditText phoneIpt;
    private Button mbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_final);

        nameIpt=findViewById(R.id.etName);
        ageIpt=findViewById(R.id.etAge);
        phoneIpt=findViewById(R.id.etPhone);
        phoneIpt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 判断手机号格式是否正确，不正确则弹出提示框
                if (!isMobilePhone(s.toString())) {
                    phoneIpt.setError("手机号格式不正确");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
            // 判断手机号格式是否正确的方法
            public boolean isMobilePhone(String phone) {
                String regex = "^1[34578]\\d{9}$";
                return phone.matches(regex);
            }
        });
        mbtn=findViewById(R.id.btnReg);

//        当前医生挂号余量充足，则挂号成功，否则挂号失败
//        每人每天只能挂一个医生的号（待实现）
//        点击挂号按钮，生成挂号记录（医生未处理，表示待处理状态，处理后表示已处理）

        information_today = findViewById(R.id.text_today);
        information_today.setOnClickListener(v->{
            Calendar calendar = Calendar.getInstance();//获取Calendar实例
            //创建日期选择器
            DatePickerDialog dialog = new DatePickerDialog(this,this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MARCH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();//窗口弹出
        });

        radioGroup = findViewById(R.id.radioGroupGender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                sex = radioButton.getText().toString();
                Log.d("TAG", "选择的性别为: " + sex);
            }
        });

        mbtn.setOnClickListener(this::goSuccess);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateIpt = String.format("%d-%02d-%02d",year,month+1,dayOfMonth);
        information_today.setText(dateIpt);//设置预约日期
    }
    private void goSuccess(View view) {
        OkHttpClient client = new OkHttpClient();
//        向后端传值，创建挂号表
//        能否直接得到当前登录者的手机号码
        String name=nameIpt.getText().toString().trim();
        int age= Integer.parseInt(ageIpt.getText().toString().trim());
        String phone=phoneIpt.getText().toString().trim();
        Intent intent = getIntent();
        String off_name = intent.getStringExtra("office_name");
        String hos_name = intent.getStringExtra("hospital_name");
        String doc_name = intent.getStringExtra("doctor_name");
        String u_phone = intent.getStringExtra("userPhone");

        String user_url = Base_URL + "/users/createReg";
        Guahao guahao = new Guahao();
        guahao.setPatientName(name);
        guahao.setAge(age);
        guahao.setPhone(phone);
        guahao.setOffice(off_name);
        guahao.setHospital(hos_name);
        guahao.setDoctorName(doc_name);
        guahao.setSex(sex);
        guahao.setRegisterDate(dateIpt);
        guahao.setUserPhone(u_phone);

        // 将用户对象转换为 JSON 字符串
        String json = new Gson().toJson(guahao);
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
                runOnUiThread(() -> Toast.makeText(RegFinalActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show());
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
                                Toast.makeText(RegFinalActivity.this, "挂号成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegFinalActivity.this, message, Toast.LENGTH_SHORT).show();
                                Log.d("理应显示响应成功但结果异常的信息:", message);
                            }
                            Intent intent1 = new Intent(RegFinalActivity.this, RegSuccessActivity.class);
                            startActivity(intent1);
                        });
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(RegFinalActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show());
                    }
                }
                else {
                    // 处理错误响应
                    String errorMessage = response.message();
                    // 显示错误提示
                    runOnUiThread(() -> {
                        Toast.makeText(RegFinalActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.d("理应显示响应错误信息:", errorMessage);
                    });
                }
            }
        });
    }
}