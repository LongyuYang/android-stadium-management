package util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    static public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    static public String getDetailTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }
    static public String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    static public Date strTimeToDate(String time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            return timeFormat.parse(time);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    static public String getBookMode(int mode) {
        if (mode == 0) return "未生效";
        else if (mode == 1) return "生效中";
        else if (mode == 2) return "已结束";
        else if (mode == 3) return "已取消";
        else if (mode == 4) return "违约";
        else return "";
    }
}
