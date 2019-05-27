package com.example.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import util.SqlHelper;
import util.User;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
    }

    public void SignUp(View view){
        String userName = ((EditText) findViewById (R.id.Newusername)).getText().toString();
        if (userName.length() == 0){
            Toast.makeText(DisplayMessageActivity.this,
                    "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SqlHelper.getInstance().isUserNameUsed(userName)){
            Toast.makeText(DisplayMessageActivity.this,
                    "用户名已被使用", Toast.LENGTH_SHORT).show();
            return;
        }
        String stuId = ((EditText) findViewById (R.id.editText6)).getText().toString();
        if (stuId.length() == 0){
            Toast.makeText(DisplayMessageActivity.this,
                    "学号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SqlHelper.getInstance().isStuIdRegistered(stuId)){
            Toast.makeText(DisplayMessageActivity.this,
                    "学号已被注册", Toast.LENGTH_SHORT).show();
            return;
        }
        String password1 = ((EditText) findViewById (R.id.editText)).getText().toString();
        if (password1.length() == 0){
            Toast.makeText(DisplayMessageActivity.this,
                    "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String password2 = ((EditText) findViewById (R.id.editText2)).getText().toString();
        if (!password2.equals(password1)){
            Toast.makeText(DisplayMessageActivity.this,
                    "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Spinner collegeSpinner = (Spinner) findViewById (R.id.spinner);
        Spinner genderSpinner = (Spinner) findViewById (R.id.spinner2);
        String college = (String) collegeSpinner.getSelectedItem();
        String gender = (String) genderSpinner.getSelectedItem();
        User newUser = new User(-1, userName, stuId, college, gender, 0);
        SqlHelper.getInstance().signUp(newUser, password1);
        Toast.makeText(DisplayMessageActivity.this,
                "注册成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
