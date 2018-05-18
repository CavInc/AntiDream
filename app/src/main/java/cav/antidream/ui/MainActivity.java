package cav.antidream.ui;

import android.content.Intent;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cav.antidream.R;
import cav.antidream.ui.activity.AddAlarm;
import cav.antidream.ui.activity.HistoryAlarm;
import cav.antidream.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTime;
    private TextView mDate;
    private FloatingActionMenu mFabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate = (TextView) findViewById(R.id.main_date);
        mTime = (TextView) findViewById(R.id.main_time);

        ((ImageView) findViewById(R.id.add_alarm)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.history_alarm)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.exit)).setOnClickListener(this);

        mFabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        mDate.setText(Utils.dateToStr("EEE, dd MMM",new Date()));
        mTime.setText(Utils.dateToStr("HH:mm",new Date()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimerCount();
    }

    private void startTimerCount(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    runOnUiThread(refreshTime);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Runnable refreshTime = new Runnable() {
        @Override
        public void run() {
            mTime.setText(Utils.dateToStr("HH:mm",new Date()));
        }
    };

    @Override
    public void onClick(View view) {
        mFabMenu.close(true);

        switch (view.getId()){
            case R.id.exit:
                finish();
                break;
            case R.id.add_alarm:
                Intent addIntent = new Intent(this, AddAlarm.class);
                startActivity(addIntent);
                break;
            case R.id.history_alarm:
                Intent intent = new Intent(this, HistoryAlarm.class);
                startActivity(intent);
                break;
        }

    }
}
