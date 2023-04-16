package com.example.mytestmenu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddMsgActivity;
import com.example.mytestmenu.activity.InquiryActivity;
import com.example.mytestmenu.activity.LoginActivity;
import com.example.mytestmenu.activity.MainActivity;
import com.example.mytestmenu.activity.SettingActivity;

public class MineFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV=inflater.inflate(R.layout.fragment_mine, container, false);
        ImageView mImgView=mV.findViewById(R.id.img_setting);
        Button mBtn=mV.findViewById(R.id.edit_profile_button);
        Button mBtn2=mV.findViewById(R.id.logout_button);

        //在frament文件中的onCreateView方法中接收数据并显示
        //声明并初始化一个textview
        TextView mTextView1=mV.findViewById(R.id.username_text_view);
        TextView mTextView2=mV.findViewById(R.id.phone_text_view);
        //接收数据并用textview控件的setText()方法改写text内容，传递的数据是name
        Bundle bundle=getArguments();

        String name=bundle.getString("name");
        mTextView1.setText(name);
        String phone=bundle.getString("phone");
        mTextView2.setText(phone);

        mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getContext(), SettingActivity.class);
                getActivity().startActivity(intent);
            }});

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getContext(), AddMsgActivity.class);
                getActivity().startActivity(intent);
            }});

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getContext(), LoginActivity.class);
//                intent.setClass(getContext(), LoginActivity.class);
//                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP;
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }});
        return mV;
    }


}