package com.example.hello.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hello.Fragment.Book;
import com.example.hello.Fragment.Mycenter;
import com.example.hello.Fragment.Notification;
import com.example.hello.R;
import com.qrcore.util.QRScannerHelper;

import java.util.ArrayList;
import java.util.List;

import util.SqlHelper;


public class First extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioButton notification_btn,book_btn,me_btn;

    private boolean cameraPermission = true;

    private QRScannerHelper ScannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initview();
        mRadioGroup.setOnCheckedChangeListener(this); //点击事件
        fragments = getFragments(); //添加布局
        //添加默认布局
        normalFragment();

        //添加扫码事件回调
        ScannerHelper = new QRScannerHelper(this);
        ScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (result != null && result.length() > 0){
                    String msg = SqlHelper.getInstance().scanQR(result);
                    Toast.makeText(First.this,
                            msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(First.this, First.class);
                    startActivity(intent);
                    First.this.finish();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }

        FloatingActionButton Scan = findViewById(R.id.Scan);
        Scan.bringToFront();
        //点击扫码
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraPermission){
                    ScannerHelper.startScanner();
                }
                else{
                    Toast.makeText(First.this, "需要先打开相机权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int len = permissions.length;
            for (int i = 0; i < len; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    cameraPermission = false;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ScannerHelper != null) {
            ScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    //默认布局
    private void normalFragment() {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(0);
        transaction.replace(R.id.mFragment,fragment);
        transaction.commit();
    }

    private void initview() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadiogroup);
        notification_btn=(RadioButton) findViewById(R.id.notification);
        book_btn=(RadioButton) findViewById(R.id.book);
        me_btn=(RadioButton) findViewById(R.id.me);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.notification:
                fragment=fragments.get(0);
                transaction.replace(R.id.mFragment,fragment);

                break;
            case R.id.book:
                fragment=fragments.get(1);
                transaction.replace(R.id.mFragment,fragment);

                break;
            case R.id.me:
                fragment=fragments.get(2);
                transaction.replace(R.id.mFragment,fragment);

                break;
        }
        setTabState();
        transaction.commit();
    }
    //设置选中和未选择的状态
    private void setTabState() {
        setNotificationState();
        setBookState();
        setMeState();
    }

    private void setNotificationState() {
        if (notification_btn.isChecked()){
            notification_btn.setTextColor(ContextCompat.getColor(this,R.color.color_blue));
        }else{
            notification_btn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
    }

    private void setBookState() {
        if (book_btn.isChecked()){
            book_btn.setTextColor(ContextCompat.getColor(this,R.color.color_blue));
        }else{
            book_btn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
    }

    private void setMeState() {
        if (me_btn.isChecked()){
            me_btn.setTextColor(ContextCompat.getColor(this,R.color.color_blue));
        }else{
            me_btn.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
    }


    public List<Fragment> getFragments() {
        fragments.add(new Notification());
        fragments.add(new Book());
        fragments.add(new Mycenter());
        return fragments;
    }


}
