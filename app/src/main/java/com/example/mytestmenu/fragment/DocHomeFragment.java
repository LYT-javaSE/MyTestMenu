package com.example.mytestmenu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.AddDoctMsgActivity;
import com.example.mytestmenu.activity.ShowForDoctActivity;

public class DocHomeFragment extends Fragment {

    private Button mBtn1;
    private Button mBtn2;
    private String name;
    private String num;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doc_home, container, false);



        mBtn1=view.findViewById(R.id.btn_process_registration);
        mBtn2=view.findViewById(R.id.btn_process_online_consultation);
        Bundle bundle=getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            num = bundle.getString("num");
        }
        mBtn1.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), ShowForDoctActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("num",num);
//            intent.putExtra("sex",sex);
//            intent.putExtra("age",age);
            getActivity().startActivity(intent);
        });


        return view;
    }
}