package com.example.mytestmenu.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Users;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName;
    private EditText registerPhone;
    private EditText registerPassword;
    private EditText registerRePassword;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.name_edit);
        registerPhone = findViewById(R.id.phone_edit);
        registerPassword = findViewById(R.id.password_edit);
        registerRePassword = findViewById(R.id.password_edit_re);
        register=findViewById(R.id.register_btn);
        register.setOnClickListener(v -> {


            String name=registerName.getText().toString().trim();
            String phone = registerPhone.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String repassword = registerRePassword.getText().toString().trim();

            if (name.equals("") ||phone.equals("")|| password.equals("")||repassword.equals("")) {
                Toast.makeText(RegisterActivity.this, "输入内容不许为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.equals(repassword)){
                List<Users> users = LitePal.findAll(Users.class);
                for (Users patient : users) {
                    if (patient.getUserPhone().equals(phone)) {
                        Toast.makeText(RegisterActivity.this, "账号不可以重复注册", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Users user = new Users();
                user.setUserName(name);
                user.setUserPhone(phone);
                user.setUserPassword(password);
                user.setUserRePassword(repassword);
                user.save();

                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public void goback(View view){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}