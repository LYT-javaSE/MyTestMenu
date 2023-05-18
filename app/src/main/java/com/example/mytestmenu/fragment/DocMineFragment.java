package com.example.mytestmenu.fragment;

import static com.example.mytestmenu.activity.RegisterActivity.Base_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytestmenu.PopupWindow.CommonPopupWindow;
import com.example.mytestmenu.PopupWindow.CommonUtil;
import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddDoctMsgActivity;
import com.example.mytestmenu.activity.AddMsgActivity;
import com.example.mytestmenu.activity.LoginActivity;
import com.example.mytestmenu.entity_class.Avatar;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.utils.FileStorage;
import com.example.mytestmenu.utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class DocMineFragment extends Fragment implements CommonPopupWindow.ViewInterface, EasyPermissions.PermissionCallbacks {

    private TextView mTextView1;
    private String phone;
    private String name;
    private CommonPopupWindow commonPopupWindow;
    private ImageView mIcon;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri uri;
    private int type;
    Uri cropUri;
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
        String path=bundle.getString("avatar");
        mTextView1.setText(name);
        mTextView2.setText(phone);
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
            editor.apply();
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        if (path != null) {
            loadCircleImage(getContext(), path, mIcon);
        }
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll(v);
            }
        });

        return view;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        // 当从其他页面返回到此页面时，重新填充数据
//        fillData();
//    }



    //全屏弹出
    public void showAll(View view) {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(getContext()).inflate(R.layout.popup_up, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setViewOnclickListener(this)
                .create();
        commonPopupWindow.showAtLocation(upView.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_up:
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = (Button) view.findViewById(R.id.btn_select_photo);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 1;
                        getPermission();    //写个if  判断是不是在6.0以上版本   不是直接调用方法
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 2;
                        getPermission();
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
        }
    }

    //获取权限
    public void getPermission() {
        //检测是否有权限
        if (EasyPermissions.hasPermissions(getContext(),permissions)) {
            switch (type) {
                case 1:
                    getCamera();
                    break;
                case 2:
                    getPhotoAlbum();
                    break;
            }
        } else {
            EasyPermissions.requestPermissions(this, "用于读取相册和拍照功能", 1, permissions);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //权限申请成功
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (type) {
            case 1:
                getCamera();
                break;
            case 2:
                getPhotoAlbum();
                break;
        }
    }
    //权限申请失败时的回调
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(getContext(), "请在应用管理里面对应用进行重新授权", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    //相册
    public void getPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");  //type  指定获取 image类型的所有文件
        startActivityForResult(intent, 2);
        //系统相册选图返回的Uri是可以直接使用的，不需要也不能使用FileProvider进行转换
    }

    //照相功能
    /*思路
     * 首先是调用相机  意图
     * 第二步 获取图片路径
     * 最后保存并返回
     */
    private void getCamera() {
        File file = new FileStorage().createCropFile();
                /*   方法                               描述
        File(File dir, String name) File对象类型的目录路径，name为文件名或目录名。
        File(String path)   path为新File对象的路径。
        File(String dirPath, String name)   dirPath为指定的文件路径，name为文件名或目录名。
        File(URI uri)   使用URI指定路径来创建新的File对象。*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(), "com.ycb.baseicon.fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//临时授予读写权限
        } else {
            //低版本路径转成uri
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);  //将图片保存在这个位置
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            //照相
            case 1:
                //此处不可写成 data.getData
                // 因为上面通过putExtra  将地址存在了 uri  这个指定的路径里面
                startPhotoZoom(uri);
                break;
            //相册
            case 2:
                //从相册返回的uri  可以看在getPhotoAlbum处的注解
                startPhotoZoom(data.getData());
                // content                  media
                Log.i("文件地址", data.getScheme() + " " + data.getData().getAuthority() +
                        data.getData().getHost() + data.getData().getPort() + "  " + data.getData().getPath() + "\n" + data.getData());
                //content://media/external/images/media/1036430   path: /external/images/media/1036430
                break;
            //裁剪
            case 3:
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //裁剪处有对照表  键名为data   类型是 parcelable   value (因为写的是true )  bitmap
                    Bitmap bitmap = bundle.getParcelable("data");
                    String path = saveImage(bitmap);
                    loadCircleImage(getContext(), path, mIcon);
                    SPUtils.putString(getContext(), "icon", path);
                }
                Log.i("Uri", cropUri.toString());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //裁剪方法
    private void startPhotoZoom(Uri uri) {
        File file = new FileStorage().createCropFile();
        cropUri = Uri.fromFile(file);  //file 类型转成了 uri 类型 最后在下面使用   最终将裁剪后的图片保存在这个指定的位置
        //调用系统裁剪的意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(uri, "image/*");  //设置data （uri） 和type  类型
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        //Intent 的data域最大传递的值的大小约为1M，所以图片的BITMAP当超过1M时就会失败 : 无法传递大图  false 传递uri
        intent.putExtra("return-data", true);  //true  表示返回的是bitmap对象  为true的情况下 一般在图片尺寸480*480 崩溃
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);  //将 图像转移保存到  -》 croupUri   ：   uri位置
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 3);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        // RequestOptions  扩展glide  自定义加载方式
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    //获取图像的String类型path 地址   可以用于保存或者上传到服务器
    public String saveImage(Bitmap bmp) {
        File file = new FileStorage().createIconFile();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);  //图片压缩
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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