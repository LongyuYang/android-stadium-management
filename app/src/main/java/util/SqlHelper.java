package util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Context.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.journeyapps.barcodescanner.Util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import util.User;

public class SqlHelper {
    private static SqlHelper SqlHelperInstance = new SqlHelper();
    private boolean isInitialized;
    private String errorResult;
    private SQLiteDatabase db;
    private SqlHelper(){
        isInitialized = false;
    }
    public void initialize(Context context){
        if (isInitialized) return;
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().getPath()+"sports.db", null);
        //db.execSQL("drop table if exists user;");
        //db.execSQL("drop table if exists stadium;");
        //db.execSQL("drop table if exists book ");
        db.execSQL("create table if not exists user(userId integer primary key autoincrement," +
                " userName text not null, password integer not null, stuId text, college text, birthday data," +
                "gender varchar(5), ruleBreak integer default 0);");
        db.execSQL("create table if not exists stadium(stadiumId integer primary key autoincrement," +
                " stadiumName text not null, details text, mode int, QRCode text, longitude real," +
                "latitude real, starttime text, endtime text, longesttime int, address text);");
        db.execSQL("create table if not exists book(bookId integer primary key autoincrement, " +
                "userId integer, stadiumId integer, bookDate text, starttime text, endtime text, mode integer, submitDate text, " +
                "FOREIGN KEY(userId) REFERENCES user(userId), FOREIGN KEY(stadiumId) REFERENCES stadium(stadiumId))");
        Cursor cursor = db.query("user", null, null,
                null, null, null, null);
        if (!cursor.moveToFirst()){
            ContentValues cValue = new ContentValues();
            cValue.put("userName", "root");
            cValue.put("password", hash("123"));
            cValue.put("stuId", "0000000");
            db.insert("user", null, cValue);
            /*
            cValue.clear();
            cValue.put("userName", "m");
            cValue.put("password", hash("1"));
            cValue.put("stuId", "0000001");
            db.insert("user", null, cValue);
            cValue.clear();
            cValue.put("userName", "t");
            cValue.put("password", hash("1"));
            cValue.put("stuId", "0000002");
            db.insert("user", null, cValue);
            */
        }
        cursor.close();
        cursor = db.query("stadium", null, null,
                null, null, null, null);
        if (!cursor.moveToFirst()) {
            ContentValues stadiumValue = new ContentValues();
            stadiumValue.put("stadiumName", "嘉定乒乓馆-1");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "1/android-stadium-management/jiadingpingpong-1");
            stadiumValue.put("latitude", 31.28987);
            stadiumValue.put("longitude", 121.21609);
            stadiumValue.put("starttime", "12:00:00");
            stadiumValue.put("endtime", "18:00:00");
            stadiumValue.put("address", "同济大学嘉定校区体育馆2楼");
            db.insert("stadium", null, stadiumValue);
            stadiumValue.clear();
            stadiumValue.put("stadiumName", "嘉定乒乓馆-2");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "2/android-stadium-management/jiadingpingpong-2");
            stadiumValue.put("latitude", 31.28987);
            stadiumValue.put("longitude", 121.21609);
            stadiumValue.put("starttime", "12:00:00");
            stadiumValue.put("endtime", "18:00:00");
            stadiumValue.put("address", "同济大学嘉定校区体育馆2楼");
            db.insert("stadium", null, stadiumValue);
            stadiumValue.clear();
            stadiumValue.put("stadiumName", "嘉定羽毛球馆-1");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "3/android-stadium-management/jiadingyumao-1");
            stadiumValue.put("latitude", 31.28987);
            stadiumValue.put("longitude", 121.21609);
            stadiumValue.put("starttime", "09:00:00");
            stadiumValue.put("endtime", "18:00:00");
            stadiumValue.put("address", "同济大学嘉定校区体育馆2楼");
            db.insert("stadium", null, stadiumValue);
            stadiumValue.clear();
            stadiumValue.put("stadiumName", "嘉定羽毛球馆-2");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "4/android-stadium-management/jiadingyumao-2");
            stadiumValue.put("latitude", 31.28987);
            stadiumValue.put("longitude", 121.21609);
            stadiumValue.put("starttime", "09:00:00");
            stadiumValue.put("endtime", "18:00:00");
            stadiumValue.put("address", "同济大学嘉定校区体育馆2楼");
            db.insert("stadium", null, stadiumValue);
            stadiumValue.clear();
            stadiumValue.put("stadiumName", "四平乒乓馆-1");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "5/android-stadium-management/sipingpingpong-1");
            stadiumValue.put("latitude", 31.2823);
            stadiumValue.put("longitude", 121.5036);
            stadiumValue.put("starttime", "09:00:00");
            stadiumValue.put("endtime", "21:00:00");
            stadiumValue.put("address", "同济大学四平路校区乒乓房");
            db.insert("stadium", null, stadiumValue);
            stadiumValue.clear();
            stadiumValue.put("stadiumName", "四平乒乓馆-2");
            stadiumValue.put("mode", 0);
            stadiumValue.put("QRCode", "6/android-stadium-management/sipingpingpong-2");
            stadiumValue.put("latitude", 31.2823);
            stadiumValue.put("longitude", 121.5036);
            stadiumValue.put("starttime", "09:00:00");
            stadiumValue.put("endtime", "21:00:00");
            stadiumValue.put("address", "同济大学四平路校区乒乓房");
            db.insert("stadium", null, stadiumValue);
        }
        errorResult = "";
        cursor.close();
        isInitialized = true;
    }

    //登陆操作
    public User signIn(String userName, String password){
        errorResult = "";
        Cursor cursor = db.query("user", null, String.format("userName=\'%s\'", userName),
                null, null, null, null);
        if (!cursor.moveToFirst()){
            errorResult = "用户不存在";
            cursor.close();
            return null;
        }
        if (cursor.getInt(cursor.getColumnIndex("password")) != hash(password)){
            errorResult = "密码错误";
            cursor.close();
            return null;
        }
        User user = new User();
        user.userId = cursor.getInt(cursor.getColumnIndex("userId"));
        user.userName = userName;
        user.stuId = cursor.getString(cursor.getColumnIndex("stuId"));
        user.college = cursor.getString(cursor.getColumnIndex("college"));
        user.gender = cursor.getString(cursor.getColumnIndex("gender"));
        user.ruleBreak = cursor.getInt(cursor.getColumnIndex("ruleBreak"));
        cursor.close();
        return user;
    }

    //检查用户名是否被注册
    public boolean isUserNameUsed(String userName){
        Cursor cursor = db.query("user", null, String.format("userName=\'%s\'", userName),
                null, null, null, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        else{
            cursor.close();
            return true;
        }
    }

    //检查学号是否已被注册
    public boolean isStuIdRegistered(String stuId){
        Cursor cursor = db.query("user", null, String.format("stuId=\'%s\'", stuId),
                null, null, null, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        else{
            cursor.close();
            return true;
        }
    }

    //注册操作
    public void signUp(User user, String password){
        ContentValues cValue = new ContentValues();
        cValue.put("userName", user.userName);
        cValue.put("password", hash(password));
        cValue.put("stuId", user.stuId);
        cValue.put("college", user.college);
        cValue.put("gender", user.gender);
        db.insert("user", null, cValue);
    }

    //获取所有体育场馆
    public List<Stadium> getAllStadiums(){
        Cursor cursor = db.query("stadium", null, null,
                null, null, null, null);
        List<Stadium> stadiums = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            Stadium stadium = new Stadium();
            stadium.StadiumId = cursor.getInt(cursor.getColumnIndex("stadiumId"));
            stadium.StadiumName = cursor.getString(cursor.getColumnIndex("stadiumName"));
            stadium.Details = cursor.getString(cursor.getColumnIndex("details"));
            stadium.mode = cursor.getInt(cursor.getColumnIndex("mode"));
            stadium.QRCode = cursor.getString(cursor.getColumnIndex("QRCode"));
            stadium.longitude = cursor.getFloat(cursor.getColumnIndex("longitude"));
            stadium.latitude = cursor.getFloat(cursor.getColumnIndex("latitude"));
            stadium.startTime = cursor.getString(cursor.getColumnIndex("starttime"));
            stadium.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
            stadium.longestTime = cursor.getInt(cursor.getColumnIndex("longesttime"));
            stadium.address = cursor.getString(cursor.getColumnIndex("address"));
            stadiums.add(stadium);
        }
        cursor.close();
        return stadiums;
    }

    //检查用户是否当天已有预约
    public boolean isUserHasAppoint(int userId, String date){
        Cursor cursor = db.query("book", null,
                String.format("userId=%d and bookDate=\'%s\' and mode != 3", userId, date),
                        null, null, null, null);
        return cursor.moveToFirst();
    }

    //添加预约
    public void addAppointByUser(ContentValues appointValue){
        db.insert("book", null, appointValue);
    }

    //获取用户的所有预约
    public List<Appoint> getAppointsByUser(int userId, boolean notif){
        Cursor cursor;
        //获取预约提醒
        if (notif){
            cursor = db.query("book", null,
                    String.format("userId=%d and mode<=1", userId),
                    null, null, null, null);
        }
        //获取预约记录
        else{
            cursor = db.query("book", null,
                    String.format("userId=%d and mode>1", userId),
                    null, null, null, null);
        }
        List<Appoint> appoints = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            Appoint appoint = new Appoint();
            appoint.ReservationId = cursor.getInt(cursor.getColumnIndex("bookId"));
            appoint.userId = userId;
            appoint.stadiumId = cursor.getInt(cursor.getColumnIndex("stadiumId"));
            appoint.mode = cursor.getInt(cursor.getColumnIndex("mode"));
            Cursor tmpCursor = db.query("stadium", null,
                    String.format("stadiumId=%d", appoint.stadiumId),
                    null, null,null, null);
            tmpCursor.moveToFirst();
            appoint.stadiumName = tmpCursor.getString(
                    tmpCursor.getColumnIndex("stadiumName"));
            appoint.stadiumAdd = tmpCursor.getString(
                    tmpCursor.getColumnIndex("address")
            );
            appoint.longitude = tmpCursor.getFloat(
                    tmpCursor.getColumnIndex("longitude")
            );
            appoint.latitude = tmpCursor.getFloat(
                    tmpCursor.getColumnIndex("latitude")
            );
            tmpCursor.close();
            appoint.bookDate = cursor.getString(cursor.getColumnIndex("bookDate"));
            appoint.startTime = cursor.getString(cursor.getColumnIndex("starttime"));
            appoint.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
            appoint.submitDate = cursor.getString(cursor.getColumnIndex("submitDate"));
            appoints.add(appoint);
        }
        cursor.close();
        return appoints;
    }

    //求某个场馆某天的空闲时间链表
    public void getFreeTimeList(Stadium stadium, String date,
                                List<FreeTime> dateList
                                ){
        Cursor cursor = db.query("book",null,
                String.format("stadiumId=%d and bookDate=\'%s\' and mode<=1", stadium.StadiumId, date),
                null, null, null, null, null);
        List<FreeTime> dateBusy = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            FreeTime busyTime = new FreeTime();
            busyTime.start = new LocalTime(Utility.getDetailTime(Utility.strTimeToDate(
                    cursor.getString(cursor.getColumnIndex("starttime")))));
            busyTime.end = new LocalTime(Utility.getDetailTime(Utility.strTimeToDate(
                    cursor.getString(cursor.getColumnIndex("endtime")))));
            dateBusy.add(busyTime);
        }
        Collections.sort(dateBusy, new FreeTimeComparetor());
        FreeTime freeTime = new FreeTime();
        freeTime.start = new LocalTime(Utility.getDetailTime(
                Utility.strTimeToDate(stadium.startTime)));
        for (int i = 0; i < dateBusy.size(); i++){
            freeTime.end = dateBusy.get(i).start;
            if (Minutes.minutesBetween(freeTime.start, freeTime.end).getMinutes() >= 10)
                dateList.add(freeTime);
            freeTime = new FreeTime();
            freeTime.start = dateBusy.get(i).end;
        }
        freeTime.end = new LocalTime(Utility.getDetailTime(
                Utility.strTimeToDate(stadium.endTime)));
        if (Minutes.minutesBetween(freeTime.start, freeTime.end).getMinutes() >= 10)
            dateList.add(freeTime);
        cursor.close();
    }

    //取消一个预约
    public void cancelAppoint(int bookId){
        db.execSQL("UPDATE book SET mode=3 WHERE bookId=" + String.valueOf(bookId) + ";");
    }

    // 检查是否有违约情况
    public void findRuleBreak(){
        String today = Utility.getDate(new Date());
        Cursor cursor = db.query("book", null,
                String.format("mode=0 and bookDate=\'%s\'", today),
                null, null, null, null);
        if (!cursor.moveToFirst()){
            return;
        }
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            LocalTime now = new LocalTime(Utility.getDetailTime(new Date()));
            LocalTime startTime = new LocalTime(
                    cursor.getString(cursor.getColumnIndex("starttime"))
            );
            if (Minutes.minutesBetween(startTime, now).getMinutes() > 5){
                setRuleBreak(Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex("bookId"))
                ));
            }
        }
        cursor.close();
    }

    //设置某条预约违约
    public void setRuleBreak(int bookId){
        db.execSQL("UPDATE book SET mode=4 WHERE bookId=" + bookId + ";");
        Cursor cursor = db.query("book",null,
                String.format("bookId=%d" ,bookId),
                        null, null, null, null);
        cursor.moveToFirst();
        int userId = cursor.getInt(cursor.getColumnIndex("userId"));
        cursor.close();
        cursor = db.query("user", null,
                String.format("userId=%d", userId),
                null, null, null, null);
        cursor.moveToFirst();
        int ruleBreak = cursor.getInt(cursor.getColumnIndex("ruleBreak")) + 1;
        if (State.getState().getUser() != null) {
            State.getState().getUser().ruleBreak = ruleBreak;
        }
        cursor.close();
        db.execSQL("UPDATE user SET ruleBreak=" + ruleBreak + " WHERE userId=" + userId + ";");
    }

    //扫码签到处理
    public String scanQR(String scanResult){
        Cursor cursor;
        cursor = db.query("stadium", null,
                String.format("QRCode=\'%s\'",
                        scanResult
                ), null, null, null, null);
        if (!cursor.moveToFirst()){
            return "二维码无效";
        }
        int stadiumId = cursor.getInt(cursor.getColumnIndex("stadiumId"));
        cursor.close();
        String today = Utility.getDate(new Date());
        cursor = db.query("book", null,
                String.format("userId=%d AND stadiumId=%d AND bookDate=\'%s\'",
                        State.getState().getUser().userId,
                        stadiumId,
                        today
                        ),
                null, null, null, null);

        if (!cursor.moveToFirst()){
            return "您今天未预约该场馆";
        }
        int mode = cursor.getInt(cursor.getColumnIndex("mode"));
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            mode = cursor.getInt(cursor.getColumnIndex("mode"));
            if (mode != 3) break;
        }
        String bookId = cursor.getString(cursor.getColumnIndex("bookId"));
        if (mode == 0){
            LocalTime startTime = new LocalTime(
                    cursor.getString(cursor.getColumnIndex("starttime"))
            );
            LocalTime now = new LocalTime(Utility.getDetailTime(new Date()));
            if (Math.abs(Minutes.minutesBetween(now, startTime).getMinutes()) <= 5){
                db.execSQL("UPDATE book SET mode=1 WHERE bookId=" + bookId + ";");
                Appoint effectAppoint = new Appoint();
                effectAppoint.ReservationId = Integer.parseInt(bookId);
                effectAppoint.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
                State.getState().effectList.add(effectAppoint);
                cursor.close();
                return "签到成功";
            }
            else{
                cursor.close();
                return "签到失败,签到时间为开始时间前后五分钟";
            }
        }
        else if (mode == 1){
            List<Appoint> effectList = State.getState().effectList;
            for (int i = 0; i < effectList.size(); i++){
                if (Integer.parseInt(bookId) == effectList.get(i).ReservationId){
                    effectList.remove(i);
                    break;
                }
            }
            db.execSQL("UPDATE book SET mode=2 WHERE bookId=" + bookId + ";");
            cursor.close();
            return "释放占用场馆成功";
        }
        else if (mode == 2){
            cursor.close();
            return "预约已结束";
        }
        else if (mode == 3){
            cursor.close();
            return "预约已取消";
        }
        else if (mode == 4){
            cursor.close();
            return "您未及时签到或释放场馆,已违约";
        }
        return "";
    }

    //系统初起时, 返回生效中的预约链表
    public void getEffectList(List<Appoint> effectList){
        Cursor cursor = db.query("book", null,
                "mode=1", null, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToFirst();
            cursor.move(i);
            Appoint appoint = new Appoint();
            appoint.ReservationId = cursor.getInt(cursor.getColumnIndex("bookId"));
            appoint.endTime = cursor.getString(cursor.getColumnIndex("endtime"));
            effectList.add(appoint);
        }
        cursor.close();
    }

    //计算密码hash
    static private int hash(String str){
        int seed = 131;
        int tmp = 0;
        for (int i = 0; i < str.length(); i++) {
            tmp = tmp * seed + str.charAt(i);
        }
        return tmp & 0x7fffffff;
    }
    public String getErrorResult(){
        return errorResult;
    }
    public static SqlHelper getInstance(){
        return SqlHelperInstance;
    }

    //空闲时间块的定义
    public class FreeTime{
        public LocalTime start;
        public LocalTime end;
    }
    private class FreeTimeComparetor implements Comparator<FreeTime> {
        @Override
        public int compare(FreeTime f1, FreeTime f2){
            if (f1.start.isBefore(f2.start)) return -1;
            if (f1.start.isAfter(f2.start)) return 1;
            return 0;
        }
    }
}



