package com.example.chillaxown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class TimerActivity extends AppCompatActivity {

    //widget variable declaration
    private FloatingActionButton mStopBtn, mSettingsBtn, mPauseBtn, mStartBtn;
    private TextView mTaskTextView, mCountDownTextView;
    private TaskDetails currentTask;
    private static final int durationID = 1;
    private boolean isTimerRunning = false;

    private CountDownTimer timer;
    // 25 mins default
    private static final long START_TIME_IN_MILLIS = 1500000;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private long mEndTime;
    MaterialProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(83,36,54)));
        getSupportActionBar().setTitle("Pomodoro Timer");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // get the object that is passed through the intent
        currentTask = bundle.getParcelable("taskObj");
        Log.i("BEFORE_EDIT", currentTask.toString());

        getSupportActionBar().setTitle("Pomodoro Timer");  // provide compatibility to all the versions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStartBtn            = (FloatingActionButton) findViewById(R.id.fab_start);
        mPauseBtn            = (FloatingActionButton) findViewById(R.id.fab_pause);
        mStopBtn             = (FloatingActionButton) findViewById(R.id.fab_stop);

        mCountDownTextView   = (TextView)  findViewById(R.id.textView_countdown);
        mTaskTextView        = (TextView)  findViewById(R.id.textView);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.progress_countdown);
        mTaskTextView.setText("Ongoing Task: " + currentTask.getTaskName());

        mProgressBar.setMax((int)mTimeLeftInMillis);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        //sendPushNotification("Timer is running in the background.");
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        timer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                updateCountDownText();
                Log.i("TIME", Long.toString(mTimeLeftInMillis));
                if(Math.abs(1200000 - mTimeLeftInMillis) <= 1500) {
                    Log.i("PUSH", "SEND NOTIFICATION");
                    sendPushNotification("Time to take a short break and drink some water!");
                }
                // triggered every 5 minutes
                //if(mTimeLeftInMillis == 1200000 || mTimeLeftInMillis == 900000 || mTimeLeftInMillis == 600000 || mTimeLeftInMillis == 300000) {
                //    Log.i("PUSH", "SEND NOTIFICATION");
                //    sendPushNotification("5 minutes has passed! Drink some water and take a break.");
                //}
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                mProgressBar.setProgress(0, true);
                sendPushNotification("25 minutes has passed! Take a long break and relax.");
            }
        }.start();

        isTimerRunning = true;
    }

    private void pauseTimer() {
        timer.cancel();
        isTimerRunning = false;
        Toast.makeText(this,  "Timer Paused!", Toast.LENGTH_LONG).show();
    }

    private void resetTimer() {
        addDurationToDB();
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        pauseTimer();
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds =  (int) (mTimeLeftInMillis / 1000)  % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mCountDownTextView.setText(timeLeftFormatted);
        mProgressBar.setProgress((int) (START_TIME_IN_MILLIS - mTimeLeftInMillis), true);
    }


    private void addDurationToDB() {
        long timePassed = START_TIME_IN_MILLIS - mTimeLeftInMillis;
        Duration duration = new Duration(durationID, currentTask.getTaskCategory(), timePassed);
        Log.i("DURATION", duration.toString());
        TaskDetailsSQL sql = new TaskDetailsSQL(this);

        int result = sql.insertDuration(duration);
        if(result != -1) {
            Log.i("DURATION", "Duration Successfully added!");
        } else {
            Log.i("DURATION", "Add database failed!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", isTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();
    }
    **/

    /**
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        isTimerRunning = prefs.getBoolean("timerRunning", false);


        mProgressBar.setMax((int)mTimeLeftInMillis);
        updateCountDownText();

        if(isTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                isTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }
    **/

    private void sendPushNotification(String message) {
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        intent.putExtra("test", "I am a String");
        Calendar now = Calendar.getInstance();

        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("Take a break!")
                .content(message)
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(intent, "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(intent, "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();

        //Snackbar.make(getWindow().getDecorView().getRootView(), "Remainder has been saved!", Snackbar.LENGTH_LONG)
        //        .show();
    }
}
