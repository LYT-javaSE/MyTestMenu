package com.example.mytestmenu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mytestmenu.R;
import com.example.mytestmenu.fragment.DocHomeFragment;
import com.example.mytestmenu.fragment.DocItemFragment;
import com.example.mytestmenu.fragment.DocMineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


/**
 * 宿主activity
 */
public class DocBaseActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_base);

        /**
         * 注册监听
         */
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_doc,new DocHomeFragment()).commit();
        }
        bottomNavigationView=findViewById(R.id.bBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.home:
                        fragment=new DocHomeFragment();
                        break;
                    case R.id.msg:
                        fragment=new DocItemFragment();
                        break;
                    case R.id.mine:
                        fragment=new DocMineFragment();
                        break;
                    default:break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_doc,fragment).commit();
                return true;
            }
        });
    }



}