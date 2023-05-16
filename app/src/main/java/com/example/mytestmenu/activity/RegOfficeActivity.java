package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.RegHosAdapter;
import com.example.mytestmenu.adapter.RegOffAdapter;
import com.example.mytestmenu.entity_class.Hospitals;
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

public class RegOfficeActivity extends AppCompatActivity implements RegOffAdapter.ItemClickListener {
    private RecyclerView mRV;
    private RegOffAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Offices> mOffices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_office);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());

        mRV = findViewById(R.id.recycler_view_office);
        mRV.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLayoutManager);

        mAdapter = new RegOffAdapter(mOffices,this);
        mRV.setAdapter(mAdapter);

        initView();
    }

    private void initView() {
        OkHttpClient client = new OkHttpClient();
        String user_url = Base_URL + "/users/showOffice";
//        获取上个页面传过来的医院id,也不一定非要id，可以是其他的，以便查询对应的科室名称
        Intent intent = getIntent();
        String hos_n = intent.getStringExtra("hospital_name");
        String hos_a = intent.getStringExtra("hospital_address");
        Log.d("传过来的name", hos_n);
        Log.d("传过来的address", hos_a);
//      应该是向后端传值,再接收传过来的数据
//      构造请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("name", hos_n)
                .add("address", hos_a)
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
                List<Offices> offices = new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<List<Offices>>(){}.getType());

                for (Offices off : offices) {
                    Log.d("打印列表项：", "officeName = " + off.getOffice());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOffices.clear();
                        mOffices.addAll(offices);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //实现接口方法，跳转到医生列表页面
    @Override
    public void onItemClick(Offices office) {
        Intent intent1 = getIntent();
        String hos_n = intent1.getStringExtra("hospital_name");
        String u_phone=intent1.getStringExtra("u_phone");
        Intent intent = new Intent(this, RegDoctActivity.class);
//        传递科室给医生列表页面
        intent.putExtra("office_name", office.getOffice());
//       传递到医生列表界面
        intent.putExtra("hospital_name",hos_n);
        intent.putExtra("u_phone",u_phone);
        startActivity(intent);
    }
}