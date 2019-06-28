package com.example.hello.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.Activity.MainActivity;
import com.example.hello.Activity.Record;
import com.example.hello.R;
import com.leon.lib.settingview.LSettingItem;

import util.State;
import util.User;

public class Mycenter extends Fragment {

    private User user;

    public Mycenter(){
    }

    //单例模式
    public static Mycenter newInstance(){
        Mycenter mycenter=new Mycenter();
        return mycenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mycenter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton Scan = getActivity().findViewById(R.id.Scan);
        Scan.bringToFront();
        user = State.getState().getUser();
        TextView displayUname = (TextView) getActivity().findViewById(R.id.displayUname);
        displayUname.setText(user.userName);

        LSettingItem MyRecord = (LSettingItem) getActivity().findViewById(R.id.MyRecord);
        MyRecord.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", Mycenter.this.user);
                Intent intent = new Intent(getActivity(), Record.class);
                startActivity(intent);
            }
        });
        //对个人信用进行点击事件
        LSettingItem two = (LSettingItem) getActivity().findViewById(R.id.MyCredit);
        two.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                //    通过AlertDialog.Builder这个类来实例化一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //    设置Title的图标
                builder.setIcon(R.drawable.vector_drawable_me);
                //    设置Title的内容
                builder.setTitle("个人信用");
                //    设置Content来显示一个信息
                builder.setMessage("您的个人信用为：");
                //    设置一个PositiveButton
                final TextView et=new TextView(getActivity());
                et.setSingleLine();
                et.setText(String.format(
                        "               %d分", 1000-100*State.getState().getUser().ruleBreak),
                        TextView.BufferType.SPANNABLE);
                builder.setView(et);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "positive: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });

        //对使用帮助进行点击事件
        LSettingItem Help = (LSettingItem) getActivity().findViewById(R.id.Help);
        Help.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                //    通过AlertDialog.Builder这个类来实例化一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getActivity(), "positive: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "neutral: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
        //对退出登录进行点击事件
        LSettingItem signOut = (LSettingItem) getActivity().findViewById(R.id.signOut);
        signOut.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            public void click() {
                State.getState().setUser(null);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
