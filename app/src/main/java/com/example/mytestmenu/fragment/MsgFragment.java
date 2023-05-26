package com.example.mytestmenu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.ChatActivity;

public class MsgFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_msg, container, false);
        //记录该用户和哪个医生聊过天，显示医生列表
        //         跳转到问诊列表
        RecyclerView recyView=view.findViewById(R.id.rv_msg);
        recyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getContext(), ChatActivity.class);
                getActivity().startActivity(intent);
            }});
        return view;
    }





}