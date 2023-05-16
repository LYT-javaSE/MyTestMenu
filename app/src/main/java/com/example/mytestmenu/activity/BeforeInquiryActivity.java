package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.RegDoctAdapter;
import com.example.mytestmenu.entity_class.Doctors;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BeforeInquiryActivity extends AppCompatActivity implements RegDoctAdapter.ItemClickListener {

    private RecyclerView mRV;
    private RegDoctAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Doctors> mDoctors = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_inquiry);

        mRV = findViewById(R.id.rvDoctorList);
        mRV.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLayoutManager);

        mAdapter = new RegDoctAdapter(mDoctors,this);
        mRV.setAdapter(mAdapter);

        initView();

    }

    private void initView(){
        String user_url = Base_URL + "/users/doctorList";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(user_url)
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
                    Log.d("打印列表项(共5项含图片,先不含图)：", "doctName = " + doctor.getDoctName());
                    Log.d("打印列表项(共5项含图片,先不含图)：", "doctOfHospital = " + doctor.getDoctOfHospital());
                    Log.d("打印列表项(共5项含图片,先不含图)：", "doctOfOffice = " + doctor.getDoctOfOffice());
                    Log.d("打印列表项(共5项含图片,先不含图)：", "doctTitle = " + doctor.getDoctTile());
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
        Intent intent = new Intent(this, InquiryActivity.class);

        intent.putExtra("office_name", doctor.getDoctOfOffice());
        intent.putExtra("hospital_name",doctor.getDoctOfHospital());
        intent.putExtra("doctor_name", doctor.getDoctName());

        startActivity(intent);
    }
}