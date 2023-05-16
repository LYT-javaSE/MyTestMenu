package com.example.mytestmenu.fragment;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddDoctMsgActivity;
import com.example.mytestmenu.activity.AddMsgActivity;
import com.example.mytestmenu.activity.LoginActivity;
import com.example.mytestmenu.entity_class.Avatar;
import com.example.mytestmenu.entity_class.Doctors;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocMineFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView mTextView1;
    private String phone;
    private String name;

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doc_mine, container, false);

        avatarImageView = view.findViewById(R.id.avatar_image1);
        avatarImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST_CODE);
        });

        Button mBtn=view.findViewById(R.id.edit_profile_button);
        Button mBtn2=view.findViewById(R.id.logout_button);
        mTextView1=view.findViewById(R.id.doctName_text_view);
        TextView mTextView2=view.findViewById(R.id.doctPhone_text_view);
        //接收数据并用textview控件的setText()方法改写text内容，传递的数据是name
        Bundle bundle=getArguments();
        name=bundle.getString("name");
        phone=bundle.getString("phone");
        String num=bundle.getString("num");
        String sex=bundle.getString("sex");
        int age=bundle.getInt("age");
        String avatar_path=bundle.getString("avatar");
        Log.d("avatarPATH:", "onCreateView: "+avatar_path);
        mTextView1.setText(name);
        mTextView2.setText(phone);

        if (avatar_path != null) {
            Glide.with(this).load(avatar_path).into(avatarImageView);
        }
        mBtn.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AddDoctMsgActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("num",num);
            intent.putExtra("sex",sex);
            intent.putExtra("age",age);
            getActivity().startActivity(intent);
        });

        mBtn2.setOnClickListener(v -> {
            SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // 当从其他页面返回到此页面时，重新填充数据
        fillData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            String realFilePath = getRealFilePath(uri);
            // update user's avatar on frontend
            Glide.with(this).load(realFilePath).into(avatarImageView);
            // get file name and path
            String fileName = uri.getLastPathSegment();
            String filePath = uri.getPath();
            // save to database
            Avatar avatar = new Avatar();
            avatar.setFileName(fileName);
            avatar.setFilePath(filePath);
            avatar.save();
            Log.d("ZZZ", "onActivityResult: "+filePath);

            // send image path to server
            OkHttpClient client = new OkHttpClient();
            String doct_url = Base_URL + "/docts/editAvatar";
            RequestBody requestBody = new FormBody.Builder()
                    .add("avatar_path", filePath)
                    .add("phone",phone)
                    .build();
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
                    String responseText = response.body().string();
                    Log.d("ZZZ", "后端传过来的code信息: " + responseText);
                }
            });

        }
    }

    private String getRealFilePath(Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }


    private void fillData(){
        OkHttpClient client = new OkHttpClient();
        String doct_url = Base_URL + "/docts/getLoginInfo";
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
                        int code = responseBody.getInt("code");
                        JSONObject subDataObject = responseBody.optJSONObject("data");

                        Log.d("vvvvvvv", "onResponse: "+subDataObject);
                        String doctName = subDataObject.getString("doctName");
//                        头像也要传一下吧
                        String avatarPath = subDataObject.getString("doctAvatar");
                        getActivity().runOnUiThread(() -> {
                            if (code == 200) {
                                Log.d("TAG", "onResponse: " + code);
                                mTextView1.setText(doctName);
                                if (avatarPath != null) {
                                    Glide.with(DocMineFragment.this).load(avatarPath).into(avatarImageView);
                                    Log.d("TAG%%%%%%", "success");
                                }
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