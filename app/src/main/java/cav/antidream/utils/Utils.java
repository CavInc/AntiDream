package cav.antidream.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.antidream.data.models.AlarmModel;
import cav.antidream.services.AlarmTaskReciver;

/**
 * Created by cav on 17.05.18.
 */

public class Utils {

    public static String dateToStr(String mask,Date date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        return format.format(date);
    }

    public static Date StrToDate(String mask,String date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        try {
            return  format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setAlarm(Context context,AlarmModel model,boolean mode){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmTaskReciver.class);
        intent.putExtra(ConstantManager.ALARM_ID,model.getId());
        // добавить констану ?
        PendingIntent pi= PendingIntent.getBroadcast(context,model.getId(), intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar c = Calendar.getInstance();
        c.setTime(model.getAlarmDate());
        if (mode) {
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        } else {
            am.cancel(pi);
        }
    }

    public static Date dateRemoveTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
