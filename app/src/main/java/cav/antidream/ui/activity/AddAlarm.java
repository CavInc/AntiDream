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
import android.widget.EditText;
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
import cav.antidream.ui.SelectSoundAlarmDialog;
import cav.antidream.utils.ConstantManager;
import cav.antidream.utils.Utils;

public class AddAlarm extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{

    private TimePicker mTime;
    private DatePicker mDate;
    private EditText mNameAlarm;
    private Spinner mAlarmStopTypeSpinner;
    private SeekBar mSeekBar;

    private MaterialCalendarView mCalendarView;

    private TextView mSetSound;

    private int alarmSize = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mTime = (TimePicker) findViewById(R.id.timePicker);
        //mDate = (DatePicker) findViewById(R.id.datePicker);

        mSetSound = (TextView) findViewById(R.id.select_sound);
        mSetSound.setOnClickListener(this);

        mNameAlarm = (EditText) findViewById(R.id.alarm_name);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
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

        mSeekBar.setOnSeekBarChangeListener(this);
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
            saveData();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData(){
        int h;
        int m;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            h = mTime.getHour();
            m = mTime.getMinute();
        } else {
            h = mTime.getCurrentHour();
            m = mTime.getCurrentMinute();
        }

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

        AlarmModel data = new AlarmModel(mNameAlarm.getText().toString()
                ,date,alarmSize,id,urlSound);

        DBConnect db = new DBConnect(this);
        int rec_id = db.storeAlarm(data);
        data.setId(rec_id);


        // а тут должны запустить будильник
        Utils.setAlarm(this,data);
        Toast.makeText(this,"Будильник включен",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
        alarmSize = progressValue;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        SelectSoundAlarmDialog dialog = new SelectSoundAlarmDialog();
        dialog.setOnSoundChangeListener(mSoundChangeListener);
        dialog.show(getSupportFragmentManager(),"SQ");
    }

    private String urlSound;

    SelectSoundAlarmDialog.OnSoundChangeListener mSoundChangeListener = new SelectSoundAlarmDialog.OnSoundChangeListener() {
        @Override
        public void onSoundChange(String title, String url) {
            mSetSound.setText("Выберите мелодию: "+ title);
            urlSound = url;
        }
    };

}
