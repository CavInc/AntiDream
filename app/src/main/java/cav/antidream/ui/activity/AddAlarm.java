package cav.antidream.ui.activity;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.utils.ConstantManager;
import cav.antidream.utils.Utils;

public class AddAlarm extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private TimePicker mTime;
    private DatePicker mDate;
    private EditText mNameAlarm;
    private Spinner mAlarmStopTypeSpinner;
    private SeekBar mSeekBar;

    private int alarmSize = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        mTime = (TimePicker) findViewById(R.id.timePicker);
        mDate = (DatePicker) findViewById(R.id.datePicker);

        mNameAlarm = (EditText) findViewById(R.id.alarm_name);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mAlarmStopTypeSpinner = (Spinner) findViewById(R.id.alarm_stop_type);

        mTime.setIs24HourView(true);
        mTime.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

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

        int day = mDate.getDayOfMonth();
        int month = mDate.getMonth();
        int year = mDate.getYear();

        Date date = new GregorianCalendar(year,month,day,h,m).getTime();

        int id = (int) mAlarmStopTypeSpinner.getSelectedItemId();

        AlarmModel data = new AlarmModel(mNameAlarm.getText().toString()
                ,date,alarmSize,id,"");

        DBConnect db = new DBConnect(this);
        int rec_id = db.storeAlarm(data);
        data.setId(rec_id);


        // а тут должны запустить будильник
        Utils.setAlarm(this,data);
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
}
