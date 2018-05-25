package cav.antidream.data.models;


import java.util.Date;

/**
 * Created by cav on 17.05.18.
 */

public class AlarmModel {
    private int mId;
    private String mAlarmName;
    private Date mAlarmDate; // совместить дату время ?
    private int mAlarmSize; // продолжительность сигнала
    private int mAlarmStopType; // Отключение будильника
    private String mAlarmUrlMelodu; // мелодия будильника
    private int mAlarmVolume; // гормкость
    private boolean mUsed; // в работе

    public AlarmModel(int id, String alarmName, Date alarmDate, int alarmSize, int alarmStopType, String alarmUrlMelodu, boolean used) {
        mId = id;
        mAlarmName = alarmName;
        mAlarmDate = alarmDate;
        mAlarmSize = alarmSize;
        mAlarmStopType = alarmStopType;
        mAlarmUrlMelodu = alarmUrlMelodu;
        mUsed = used;
    }

    public AlarmModel(String alarmName, Date alarmDate, int alarmVolume, int alarmStopType, String alarmUrlMelodu) {
        mAlarmName = alarmName;
        mAlarmDate = alarmDate;
        mAlarmVolume = alarmVolume;
        mAlarmStopType = alarmStopType;
        mAlarmUrlMelodu = alarmUrlMelodu;
        mUsed = true;
    }

    public AlarmModel(String alarmName, Date alarmDate, int alarmSize, int alarmStopType, String alarmUrlMelodu, boolean used) {
        mAlarmName = alarmName;
        mAlarmDate = alarmDate;
        mAlarmSize = alarmSize;
        mAlarmStopType = alarmStopType;
        mAlarmUrlMelodu = alarmUrlMelodu;
        mUsed = used;
    }

    public AlarmModel(int id, String alarmName, Date alarmDate, int alarmSize, int alarmStopType, String alarmUrlMelodu, int alarmVolume, boolean used) {
        mId = id;
        mAlarmName = alarmName;
        mAlarmDate = alarmDate;
        mAlarmSize = alarmSize;
        mAlarmStopType = alarmStopType;
        mAlarmUrlMelodu = alarmUrlMelodu;
        mAlarmVolume = alarmVolume;
        mUsed = used;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getAlarmName() {
        return mAlarmName;
    }

    public Date getAlarmDate() {
        return mAlarmDate;
    }

    public int getAlarmSize() {
        return mAlarmSize;
    }

    public int getAlarmStopType() {
        return mAlarmStopType;
    }

    public String getAlarmUrlMelodu() {
        return mAlarmUrlMelodu;
    }

    public boolean isUsed() {
        return mUsed;
    }

    public int getAlarmVolume() {
        return mAlarmVolume;
    }

    public void setAlarmDate(Date date) {
        mAlarmDate = date;
    }
}
