package cav.antidream.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //для демонстрации запустим только один тип уведомлений при перезапуске
            //NotificationHelper.scheduleRepeatingElapsedNotification(context);
            Intent service = new Intent(context,StartAlarmInReboot.class);
            context.startService(service);
        }
    }
}
