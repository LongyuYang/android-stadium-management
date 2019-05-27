package util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Context.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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
        db.execSQL("create table if not exists user(userId integer primary key autoincrement," +
                " userName text not null, password integer not null, stuId text, college text, birthday data," +
                "gender varchar(5), ruleBreak integer default 0);");
        Cursor cursor = db.query("user", null, null,
                null, null, null, null);
        if (!cursor.moveToFirst()){
            ContentValues cValue = new ContentValues();
            cValue.put("userName", "root");
            cValue.put("password", hash("123"));
            cValue.put("stuId", "0000000");
            db.insert("user", null, cValue);
        }

        errorResult = "";
        cursor.close();
        isInitialized = true;
    }

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
    public void signUp(User user, String password){
        ContentValues cValue = new ContentValues();
        cValue.put("userName", user.userName);
        cValue.put("password", hash(password));
        cValue.put("stuId", user.stuId);
        cValue.put("college", user.college);
        cValue.put("gender", user.gender);
        db.insert("user", null, cValue);
    }
    static private int hash(String str){
        int seed = 131;
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * seed + str.charAt(i);
        }
        return hash & 0x7fffffff;
    }
    public String getErrorResult(){
        return errorResult;
    }
    public static SqlHelper getInstance(){
        return SqlHelperInstance;
    }
}
