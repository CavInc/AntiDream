package cav.antidream.ui.activity;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.ui.adapters.HistoryAlarmAdapter;
import cav.antidream.utils.Utils;

public class HistoryAlarm extends AppCompatActivity implements AdapterView.OnItemLongClickListener,View.OnClickListener {
    private ListView mListView;

    private HistoryAlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_alarm);

        mListView = (ListView) findViewById(R.id.history_list);
        mListView.setOnItemLongClickListener(this);

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
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(){
        DBConnect db = new DBConnect(this);
        ArrayList<AlarmModel> model = db.getAlarmModel();
        if (mAdapter == null) {
            mAdapter = new HistoryAlarmAdapter(this,R.layout.history_item,model);
            mAdapter.setHistoryAlarmCheckChange(mAlarmCheckChange);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(model);
            mAdapter.notifyDataSetChanged();
        }
    }

    private AlarmModel selectRec;

    private Dialog dialog;

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("HA","LONG");
         // TODO показывать что надо редактировать или удалить
        AlarmModel rec = (AlarmModel) adapterView.getItemAtPosition(position);
        selectRec = rec;

        dialog = new Dialog(this);
        dialog.setTitle("Действие над будильником");
        dialog.setContentView(R.layout.dialog_history_item);

        LinearLayout mEditLayout = (LinearLayout) dialog.findViewById(R.id.edit_laout);
        LinearLayout mDelLayout = (LinearLayout) dialog.findViewById(R.id.del_laout);
        mEditLayout.setOnClickListener(this);
        mDelLayout.setOnClickListener(this);
        dialog.show();

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.del_laout) {
            DBConnect dbConnect = new DBConnect(this);
            dbConnect.delAlarm(selectRec.getId());
            mAdapter.remove(selectRec);
            mAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    HistoryAlarmAdapter.HistoryAlarmCheckChange mAlarmCheckChange = new HistoryAlarmAdapter.HistoryAlarmCheckChange() {
        @Override
        public void CheckChange(AlarmModel model, boolean mode) {
            if (mode) {
                // запускаем будильник по новой
                Date currentDate = new Date();
                Date date = model.getAlarmDate();

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                int currentDay = c.get(Calendar.DAY_OF_WEEK);

                c.setTime(date);
                c.setFirstDayOfWeek(Calendar.MONDAY);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                int h = c.get(Calendar.HOUR_OF_DAY);
                int m = c.get(Calendar.MINUTE);

                c.setTime(currentDate); // для того что бы сдвинуть
                Log.d("HA","DAY :"+dayOfWeek+" CURDAY :"+currentDay);

                if (dayOfWeek == currentDay) {
                    // одинаковый день недели ставим будильник на сегодня
                    currentDate.setHours(h);
                    currentDate.setMinutes(m);
                    model.setAlarmDate(currentDate);
                }
                if (dayOfWeek == Calendar.SUNDAY) dayOfWeek = 8;

                if (dayOfWeek > currentDay) {
                    // на оставшийся конец недели
                    c.add(Calendar.DAY_OF_MONTH,dayOfWeek-currentDay);
                    c.set(Calendar.HOUR_OF_DAY,h);
                    c.set(Calendar.MINUTE,m);
                    model.setAlarmDate(c.getTime());
                } else if (dayOfWeek<currentDay){
                   c.add(Calendar.DAY_OF_MONTH,dayOfWeek+7-currentDay);
                    c.set(Calendar.HOUR_OF_DAY,h);
                    c.set(Calendar.MINUTE,m);
                    model.setAlarmDate(c.getTime());
                }
                Utils.setAlarm(HistoryAlarm.this,model);
                DBConnect dbConnect = new DBConnect(HistoryAlarm.this);
                dbConnect.setStopUsed(model.getId(),true);

                Toast.makeText(HistoryAlarm.this,"Будильник включен",Toast.LENGTH_LONG);
            }
        }
    };
}
