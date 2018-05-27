package cav.antidream.ui.activity;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.ui.dialogs.SelectSoundAlarmDialog;
import cav.antidream.ui.dialogs.SetNameDialog;
import cav.antidream.utils.ConstantManager;
import cav.antidream.utils.Utils;

public class AddAlarm extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{

    private TimePicker mTime;
    private DatePicker mDate;
    private Spinner mAlarmStopTypeSpinner;
    private SeekBar mSeekBar;

    private TextView mNameAlarm;

    private MaterialCalendarView mCalendarView;

    private TextView mSetSound;

    private int alarmVolume = 100;

    private String mAlarmName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mTime = (TimePicker) findViewById(R.id.timePicker);
        //mDate = (DatePicker) findViewById(R.id.datePicker);

        mSetSound = (TextView) findViewById(R.id.select_sound);
        findViewById(R.id.select_sound_lv).setOnClickListener(this);


        mSeekBar = (SeekBar) findViewById(R.id.volime_seek);
        mSeekBar.setOnSeekBarChangeListener(this);

        mNameAlarm = (TextView) findViewById(R.id.alarm_name);

        findViewById(R.id.set_name_alarm).setOnClickListener(this);

        mAlarmStopTypeSpinner = (Spinner) findViewById(R.id.alarm_stop_type);

        mTime.setIs24HourView(true);
        mTime.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        Calendar newYear = Calendar.getInstance();
        newYear.add(Calendar.YEAR, 1);
        mCalendarView = (MaterialCalendarView) findViewById(R.id.data_calendar);

        mCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2016,12,31))
                .setMaximumDate(newYear)
                .commit();
        mCalendarView.setCurrentDate(new Date());
        mCalendarView.setDateSelected(new Date(),true);


        ArrayAdapter<String> stopTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ConstantManager.ALARM_STOP_TYPE);
        stopTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAlarmStopTypeSpinner.setAdapter(stopTypeAdapter);

        setupToolbar();
    }

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(mFileName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addalarm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.save_item) {
            if (!saveData()) return false;
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveData(){
        int h;
        int m;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            h = mTime.getHour();
            m = mTime.getMinute();
        } else {
            h = mTime.getCurrentHour();
            m = mTime.getCurrentMinute();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());


        CalendarDay cd = mCalendarView.getSelectedDate();
        int year = cd.getYear();
        int day = cd.getDay();
        int month = cd.getMonth();

        /*
        int day = mDate.getDayOfMonth();
        int month = mDate.getMonth();
        int year = mDate.getYear();
        */

        Date date = new GregorianCalendar(year,month,day,h,m).getTime();

        int id = (int) mAlarmStopTypeSpinner.getSelectedItemId();

        if (date.before(c.getTime())) {
            Toast.makeText(this,"Дата или время меньше текущей(го)",Toast.LENGTH_LONG).show();
            return false;
        }

        if(Utils.dateRemoveTime(cd.getDate()).compareTo(Utils.dateRemoveTime(c.getTime())) == 0 ){
            if ( h<c.get(Calendar.HOUR_OF_DAY) ){
                Toast.makeText(this,"Время меньше текущего",Toast.LENGTH_LONG).show();
                return false;
            }
        }

        AlarmModel data = new AlarmModel(mAlarmName,date, alarmVolume,id,urlSound);

        DBConnect db = new DBConnect(this);
        int rec_id = db.storeAlarm(data);
        data.setId(rec_id);


        // а тут должны запустить будильник
        Utils.setAlarm(this,data);
        Toast.makeText(this,"Будильник включен",Toast.LENGTH_LONG).show();
        return true;
    }

  //  https://habr.com/sandbox/69154/
 //   https://ru.stackoverflow.com/questions/473690/seekbar-%D0%BA%D0%B0%D1%81%D1%82%D0%BE%D0%BC%D0%BD%D1%8B%D0%B9-background-progress
  //  http://www.zoftino.com/android-seekbar-and-custom-seekbar-examples
 //   https://www.youtube.com/watch?v=iHOW15y-8U4


    @Override
    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
        alarmVolume = progressValue;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (alarmVolume == 1) alarmVolume = 20;
        if (alarmVolume == 2) alarmVolume = 40;
        if (alarmVolume == 3) alarmVolume = 60;
        if (alarmVolume == 4) alarmVolume = 80;
        if (alarmVolume == 5) alarmVolume = 100;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.select_sound_lv) {
            SelectSoundAlarmDialog dialog = new SelectSoundAlarmDialog();
            dialog.setOnSoundChangeListener(mSoundChangeListener);
            dialog.show(getSupportFragmentManager(), "SQ");
        }
        if (view.getId() == R.id.set_name_alarm){
            SetNameDialog dialog = new SetNameDialog();
            dialog.setOnSetNameLisneter(mOnSetNameLisneter);
            dialog.show(getSupportFragmentManager(),"SN");
        }
    }

    private String urlSound;

    SelectSoundAlarmDialog.OnSoundChangeListener mSoundChangeListener = new SelectSoundAlarmDialog.OnSoundChangeListener() {
        @Override
        public void onSoundChange(String title, String url) {
            mSetSound.setText(title);
            urlSound = url;
        }
    };

    SetNameDialog.OnSetNameLisneter mOnSetNameLisneter = new SetNameDialog.OnSetNameLisneter() {
        @Override
        public void SetName(String name) {
            mAlarmName = name;
            mNameAlarm.setText(mAlarmName);
        }
    };

}
