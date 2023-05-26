package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mytestmenu.R;

public class RegSuccessActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_success);

        textView=findViewById(R.id.tv_backToHome);
        textView.setOnClickListener((v)->{
            Intent intent =new Intent(RegSuccessActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}