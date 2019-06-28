package com.example.hello.Activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.hello.R;
import com.example.hello.View.myView;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import util.SqlHelper;
import util.State;
import util.Utility;

public class Reservation extends Activity implements View.OnClickListener {

    private Date startTime;
    private Date endTime;
    private Date dateTime;

    private Date stadiumStart = new Date();
    private Date stadiumEnd = new Date();
    private Calendar today = Calendar.getInstance();
    private Calendar tomorrow = Calendar.getInstance();
    DateTime localTime = (new DateTime()).withZone(DateTimeZone.forOffsetHours(8));
    private myView rect;
    private TimePickerView pvTime, pvDate;
    List<SqlHelper.FreeTime> todayFreeList = new ArrayList<>();
    List<SqlHelper.FreeTime> tomorrowFreeList = new ArrayList<>();
    List<Integer> todayColor = new ArrayList<>();
    List<Float> todayWeight = new ArrayList<>();
    List<Integer> tomorrowColor = new ArrayList<>();
    List<Float> tomorrowWeight = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reservation);

        TextView gymName = findViewById(R.id.Reservationgymname);
        rect = findViewById(R.id.rect);
        gymName.setText(State.getState().getNowStadium().StadiumName);

        EditText et_startTime = findViewById(R.id.et_startTime);
        EditText et_endTime = findViewById(R.id.et_endTime);
        EditText et_dateTime = findViewById(R.id.et_dateTime);
        et_startTime.setOnClickListener(this);
        et_endTime.setOnClickListener(this);
        et_dateTime.setOnClickListener(this);
        stadiumStart = Utility.strTimeToDate(State.getState().getNowStadium().startTime);
        stadiumEnd = Utility.strTimeToDate((State.getState().getNowStadium().endTime));
        initTimePicker();
        initDatePicker();

        //加载今天的空闲时间链表
        SqlHelper.getInstance().getFreeTimeList(
                State.getState().getNowStadium(),
                Utility.getDate((new DateTime()).toDate()),
                todayFreeList
        );
        //加载今天的时间条颜色链表
        setTodayColorWeight();
        //加载明天的空闲时间链表
        SqlHelper.getInstance().getFreeTimeList(
                State.getState().getNowStadium(),
                Utility.getDate((new DateTime().plusDays(1)).toDate()),
                tomorrowFreeList
        );
        //加载明天的时间颜色条链表
        setTomorrowColorWeight();
    }

    /* 加载今天的时间条颜色链表 */
    public void setTodayColorWeight(){
        LocalTime now = new LocalTime("07:00:00");
        for (int i = 0; i < todayFreeList.size(); i++){
            todayWeight.add((float)Minutes.minutesBetween(now, todayFreeList.get(i).start).getMinutes());
            todayColor.add(Color.parseColor("#D3D3D3"));
            todayWeight.add((float)Minutes.minutesBetween(todayFreeList.get(i).start, todayFreeList.get(i).end).getMinutes());
            todayColor.add(Color.parseColor("#1E8EFE"));
            now = todayFreeList.get(i).end;
        }
        todayWeight.add((float)Minutes.minutesBetween(now, new LocalTime("22:00:00")).getMinutes());
        todayColor.add(Color.parseColor("#D3D3D3"));
    }

    /* 加载明天的空闲时间链表 */
    public void setTomorrowColorWeight(){
        LocalTime now = new LocalTime("07:00:00");
        for (int i = 0; i < tomorrowFreeList.size(); i++){
            tomorrowWeight.add((float)Minutes.minutesBetween(now, tomorrowFreeList.get(i).start).getMinutes());
            tomorrowColor.add(Color.parseColor("#D3D3D3"));
            tomorrowWeight.add((float)Minutes.minutesBetween(tomorrowFreeList.get(i).start, tomorrowFreeList.get(i).end).getMinutes());
            tomorrowColor.add(Color.parseColor("#1E8EFE"));
            now = tomorrowFreeList.get(i).end;
        }
        tomorrowWeight.add((float)Minutes.minutesBetween(now, new LocalTime("22:00:00")).getMinutes());
        tomorrowColor.add(Color.parseColor("#D3D3D3"));
    }

    /* 取消按钮回调 */
    public void Backtome(View view) {
        Reservation.this.finish();
    }

    /* 提交预约按钮回调 */
    public void submitBook(View view){
        if (dateTime == null){
            Toast.makeText(Reservation.this,
                    "请选择预约日期", Toast.LENGTH_SHORT).show();
        }
        else if (startTime == null){
            Toast.makeText(Reservation.this,
                    "请选择预约开始时间", Toast.LENGTH_SHORT).show();
        }
        else if (endTime == null) {
            Toast.makeText(Reservation.this,
                    "请选择预约结束时间", Toast.LENGTH_SHORT).show();
        }
        else{
            LocalTime start = new LocalTime(Utility.getDetailTime(stadiumStart));
            LocalTime pickStart = new LocalTime(Utility.getDetailTime(startTime));
            LocalTime end = new LocalTime(Utility.getDetailTime(stadiumEnd));
            LocalTime pickEnd = new LocalTime(Utility.getDetailTime(endTime));
            DateTime pickDate = new DateTime(Utility.getDate(dateTime));

            if (pickStart.isBefore(start)){
                Toast.makeText(Reservation.this,
                        "预约开始时间必须大于该场地开放时间", Toast.LENGTH_SHORT).show();
            }
            else if (localTime.getDayOfMonth() == pickDate.getDayOfMonth() &&
                    Minutes.minutesBetween(localTime.toLocalTime(), pickStart).getMinutes() < 0)
            {
                Toast.makeText(Reservation.this,
                        "预约时间已过期", Toast.LENGTH_SHORT).show();
            }
            else if (pickEnd.isAfter(end)){
                Toast.makeText(Reservation.this,
                        "预约结束时间必须小于该场地结束时间", Toast.LENGTH_SHORT).show();
            }
            else if (pickEnd.isBefore(pickStart)){
                Toast.makeText(Reservation.this,
                        "预约结束时间必须大于预约开始时间", Toast.LENGTH_SHORT).show();
            }
            else if (Minutes.minutesBetween(pickStart, pickEnd).getMinutes() < 10){
                Toast.makeText(Reservation.this,
                        "预约时长必须大于10分钟", Toast.LENGTH_SHORT).show();
            }
            else if (Minutes.minutesBetween(pickStart, pickEnd).getMinutes() > 60){
                Toast.makeText(Reservation.this,
                        "预约时长不能超过1小时", Toast.LENGTH_SHORT).show();
            }
            else if (SqlHelper.getInstance().isUserHasAppoint(
                    State.getState().getUser().userId,
                    Utility.getDate(pickDate.toDate())
            )){
                Toast.makeText(Reservation.this,
                        "您当天已有预约或违约,不能再预约", Toast.LENGTH_SHORT).show();
            }
            else {
                List<SqlHelper.FreeTime> freeList;
                if (Utility.getDate(pickDate.toDate()).equals(
                        Utility.getDate((new DateTime()).toDate())
                )){
                    freeList = todayFreeList;
                }
                else{
                    freeList = tomorrowFreeList;
                }
                for (int i = 0; i < freeList.size(); i++){
                    if (!pickStart.isBefore(freeList.get(i).start) &&
                        !pickEnd.isAfter(freeList.get(i).end)
                    ){
                        Toast.makeText(Reservation.this,
                                "预约成功", Toast.LENGTH_SHORT).show();
                        ContentValues appointValue = new ContentValues();
                        appointValue.put("userId", State.getState().getUser().userId);
                        appointValue.put("stadiumId", State.getState().getNowStadium().StadiumId);
                        appointValue.put("bookDate", Utility.getDate(pickDate.toDate()));
                        appointValue.put("startTime", pickStart.toString("HH:mm:ss"));
                        appointValue.put("endTime", pickEnd.toString("HH:mm:ss"));
                        appointValue.put("mode", 0);
                        appointValue.put("submitDate", Utility.getDate(localTime.toDate()));
                        SqlHelper.getInstance().addAppointByUser(appointValue);
                        Intent intent = new Intent(Reservation.this, First.class);
                        startActivity(intent);
                        Reservation.this.finish();
                        return;
                    }
                }
                Toast.makeText(Reservation.this,
                        "与已有预约时间冲突,请重新选择时间", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_startTime:
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    if (startTime == null){
                        calendar.setTime(stadiumStart);
                    }
                    else{
                        calendar.setTime(startTime);
                    }
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                }
                break;
            case R.id.et_endTime:
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    if (endTime != null){
                        calendar.setTime(endTime);
                    }
                    else if (startTime != null){
                        LocalTime start = new LocalTime(startTime);
                        calendar.setTime(start.plusMinutes(10).toDateTimeToday().toDate());
                    }
                    else{
                        calendar.setTime(stadiumStart);
                    }
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                }
                break;
            case R.id.et_dateTime:
                if (pvDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    pvDate.setDate(calendar);
                    pvDate.show(view);
                }
                break;

        }
    }


    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(v.getId() == R.id.et_startTime){
                    startTime = new Date();
                    startTime = date;
                }else {
                    endTime = new Date();
                    endTime = date;
                }
                EditText editText = (EditText)v;
                editText.setText(Utility.getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setDate(today)
                .isDialog(true)
                .build();


        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }
    private void initDatePicker() {

        tomorrow.set(today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) + 1);
        pvDate = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(v.getId() == R.id.et_dateTime){
                    dateTime = new Date();
                    dateTime= date;
                }
                EditText editText = (EditText)v;
                editText.setText(Utility.getDate(date));
                if (Utility.getDate(localTime.toDate()).equals(Utility.getDate(date))){
                    State.getState().DEF_COLORS = new int[todayColor.size()];
                    State.getState().DEF_WEIGHTS = new float[todayColor.size()];
                    for (int i = 0; i < todayColor.size(); i++) {
                        State.getState().DEF_COLORS[i] = todayColor.get(i);
                        State.getState().DEF_WEIGHTS[i] = todayWeight.get(i);
                    }
                }
                else{
                    State.getState().DEF_COLORS = new int[tomorrowColor.size()];
                    State.getState().DEF_WEIGHTS = new float[tomorrowColor.size()];
                    for (int i = 0; i < tomorrowColor.size(); i++) {
                        State.getState().DEF_COLORS[i] = tomorrowColor.get(i);
                        State.getState().DEF_WEIGHTS[i] = tomorrowWeight.get(i);
                    }
                }
                rect.postInvalidate();
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{false, true, true, false, false, false})
                .setDate(today)
                .setRangDate(today, tomorrow)
                .isDialog(true)
                .build();


        Dialog mDialog = pvDate.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvDate.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        State.getState().DEF_WEIGHTS = null;
        State.getState().DEF_COLORS = null;
    }
}
