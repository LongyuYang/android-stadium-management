package com.example.hello.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import util.Appoint;
import util.SqlHelper;
import util.State;
import util.Utility;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
         Timer timer = new Timer();
         timer.schedule(new TimerTaskTest(), 0, 60*1000);
         SqlHelper.getInstance().getEffectList(
                 State.getState().effectList
         );
         return super.onStartCommand(intent, flags, startId);
    }

    public class TimerTaskTest extends java.util.TimerTask{
        @Override
        public void run() {
            /* 寻找未及时签到的预约 */
            SqlHelper.getInstance().findRuleBreak();

            /* 寻找未及时释放场馆的预约 */
            List<Appoint> effectList = State.getState().effectList;
            LocalTime now = new LocalTime(Utility.getDetailTime(new Date()));
            for (int i = 0; i < effectList.size(); i++){
                LocalTime endTime = new LocalTime(effectList.get(i).endTime);
                if (Minutes.minutesBetween(endTime, now).getMinutes() > 5){
                    SqlHelper.getInstance().setRuleBreak(effectList.get(i).ReservationId);
                    effectList.remove(i--);
                }
            }
        }
    }
}
