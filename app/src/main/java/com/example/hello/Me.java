package com.example.hello;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lib.settingview.LSettingItem;

import util.*;

public class Me extends AppCompatActivity {


    private BottomNavigationView mBottomNavigationView;
    private Fragment[]mFragments;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        user = State.getState().getUser();
        TextView displayUname = (TextView) findViewById(R.id.displayUname);
        displayUname.setText(user.userName);

        //对预约记录进行点击事件
        LSettingItem MyRecord = (LSettingItem) findViewById(R.id.MyRecord);
        MyRecord.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", Me.this.user);
                Intent intent = new Intent(Me.this, Record.class);
                startActivity(intent);
            }
        });
        //对个人信用进行点击事件
        LSettingItem two = (LSettingItem) findViewById(R.id.MyCredit);
        two.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                Toast.makeText(Me.this, "点击了个人信用", Toast.LENGTH_SHORT).show();
                //    通过AlertDialog.Builder这个类来实例化一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.vector_drawable_me);
                //    设置Title的内容
                builder.setTitle("个人信用");
                //    设置Content来显示一个信息
                builder.setMessage("您的个人信用为：");
                //    设置一个PositiveButton
                final TextView et=new TextView(Me.this);
                et.setSingleLine();
                et.setText("               1000分", TextView.BufferType.SPANNABLE);
                builder.setView(et);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });

        //对使用帮助进行点击事件
        LSettingItem Help = (LSettingItem) findViewById(R.id.Help);
        Help.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                Toast.makeText(Me.this, "点击了使用帮助", Toast.LENGTH_SHORT).show();
                //    通过AlertDialog.Builder这个类来实例化一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.vector_drawable_me);
                //    设置Title的内容
                builder.setTitle("使用帮助");
                //    设置Content来显示一个信息
                builder.setMessage("本公共体育设施管理系统App详细说明以及代码可见如下链接：\n https://github.com/ynuy1998/android-stadium-management");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(Me.this, "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
        //对退出登录进行点击事件
        LSettingItem signOut = (LSettingItem) findViewById(R.id.signOut);
        signOut.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                State.getState().setUser(null);
                Intent intent = new Intent(Me.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
