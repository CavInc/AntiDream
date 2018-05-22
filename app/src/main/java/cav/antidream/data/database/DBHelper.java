package cav.antidream.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cav on 17.05.18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;
    public static final String DATABASE_NAME = "alarm.db3";
    public static final String ALARM_TABLE = "alarm_table";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updatedDB(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        updatedDB(db,oldVersion,newVersion);
    }

    private void updatedDB(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion<1){
            db.execSQL("create table "+ALARM_TABLE+"(" +
                    "_id integer not null primary key AUTOINCREMENT," +
                    "name_alarm text," +
                    "datetime text," +
                    "stop_type integer," +
                    "alarm_size integer," +
                    "url_sound text," +
                    "alarm_volume integer," + // громкость
                    "used integer)");
        }
    }
}
