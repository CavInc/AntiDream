package cav.antidream.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.ui.adapters.HistoryAlarmAdapter;

public class HistoryAlarm extends AppCompatActivity {
    private ListView mListView;

    private HistoryAlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_alarm);

        mListView = (ListView) findViewById(R.id.history_list);

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
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(model);
            mAdapter.notifyDataSetChanged();
        }
    }
}
