package cav.antidream.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cav.antidream.data.models.AlarmModel;
import cav.antidream.utils.Utils;

/**
 * Created by cav on 17.05.18.
 */

public class DBConnect {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;

    public DBConnect(Context context){
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public void close(){
        if (database!=null) {
            database.close();
        }
    }

    public int storeAlarm(AlarmModel data){
        open();
        // TODO добавить параметры
        ContentValues values = new ContentValues();
        values.put("name_alarm ",data.getAlarmName());
        values.put("stop_type",data.getAlarmStopType());
        values.put("datetime", Utils.dateToStr("yyyy-MM-dd HH:mm",data.getAlarmDate()));
        values.put("alarm_size",data.getAlarmSize());
        int rec_id = (int) database.insert(DBHelper.ALARM_TABLE,null,values);
        close();
        return rec_id;
    }

    public ArrayList<AlarmModel> getAlarmModel(){
        // TODO исправить
        ArrayList<AlarmModel> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.ALARM_TABLE,
                new String[]{"_id","name_alarm","datetime","stop_type","alarm_size"},null,null,null,null,"_id");
        while (cursor.moveToNext()){
            rec.add(new AlarmModel(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name_alarm")),
                    Utils.StrToDate("yyyy-MM-dd HH:mm",cursor.getString(cursor.getColumnIndex("datetime"))),
                    cursor.getInt(cursor.getColumnIndex("alarm_size")),
                    cursor.getInt(cursor.getColumnIndex("stop_type")),
                    "",
                    true
            ));
        }
        close();
        return rec;
    }

    public AlarmModel getOneAlarmRec(int id){
        open();
        Cursor cursor = database.query(DBHelper.ALARM_TABLE,
                new String[]{"_id","name_alarm","datetime","stop_type","alarm_size"},"_id="+id,null,null,null,"_id");
        cursor.moveToFirst();
        AlarmModel rec = new AlarmModel(
                cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("name_alarm")),
                Utils.StrToDate("yyyy-MM-dd HH:mm", cursor.getString(cursor.getColumnIndex("datetime"))),
                cursor.getInt(cursor.getColumnIndex("alarm_size")),
                cursor.getInt(cursor.getColumnIndex("stop_type")),
                "",
                true
        );
        close();
        return rec;
    }

}
