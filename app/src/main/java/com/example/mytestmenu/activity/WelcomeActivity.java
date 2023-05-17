package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.mytestmenu.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mtvCountDown;
    private MyCountDownTimer mCountDownTimer;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();

        //创建倒计时类
        mCountDownTimer = new MyCountDownTimer(6000, 1000);
        //启动倒计时
        mCountDownTimer.start();
        handler = new Handler();
        //这是一个 Handler 里面的逻辑是从 Splash 界面跳转到 Main 界面
        handler.postDelayed(runnable,6000);

        // 检查登录状态
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");

        if (!phone.isEmpty() && !password.isEmpty()) {
            // 已登录，跳转到主界面
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 结束当前启动页，防止用户按返回键返回到该页面
        } else {
            // 未登录，跳转到登录界面
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // 结束当前启动页，防止用户按返回键返回到该页面
        }

    }


    //定义线程，执行跳转
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mtvCountDown.setText( millisUntilFinished / 1000 + "s 跳过");
        }

        @Override
        public void onFinish() {
            mtvCountDown.setText("0s 跳过");
        }
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }


    private void initView() {
        mtvCountDown = (TextView) findViewById(R.id.start_skip_count_down);//跳过
        mtvCountDown.setOnClickListener(this);//跳过监听
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_skip_count_down:
                //从闪屏界面跳转到登录界面
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }

}