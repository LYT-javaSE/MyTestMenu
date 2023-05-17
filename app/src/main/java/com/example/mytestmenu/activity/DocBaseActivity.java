package com.example.mytestmenu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Intent intent;
    Bundle bundle;

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

                        intent =getIntent();
                        String one = intent.getStringExtra("doctName");
                        String two = intent.getStringExtra("doctNum");

                        bundle=new Bundle();
                        bundle.putString("name",one);
                        bundle.putString("num",two);

                        fragment.setArguments(bundle);
                        break;
                    case R.id.msg:
                        fragment=new DocItemFragment();
                        break;
                    case R.id.mine:
                        fragment=new DocMineFragment();

                        //取得启动该Activity的Intent对象
                        intent =getIntent();
//                        /*取出Intent中附加的数据*/
                        String first = intent.getStringExtra("doctPhone");
                        String second = intent.getStringExtra("doctName");
                        String third = intent.getStringExtra("doctNum");
                        String forth = intent.getStringExtra("doctSex");
                        int fifth = intent.getIntExtra("doctAge",0);
                        String six = intent.getStringExtra("avatar");
                        Log.d("", "头像路径: "+six);
//                        向fragment传递数据
                        bundle=new Bundle();
                        bundle.putString("phone",first);
                        bundle.putString("name",second);
                        bundle.putString("num",third);
                        bundle.putString("sex",forth);
                        bundle.putInt("age",fifth);
                        bundle.putString("avatar",six);
                        fragment.setArguments(bundle);
                        break;
                    default:break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_doc,fragment).commit();
                return true;
            }
        });
    }
}