package com.example.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import util.*;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SqlHelper.getInstance().initialize(this.getBaseContext());
    }
    /** 注册新用户 */
    public void GotoSignup(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }
    /** 登录成功 */
    public void LoginSuccess(View view) {
        // Do something in response to button
        String userName = ((EditText) findViewById (R.id.username)).getText().toString();
        String password = ((EditText) findViewById (R.id.editText6)).getText().toString();

        if (userName.length() == 0 || password.length() == 0){
            Toast.makeText(MainActivity.this,
                    "登录信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = SqlHelper.getInstance().signIn(userName, password);
        if (SqlHelper.getInstance().getErrorResult().equals("")){
            State.getState().setUser(user);
            Intent intent = new Intent(this, Me.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this,
                    SqlHelper.getInstance().getErrorResult(), Toast.LENGTH_SHORT).show();
        }
    }
}

