package com.example.mytestmenu.fragment;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddMsgActivity;
import com.example.mytestmenu.activity.LoginActivity;
import com.example.mytestmenu.entity_class.MsgContent;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineFragment extends Fragment {
    private String phone;
    private TextView mTextView1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV=inflater.inflate(R.layout.fragment_mine, container, false);

        Button mBtn=mV.findViewById(R.id.edit_profile_button);
        Button mBtn2=mV.findViewById(R.id.logout_button);

        //在frament文件中的onCreateView方法中接收数据并显示
        //声明并初始化一个textview
        mTextView1=mV.findViewById(R.id.username_text_view);
        TextView mTextView2=mV.findViewById(R.id.phone_text_view);
        //接收数据并用textview控件的setText()方法改写text内容，传递的数据是name
        Bundle bundle=getArguments();

        String name=bundle.getString("name");
        phone=bundle.getString("phone");
        int age=bundle.getInt("age");
        String sex=bundle.getString("sex");
        String birth=bundle.getString("birth");
        mTextView1.setText(name);
        mTextView2.setText(phone);

        mBtn.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AddMsgActivity.class);
//            LitePal.deleteAll(MsgContent.class,"id>?","1");
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("age",age);
            intent.putExtra("sex",sex);
            intent.putExtra("birth",birth);
            getActivity().startActivity(intent);
        });

        mBtn2.setOnClickListener(v -> {
//                SharedPreferences sp = getActivity().getSharedPreferences("login_status", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.clear();
//                editor.apply();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return mV;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当从其他页面返回到此页面时，重新填充数据
        fillData();
    }

    private void fillData(){
        OkHttpClient client = new OkHttpClient();
        String doct_url = Base_URL + "/users/getLoginInfo";
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .build();
//      构造请求
        Request request = new Request.Builder()
                .url(doct_url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "网络请求失败，请重试", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseBody = new JSONObject(response.body().string());
                    Log.d("vvv", "onResponse: "+responseBody);
                    int code = responseBody.getInt("code");
                    JSONObject subDataObject = responseBody.optJSONObject("data");
                    Log.d("vvvvvvv", "onResponse: "+subDataObject);
                    String userName = subDataObject.getString("userName");
                    getActivity().runOnUiThread(() -> {
                        if (code == 200) {
                            Log.d("TAG", "onResponse: " + code);
                            mTextView1.setText(userName);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "解析响应体出错", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
}