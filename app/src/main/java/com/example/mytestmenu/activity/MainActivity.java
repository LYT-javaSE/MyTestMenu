package com.example.mytestmenu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
                switch (item.getItemId()){
                    case R.id.home:
                        fragment=new HomeFragment();
                        break;
                    case R.id.msg:
                        fragment=new MsgFragment();
                        break;
                    case R.id.mine:
                        fragment=new MineFragment();
                        //取得启动该Activity的Intent对象
                        Intent intent =getIntent();
//                        /*取出Intent中附加的数据*/
                        String first = intent.getStringExtra("userPhone");
                        String second = intent.getStringExtra("userName");
//                        Log.d("MineFragment", "onCreate: "+first+second);
//                        向fragment传递数据
                        Bundle bundle=new Bundle();
                        bundle.putString("phone",first);
                        bundle.putString("name",second);
                        fragment.setArguments(bundle);
//nav_contatiner是activity2的xml文件中的fragment控件的id，用replace（）方法替换掉activi2中的fragment
//                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.nav_container,fragment).commitNow();

                        break;
                    default:break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frags,fragment).commit();
                return true;
            }
        });
    }



}