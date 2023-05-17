package com.example.mytestmenu.fragment;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        String avatarPath=bundle.getString("avatar");

        mTextView1.setText(name);
        mTextView2.setText(phone);

        Log.d("ZZZPPPOOO", "onActivityResult: "+avatarPath);
        String filePath = getRealPathFromUri(Uri.parse(avatarPath));
        Log.d("ZZZ", "change: "+Uri.parse(avatarPath));
        Log.d("ZZZ", "Result: "+filePath);

        // 使用Glide加载图片
        Glide.with(requireContext())
                .load(filePath)
                .placeholder(R.drawable.yiliao)
                .into(avatarImageView);

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
//    @Override
//    public void onResume() {
//        super.onResume();
//        // 当从其他页面返回到此页面时，重新填充数据
//        fillData();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                String imagePath = getRealPathFromUri(selectedImageUri);
                if (imagePath != null) {
                    // 使用获取到的图片路径进行操作，例如显示图片或上传到服务器
                    Glide.with(requireContext())
                            .load(imagePath)
                            .placeholder(R.drawable.yiliao) // 设置默认占位图
                            .into(avatarImageView);
                }
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            String realFilePath = getRealPathFromUri(uri);
//            // update user's avatar on frontend
//            Glide.with(this).load(realFilePath).into(avatarImageView);
//            // get file name and path
//            String fileName = uri.getLastPathSegment();
//            String filePath = uri.getPath();
//            // save to database
//            Avatar avatar = new Avatar();
//            avatar.setFileName(fileName);
//            avatar.setFilePath(filePath);
//            avatar.save();
//            Log.d("ZZZ", "onActivityResult: "+filePath);
//
//            // send image path to server
//            OkHttpClient client = new OkHttpClient();
//            String doct_url = Base_URL + "/docts/editAvatar";
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("avatar_path", filePath)
//                    .add("phone",phone)
//                    .build();
//            Request request = new Request.Builder()
//                    .url(doct_url)
//                    .post(requestBody)
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String responseText = response.body().string();
//                    Log.d("ZZZ", "后端传过来的code信息: " + responseText);
//                }
//            });
//
//        }
//    }

    private String getRealPathFromUri(Uri uri) {
        String realPath = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                realPath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            realPath = uri.getPath();
        }
        return realPath;
    }

//    private String getRealPathFromUri(Uri uri) {
//        String realPath = null;
//        if (DocumentsContract.isDocumentUri(requireContext(), uri)) {
//            // 如果是文档类型的 URI
//            String documentId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                // MediaProvider
//                String id = documentId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=?";
//                String[] selectionArgs = { id };
//                realPath = getDataColumn(requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                // DownloadsProvider
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
//                realPath = getDataColumn(requireContext(), contentUri, null, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            // 如果是 content 类型的 URI
//            realPath = getDataColumn(requireContext(), uri, null, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            // 如果是 file 类型的 URI
//            realPath = uri.getPath();
//        }
//        return realPath;
//    }
//
//    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                return cursor.getString(columnIndex);
//            }
//        }
//        return null;
//    }




//    public String getRealPathFromUri(Uri uri) {
//        String realPath = null;
//        if (uri.getScheme().equals("content")) {
//            try {
//                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
//                File file = createFileFromInputStream(inputStream);
//                if (file != null) {
//                    realPath = file.getAbsolutePath();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (uri.getScheme().equals("file")) {
//            realPath = uri.getPath();
//        }
//        return realPath;
//    }
//
//    private File createFileFromInputStream(InputStream inputStream) {
//        try {
//            File file = new File(requireContext().getCacheDir(), "temp_image.jpg");
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, length);
//            }
//            outputStream.close();
//            inputStream.close();
//            return file;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }




//    private String getRealPathFromUri(Uri uri) {
//        String imagePath = null;
//        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
//            // 处理Document类型的URI
//            String documentId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = documentId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
//                imagePath = getImagePath(contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            // 处理Content类型的URI
//            imagePath = getImagePath(uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            // 处理File类型的URI
//            imagePath = uri.getPath();
//        }
//        return imagePath;
//    }

//    @SuppressLint("Range")
//    private String getImagePath(Uri uri, String selection) {
//        String path = null;
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }

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
                        String doctName = subDataObject.getString("doctName");
//                        头像也要传一下吧
                        String avatarPath = subDataObject.getString("doctAvatar");
                        Log.d("vvvvvvv", "avatarPath: "+avatarPath);
//                        String realFilePath = getRealPathFromUri(Uri.parse(avatarPath));
//                        Log.d("vvvvvvv", "realFilePath: "+realFilePath);
                        getActivity().runOnUiThread(() -> {
                            if (code == 200) {
                                Log.d("TAG", "onResponse: " + code);
                                mTextView1.setText(doctName);
                                if (avatarPath != null) {
//                                    Glide.with(DocMineFragment.this).load(avatarPath).into(avatarImageView);
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