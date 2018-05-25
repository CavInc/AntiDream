package cav.antidream.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.ui.activity.AlarmSignalActivity;
import cav.antidream.utils.ConstantManager;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AlarmTaskReciver extends BroadcastReceiver {

    private Context mContext;
    private int mId;
    private AlarmModel mModel;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mId = intent.getIntExtra(ConstantManager.ALARM_ID,-1);
        getModel();
        //showNotification(mContext);
        startAlarm();
    }

    private void getModel(){
        DBConnect db = new DBConnect(mContext);
        mModel = db.getOneAlarmRec(mId);
    }

    private void showNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = null;

        Notification.Builder builder = new Notification.Builder(context);

        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getActivity(context,mId,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.icon_alarm_p)
                .setTicker("Задолженность")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Задолженность")
                .setContentText(mModel.getAlarmName())
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < 16){
            notification = builder.getNotification(); // до API 16
        }else{
            notification = builder.build();
        }

        notificationManager.notify(ConstantManager.NOTIFY_ID+mId, notification);

    }

    private void startAlarm(){
        Intent intent = new Intent(mContext, AlarmSignalActivity.class);
        intent.putExtra("URL_SOUND",mModel.getAlarmUrlMelodu());
        intent.putExtra("TYPE_ALARM",mModel.getAlarmStopType());
        intent.putExtra("SIZE_ALARM",mModel.getAlarmSize());
        intent.putExtra("ALARM_ID",mModel.getId());
        intent.putExtra("ALARM_VOLUME",mModel.getAlarmVolume());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
