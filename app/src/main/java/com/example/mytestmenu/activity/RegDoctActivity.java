package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.RegDoctAdapter;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.Offices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegDoctActivity extends AppCompatActivity implements RegDoctAdapter.ItemClickListener {

    private RecyclerView mRV;
    private RegDoctAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Doctors> mDoctors = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_doctor);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());

        mRV = findViewById(R.id.rvSurgeonList);
        mRV.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLayoutManager);

        mAdapter = new RegDoctAdapter(mDoctors,this);
        mRV.setAdapter(mAdapter);

        initView();
//        进入此页面，理论上调用接口去获得当前登录者的信息，获取userPhone{最重要}
//        acquirePhone();
    }

    public void initView(){
        OkHttpClient client = new OkHttpClient();
        String user_url = Base_URL + "/users/showDoctor";
        Intent intent = getIntent();
        String off_name = intent.getStringExtra("office_name");
        String hos_name = intent.getStringExtra("hospital_name");
        Log.d("传过来的科室名", off_name);
        Log.d("传过来的医院名", hos_name);
//      应该是向后端传值,再接收传过来的数据
//      构造请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("hospName", hos_name)
                .add("offName", off_name)
                .build();
//      构造请求
        Request request = new Request.Builder()
                .url(user_url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Log.d("打印响应体", jsonStr);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jsonArray = jsonObject.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Doctors> doctors = new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<List<Doctors>>(){}.getType());

                for (Doctors doctor : doctors) {
                    Log.d("打印列表项(共5项含图片)：", "doctName = " + doctor.getDoctName());
                    Log.d("打印列表项(共5项含图片)：", "doctOfHospital = " + doctor.getDoctOfHospital());
                    Log.d("打印列表项(共5项含图片)：", "doctOfOffice = " + doctor.getDoctOfOffice());
                    Log.d("打印列表项(共5项含图片)：", "doctTitle = " + doctor.getDoctTile());
                    Log.d("打印列表项(共5项含图片)：", "doctAvatar = " + doctor.getDoctAvatar());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDoctors.clear();
                        mDoctors.addAll(doctors);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    @Override
    public void onItemClick(Doctors doctor) {
        Intent intent1 = getIntent();
        String u_phone=intent1.getStringExtra("u_phone");

        Intent intent = new Intent(this, RegFinalActivity.class);
        intent.putExtra("office_name", doctor.getDoctOfOffice());
        intent.putExtra("hospital_name",doctor.getDoctOfHospital());
        intent.putExtra("doctor_name", doctor.getDoctName());
//点击这个按钮就把userPhone的值传到下个预约界面，
        intent.putExtra("userPhone",u_phone);
        startActivity(intent);
    }
}