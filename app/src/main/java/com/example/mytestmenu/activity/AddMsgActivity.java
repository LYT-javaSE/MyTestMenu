package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytestmenu.R;

import java.util.Calendar;

public class AddMsgActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView information_birthday;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_msg);


//        EditText mTextView1=findViewById(R.id.editTextNickname);
//        // 使用Intent对象得到MainActivity中传过来的参数
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        mTextView1.setText(name);

        information_birthday = findViewById(R.id.text_birthday);
        information_birthday.setOnClickListener(v->{
                Calendar calendar = Calendar.getInstance();//获取Calendar实例
                //创建日期选择器
                DatePickerDialog dialog = new DatePickerDialog(this,this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MARCH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();//窗口弹出
        });

    }

    //日期选择完事件,实现onDateSet方法，作为待会日期选择完要执行的事件
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String desc = String.format("%d-%d-%d",year,month+1,dayOfMonth);
        information_birthday.setText(desc);//设置生日
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}