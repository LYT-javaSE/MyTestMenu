package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
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
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddMsgActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mTextView1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    TextView information_birthday;
    private Date birthDate;
    private SimpleDateFormat sdf;
    private String birthday;
    private Button mButton;
    private String name;
    private String mName;
    private String sex;
    private int age;
    private String  phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_msg);


        radioButton1=findViewById(R.id.radioButtonMale);
        radioButton2=findViewById(R.id.radioButtonFemale);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());

        mButton=findViewById(R.id.buttonSave);
        mTextView1=findViewById(R.id.editTextNickname);
        // 使用Intent对象得到传过来的参数
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        sex = intent.getStringExtra("sex");
        if (!sex.equals("")) {
            if (sex.equals("男")) {
                radioButton1.setChecked(true); // 让男性单选圈亮起
            } else {
                radioButton2.setChecked(true); // 让女性单选圈亮起
            }
        }
        String birth = intent.getStringExtra("birth");
        mTextView1.setText(name);

            Log.d("CCCC", "onCreate: " + birth);
            information_birthday = findViewById(R.id.text_birthday);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.getDefault());
            Date date = null;
            try {
                date = inputFormat.parse(birth);
                Log.d("DDDD", "onCreate: " + date);
            } catch (ParseException e) {
                Log.d("TAG", "onCreate: "+date);
                date=new Date();
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String outputDate = sdf1.format(date);
            Log.d("EEEE", "onCreate: " + outputDate);
            information_birthday.setText(outputDate);


        information_birthday.setOnClickListener(v->{
                Calendar calendar = Calendar.getInstance();//获取Calendar实例
                //创建日期选择器
                DatePickerDialog dialog = new DatePickerDialog(this,this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MARCH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();//窗口弹出
        });

        radioGroup = findViewById(R.id.radioGroupGender);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
            sex = radioButton.getText().toString();
            Log.d("TAG", "选择的性别为: " + sex);
        });

        mButton.setOnClickListener(this::goUpdate);
    }



    //日期选择完事件,实现onDateSet方法，作为待会日期选择完要执行的事件
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        birthday = String.format("%d-%02d-%02d",year,month+1,dayOfMonth);
        information_birthday.setText(birthday);//设置生日
        // 计算年龄
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            birthDate = sdf.parse(birthday);
            long currentTime = System.currentTimeMillis();
            age = (int) ((currentTime - birthDate.getTime()) / (1000L * 60 * 60 * 24 * 365));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



    private void goUpdate(View view) {
        OkHttpClient client = new OkHttpClient();
        String user_url = Base_URL + "/users/editInfo";
        mName = mTextView1.getText().toString().trim();
        Users user = new Users();
        user.setUserPhone(phone);
        user.setUserName(mName);
        user.setUserAge(age);
        user.setUserSex(sex);
        user.setBirthDay(birthday);

        // 将用户对象转换为 JSON 字符串
        String json = new Gson().toJson(user);
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
                runOnUiThread(() -> Toast.makeText(AddMsgActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT).show());
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
                                Toast.makeText(AddMsgActivity.this, "完善成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddMsgActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(AddMsgActivity.this, "解析响应体出错", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // 处理错误响应
                    String errorMessage = response.message();
                    // 显示错误提示
                    runOnUiThread(() -> {
                        Toast.makeText(AddMsgActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}