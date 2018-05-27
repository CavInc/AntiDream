package cav.antidream.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.widget.Toast;

import java.util.ArrayList;

import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.utils.Utils;

public class StartAlarmInReboot extends Service {
    public StartAlarmInReboot() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Ребут !!!!",Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    private void restartAlarm(){
        DBConnect dbConnect = new DBConnect(getBaseContext());
        ArrayList<AlarmModel> data = dbConnect.getUsedAlarmRec();
        for (AlarmModel l : data){
            Utils.setAlarm(getBaseContext(),l);
        }
    }

}
