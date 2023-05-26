package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.RecordAdapter;
import com.example.mytestmenu.utils.RecordData;
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

public class ShowForDoctActivity extends AppCompatActivity implements RecordAdapter.ItemClickListener{

    private String first,second;
    private RecyclerView mRV;
    private RecordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<RecordData> mRecordData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_for_doct);

        mRV = findViewById(R.id.recycler_view_record);
        mRV.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLayoutManager);

        mAdapter = new RecordAdapter(mRecordData, this);
        mRV.setAdapter(mAdapter);

        Intent intent =getIntent();
        /*取出Intent中附加的数据*/
        first= intent.getStringExtra("name");
        second = intent.getStringExtra("num");
        Log.d("TAG", "onCreate: "+first);
        Log.d("TAG", "onCreate: "+second);
        initView();
    }

    private void initView() {
        OkHttpClient client = new OkHttpClient();
        String doct_url = Base_URL + "/docts/showGuahao";
//      应该是向后端传值,再接收传过来的数据
//      构造请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("name", first)
                .add("num", second)
                .build();
//      构造请求
        Request request = new Request.Builder()
                .url(doct_url)
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
                List<RecordData> recordData = new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<List<RecordData>>(){}.getType());
                runOnUiThread(() -> {
                    mRecordData.clear();
                    mRecordData.addAll(recordData);
                    mAdapter.notifyDataSetChanged();
                });
            }
        });
    }
    //实现接口方法，跳转到医生列表页面
    public void onItemClick(RecordData record) {

    }
}