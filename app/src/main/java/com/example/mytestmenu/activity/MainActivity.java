package com.example.mytestmenu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import com.example.mytestmenu.R;
import com.example.mytestmenu.fragment.HomeFragment;
import com.example.mytestmenu.fragment.MineFragment;
import com.example.mytestmenu.fragment.MsgFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


/**
 * 宿主activity
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 注册监听
         */
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frags,new HomeFragment()).commit();
        }
        bnv=findViewById(R.id.bottomBar);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                Intent intent;
                Bundle bundle;
                switch (item.getItemId()){
                    case R.id.home:
                        fragment=new HomeFragment();

                        intent =getIntent();
                        String fi = intent.getStringExtra("userPhone");
                        bundle=new Bundle();
                        bundle.putString("phone",fi);
                        fragment.setArguments(bundle);

                        break;
                    case R.id.msg:
                        fragment=new MsgFragment();
                        break;
                    case R.id.mine:
                        fragment=new MineFragment();
                        //取得启动该Activity的Intent对象
                        intent =getIntent();
//                        /*取出Intent中附加的数据*/
                        String first = intent.getStringExtra("userPhone");
                        String second = intent.getStringExtra("userName");
                        int third = intent.getIntExtra("userAge",0);
                        String forth = intent.getStringExtra("userSex");
                        String fifth = intent.getStringExtra("birthDay");
//                        向fragment传递数据
                        bundle=new Bundle();
                        bundle.putString("phone",first);
                        bundle.putString("name",second);
                        bundle.putInt("age", third);
                        bundle.putString("sex",forth);
                        bundle.putString("birth",fifth);
                        fragment.setArguments(bundle);

                        break;
                    default:break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frags,fragment).commit();
                return true;
            }
        });
    }

}