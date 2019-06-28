package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.Activity.First;
import com.example.hello.Activity.MainActivity;
import com.example.hello.Activity.mapActivity;
import com.example.hello.Fragment.Notification;
import com.example.hello.R;
import com.qrcore.util.QRScannerHelper;

import java.io.File;
import java.util.List;

import util.Appoint;
import util.SqlHelper;
import util.Utility;


public class notifAdapter extends ArrayAdapter {

    public notifAdapter(Context context, int resource, List<Appoint> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Appoint appoint = (Appoint) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.notif_item, null);
        TextView stadiumName = view.findViewById(R.id.bookStadiumName);
        TextView date = view.findViewById(R.id.remindDate);
        TextView start = view.findViewById(R.id.remindStart);
        TextView end = view.findViewById(R.id.remindEnd);
        TextView mode = view.findViewById(R.id.remindMode);
        stadiumName.setText(appoint.stadiumName);
        date.setText(appoint.bookDate);
        start.setText(appoint.startTime.substring(0, 5));
        end.setText(appoint.endTime.substring(0, 5));
        mode.setText(Utility.getBookMode(appoint.mode));
        Button GPS = view.findViewById(R.id.GPSbtn);
        Button Gaode = view.findViewById(R.id.GPSbtn2);
        Button Baidu = view.findViewById(R.id.GPSbtn3);
        Button Cancel = view.findViewById(R.id.CancelBtn);
        //点击内置导航
        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), mapActivity.class);
                intent.putExtra("longitude", (double)appoint.longitude);
                intent.putExtra("latitude", (double)appoint.latitude);
                intent.putExtra("address", appoint.stadiumAdd);
                getContext().startActivity(intent);
            }
        });
        //点击高德地图
        Gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPackageInstalled("com.autonavi.minimap")){
                    Toast.makeText(getContext(), "高德地图尚未安装", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent("android.intent.action.VIEW",
                            android.net.Uri.parse("androidamap://route?sourceApplication=appName" +
                                    "&slat=&slon=&sname=我的位置&dlat=" + appoint.latitude +
                                    "&dlon=" + appoint.longitude + "&dname=" + appoint.stadiumAdd + "&t=0"));
                    getContext().startActivity(intent);
                }
            }
        });
        //点击百度地图
        Baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPackageInstalled("com.baidu.BaiduMap")){
                    Toast.makeText(getContext(), "百度地图尚未安装", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent naviIntent = new Intent(
                            "android.intent.action.VIEW",
                            android.net.Uri.parse("baidumap://map/geocoder?location=" +
                                    appoint.latitude + "," +
                                    appoint.longitude));
                    getContext().startActivity(naviIntent);
                }
            }
        });

        //点击取消预约
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appoint.mode == 0) {
                    Toast.makeText(getContext(), "预约已取消", Toast.LENGTH_SHORT).show();
                    SqlHelper.getInstance().cancelAppoint(appoint.ReservationId);
                    Intent intent = new Intent(getContext(), First.class);
                    getContext().startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "预约已生效,无法取消", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean isPackageInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
