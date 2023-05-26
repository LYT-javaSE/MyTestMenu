package com.example.mytestmenu.fragment;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.ChatActivity;
import com.example.mytestmenu.adapter.DealUserMsgAdapter;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.Users;
import com.example.mytestmenu.utils.ActivityCollector;
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

public class DocItemFragment extends Fragment implements DealUserMsgAdapter.ItemClickListener{

    private DealUserMsgAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Users> mUsers = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_doc_item, container, false);

        recyclerView = v.findViewById(R.id.user_list_recycview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new DealUserMsgAdapter(mUsers, this);
        recyclerView.setAdapter(adapter);

        initView();
        return v;
    }

    private void initView() {
//get方法
        OkHttpClient client = new OkHttpClient();
        String doct_url = Base_URL + "/docts/showUsers";
        Request request = new Request.Builder()
                .url(doct_url)
                .build();
//        Bundle bundle=getArguments();
//        String hos_n=bundle.getString("name");
//        String hos_a=bundle.getString("phone");
//        Log.d("传过来的name", hos_n);
//        Log.d("传过来的address", hos_a);
////      应该是向后端传值,再接收传过来的数据
////      构造请求体
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("name", hos_n)
//                    .add("address", hos_a)
//                    .build();
////      构造请求
//            Request request = new Request.Builder()
//                    .url(user_url)
//                    .post(requestBody)
//                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();
                    JSONArray jsonArray = null;
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        jsonArray = jsonObject.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<Users> users = new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<List<Users>>(){}.getType());

                    getActivity().runOnUiThread(() -> {
                        mUsers.clear();
                        mUsers.addAll(users);
                        adapter.notifyDataSetChanged();
                    });
                }
            });
    }
    @Override
    public void onItemClick(Users user) {
        Intent intent = new Intent(getContext(), ChatActivity.class);

        intent.putExtra("user_name", user.getUserName());
        intent.putExtra("user_phone", user.getUserPhone());

        startActivity(intent);

//        加入监测集合，启动时即监测
        Activity currentActivity = (Activity) requireContext();
        ActivityCollector.addActivity(currentActivity, ChatActivity.class);


    }
}