package com.example.mytestmenu.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddDoctMsgActivity;
import com.example.mytestmenu.activity.LoginActivity;
import com.example.mytestmenu.entity_class.MsgContent;
import com.example.mytestmenu.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocMineFragment extends Fragment  {

    public static final int REQUEST_CODE_TAKE = 1;
    public static final int REQUEST_CODE_CHOOSE = 0;
    private TextView mTextView1;
    private String phone;
    private String name;
    private ImageView mIcon;
    private Uri imageUri;
    private String imageBase64;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doc_mine, container, false);


        mIcon = view.findViewById(R.id.avatar_image1);
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
        String imagePath=bundle.getString("avatar");
        mTextView1.setText(name);
        mTextView2.setText(phone);
//        显示图片
        if (imagePath != null && !imagePath.isEmpty()) {
            if (isFilePath(imagePath)) {
                // 如果是文件路径，直接加载图片
                displayImage(imagePath);
            } else {
                // 如果是Uri，使用InputStream加载图片
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(imagePath));
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mIcon.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 如果图片路径为空，使用默认的图片或者显示占位符
            mIcon.setImageResource(R.drawable.cycle);
        }

        mBtn.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AddDoctMsgActivity.class);
//            LitePal.deleteAll("MsgContent","mid=?","30");
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("num",num);
            intent.putExtra("sex",sex);
            intent.putExtra("age",age);
            getActivity().startActivity(intent);
        });
        mBtn2.setOnClickListener(v -> {
//            SharedPreferences spf = getActivity().getSharedPreferences("login_status", MODE_PRIVATE);
//            SharedPreferences.Editor editor = spf.edit();
//            editor.clear();
//            editor.apply();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // 为ImageView设置点击事件监听器
        mIcon.setOnClickListener(this::showPopupWindow);
        return view;
    }

    public boolean isFilePath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    private void showPopupWindow(View anchorView) {
        // 创建PopupWindow布局
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_up, null);
        // 创建PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        popupWindow.setOutsideTouchable(true); // 点击PopupWindow以外的区域可以关闭PopupWindow

        // 设置相机按钮的点击事件
        Button cameraButton = popupView.findViewById(R.id.btn_take_photo);
        cameraButton.setOnClickListener(v -> {
            // 处理相机按钮的点击事件
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                Log.d("BBBBBBB", "去打开相机！ ");
                doTake();
                Log.d("BBBBBBB", "去打开相机！ ");
            }else {
                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CAMERA},1);
            }
            popupWindow.dismiss(); // 关闭PopupWindow
        });

        // 设置相册按钮的点击事件
        Button galleryButton = popupView.findViewById(R.id.btn_select_photo);
        galleryButton.setOnClickListener(v -> {
            // 处理相册按钮的点击事件
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("CCCCCCCCC", "去打开相册！ ");
                openAlbum();
                Log.d("CCCCCCCCC", "去打开相册！ ");
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            popupWindow.dismiss();
        });

        // 设置取消按钮的点击事件
        Button cancelButton = popupView.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> {
            // 处理取消按钮的点击事件
            popupWindow.dismiss();
        });
        // 获取当前页面的视图
        View rootView = getActivity().getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        // 创建一个 Bitmap 对象，并将当前页面的视图绘制到 Bitmap 上
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        // 对 Bitmap 进行模糊处理
        Bitmap blurredBitmap = blurBitmap(bitmap, 10); // 模糊半径可以根据需要进行调整
        // 创建模糊效果的背景
        BitmapDrawable background = new BitmapDrawable(getResources(), blurredBitmap);
        // 设置背景模糊效果
        background.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_OVER);
        popupWindow.setBackgroundDrawable(background);
        // 计算弹窗的偏移量
        int offsetX = (anchorView.getWidth() - popupView.getWidth()) / 2;
        int offsetY = (anchorView.getHeight() - popupView.getHeight()) / 2;
        // 设置弹窗的位置
        popupWindow.showAsDropDown(anchorView, offsetX, offsetY);
    }
    private Bitmap blurBitmap(Bitmap bitmap, int radius) {
        // 创建 RenderScript 对象
        RenderScript rs = RenderScript.create(getContext());
        // 创建一个用于输入的 Allocation 对象
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        // 创建一个用于输出的 Allocation 对象
        Allocation output = Allocation.createTyped(rs, input.getType());
        // 创建一个模糊效果的 RenderScript 脚本对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blurScript.setInput(input);
        // 设置模糊半径
        blurScript.setRadius(radius);
        // 执行模糊处理
        blurScript.forEach(output);
        // 将处理后的 Allocation 对象内容复制到 Bitmap 对象中
        output.copyTo(bitmap);
        // 销毁 RenderScript 对象
        rs.destroy();
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_TAKE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doTake();
                Log.d("DDDDDDDDDDDDDDD", "已获得摄像头权限 ");
            } else {
                Log.d("DDDDDDDDDDDDDDD", "没有获得摄像头权限 ");
                Toast.makeText(getContext(), "你没有获得摄像头权限~", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
                Log.d("DDDDDDDDDDDDDDD", "已获得相册权限 ");
            } else {
                Log.d("DDDDDDDDDDDDDDD", "获得摄像头权限 ");
                Toast.makeText(getContext(), "你没有获得访问相册的权限~", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE) {
            if (resultCode == RESULT_OK) {
                // 获取拍摄的照片

                // 上传图片到服务器
                uploadImageToServer(String.valueOf(imageUri));
                Log.d("tttttttttttttttttttt", String.valueOf(imageUri));
//                    显示图片
                InputStream inputStream = null;
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mIcon.setImageBitmap(bitmap);
                fillData();
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE) {
            if (Build.VERSION.SDK_INT < 19) {
                handleImageBeforeApi19(data);
            } else {
                handleImageOnApi19(data);
            }
        }
    }

    private void handleImageBeforeApi19(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
        uploadImageToServer(imagePath);
        fillData();
    }
    private void handleImageOnApi19(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(requireContext(), uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (TextUtils.equals(uri.getAuthority(), "com.android.providers.media.documents")) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if (TextUtils.equals(uri.getAuthority(), "com.android.providers.downloads.documents")) {
                if (documentId != null && documentId.startsWith("msf:")) {
                    resolveMSFContent(uri, documentId);
                    return;
                }
                assert documentId != null;
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
//        还可以把路径保存到数据库里，使下次登录时能找到该路径并显示
        uploadImageToServer(imagePath);
        fillData();
    }

    private void resolveMSFContent(Uri uri, String documentId) {
        File file = new File(getActivity().getCacheDir(), "temp_file" + getActivity().getContentResolver().getType(uri).split("/")[1]);
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mIcon.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.imageToBase64(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 进一步解析真实Uri
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        Log.d("ooooooooo",uri+"&&"+selection);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        Log.d("ppppppppppppp",path);
        return path;
    }


    public void displayImage(String imagePath) {
        Log.d("TAG", "displayImage: ------------" + imagePath);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIcon.setImageBitmap(bitmap);
//            可以将字节流转换为字符串存起来（也就是说可以作为图片的名字imageName）
            String imageToBase64 = ImageUtil.imageToBase64(bitmap);
            imageBase64 = imageToBase64;
        }else {
            Log.d("TAG", "图片路径为空,显示默认图片");
        }
    }

    private void doTake(){
        File imageTemp = new File(getActivity().getExternalCacheDir(), "imageOut.jpeg");
        if (imageTemp.exists()){
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Build.VERSION.SDK_INT>24){
            imageUri= FileProvider.getUriForFile(getContext(),"com.example.mytestmenu.fileprovider",imageTemp);
        }else {
            imageUri=Uri.fromFile(imageTemp);
        }
        Intent intent=new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,REQUEST_CODE_TAKE);
    }
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_CHOOSE);
    }

    // 传递图片地址到后端数据库
    private void uploadImageToServer(String imagePath) {
        // 创建请求体
        OkHttpClient client = new OkHttpClient();
        String doct_url = Base_URL + "/docts/editAvatar";
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .add("avatar_path",imagePath)
                .build();
        // 创建请求
        Request request = new Request.Builder()
                .url(doct_url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                e.printStackTrace();
                Toast.makeText(getContext(), "网络请求失败，请重试", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理请求成功的情况
                if (response.isSuccessful()) {
                    JSONObject responseBody = null;
                    try {
                        responseBody = new JSONObject(response.body().string());
                        int code = responseBody.getInt("code");
                        if (code==200){
                            Log.d("显示响应成功信息:", "换头像成功");
                        }
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // 处理请求失败的情况
                    String errorMessage = response.message();
                    // 显示错误提示
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.d("理应显示响应错误信息:", errorMessage);
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当从其他页面返回到此页面时，重新填充数据
        fillData();
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
                        String doctName = subDataObject.getString("doctName");
//                        头像也要传一下吧
                        String avatarPath = subDataObject.getString("doctAvatar");
                        getActivity().runOnUiThread(() -> {
                            if (code == 200) {
                                Log.d("TAG", "onResponse: " + code);
                                mTextView1.setText(doctName);

                                Log.d("vvvvvvv", "avatarPath: "+avatarPath);
                                if (avatarPath != null && !avatarPath.isEmpty()) {
                                    if (isFilePath(avatarPath)) {
                                        // 如果是文件路径，直接加载图片
                                        displayImage(avatarPath);
                                    } else {
                                        // 如果是Uri，使用InputStream加载图片
                                        try {
                                            InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(avatarPath));
                                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                            mIcon.setImageBitmap(bitmap);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    // 如果图片路径为空，使用默认的图片或者显示占位符
                                    mIcon.setImageResource(R.drawable.cycle);
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