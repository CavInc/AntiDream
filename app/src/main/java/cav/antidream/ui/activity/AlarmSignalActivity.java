package cav.antidream.ui.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cav.antidream.R;
import cav.antidream.data.database.DBConnect;
import cav.antidream.utils.ConstantManager;
import cav.antidream.utils.Utils;

public class AlarmSignalActivity extends AppCompatActivity {
    private TextView mTime;
    private TextView mDate;

    private String urlSound;
    private int alarm_id;
    private int alarm_size;
    private int alarm_type;

    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock fullWakeLock;
    private PowerManager.WakeLock partialWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //getWindow().setFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
                //mDecorView = getWindow().getDecorView();
                //hideSystemUI();
        }


        setContentView(R.layout.activity_alarm_signal);

        mDate = (TextView) findViewById(R.id.main_date);
        mTime = (TextView) findViewById(R.id.main_time);


        mDate.setText(Utils.dateToStr("EEE, dd MMM",new Date()));
        mTime.setText(Utils.dateToStr("HH:mm",new Date()));

        urlSound = getIntent().getStringExtra("URL_SOUND");
        alarm_id = getIntent().getIntExtra("ALARM_ID",-1);
        alarm_type = getIntent().getIntExtra("TYPE_ALARM", -1);
        alarm_size = getIntent().getIntExtra("SIZE_ALARM",0);

        SwipeButton enableButton = (SwipeButton) findViewById(R.id.stop_alarm);

        /*
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Toast.makeText(AlarmSignalActivity.this, "State: " + active, Toast.LENGTH_SHORT).show();
            }
        });
        */

        enableButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                Toast.makeText(AlarmSignalActivity.this, "Active!", Toast.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopMusic();
                finish();
            }
        });

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        //wakeUp();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DBConnect dbConnect = new DBConnect(this);
        dbConnect.setStopUser(alarm_id,false);
        startMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    private void startMusic(){
        if (urlSound!=null && urlSound.length()!=0) {
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(this, Uri.parse(urlSound));
                mMediaPlayer.prepare();
                mMediaPlayer.setVolume(1.0f, 1.0f);
                mMediaPlayer.setScreenOnWhilePlaying(true); // не дает уснуть во премя воспроизведениея ?
                mMediaPlayer.setLooping(true); // зациклим до окончания работы активности
                int duration = mMediaPlayer.getDuration();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void stopMusic(){
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mediaPlayer.isPlaying()) {

            }
        }
    };

    @Override
    public void onBackPressed() {
        if (alarm_type == ConstantManager.ALARM_STOP_KEY) {
            super.onBackPressed();
        }
    }

    // экран
    //http://android.okhelp.cz/turn-screen-on-off-android-sample-code/
    //https://habr.com/company/appsministry/blog/168573/


    //http://findevelop.blogspot.ru/2012/07/drag-and-drop-android.html
    //http://startandroid.ru/ru/uroki/vse-uroki-spiskom/167-urok-102-touch-obrabotka-kasanija.html
    //http://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // нажатие
                    break;
                case MotionEvent.ACTION_MOVE: // движение
                    break;
                case MotionEvent.ACTION_UP: // отпускание
                    break;
            }
            return true;
        }
    };

    // Called from onCreate
    // protected void createWakeLocks(){
    // PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    // fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
    // partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK"); }

    // Called implicitly when device is about to sleep or application is backgrounded
    // protected void onPause(){
    // super.onPause();
    // partialWakeLock.acquire(); }

    // Called implicitly when device is about to wake up or foregrounded
    // protected void onResume(){
    // super.onResume();
    // if(fullWakeLock.isHeld()){
    // fullWakeLock.release(); }
    // if(partialWakeLock.isHeld())
    // { partialWakeLock.release(); } }

    // Called whenever we need to wake up the device
    // public void wakeDevice() {
    // fullWakeLock.acquire();
    // KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    // KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG"); keyguardLock.disableKeyguard(); }

    // будим устройство
    private void wakeUp(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),"ALARM");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ALARM - PARTIAL WAKE LOCK");
        if (fullWakeLock.isHeld()) {
            fullWakeLock.release();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("KEY CODE ","PRESSED KEY - "+keyCode);
        if (alarm_type == ConstantManager.ALARM_STOP_KEY) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
