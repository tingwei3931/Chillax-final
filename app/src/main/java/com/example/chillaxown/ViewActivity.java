package com.example.chillaxown;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ViewActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    TaskDetails task, currentTask;
    //widget variable declaration
    private Button mClearBtn, mEditBtn;
    private EditText mNameEditText, mLocationEditText, mReporterNameEditText, mDateEditText, mTimeEditText, mCategoryEditText;
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    FragmentManager fm = getFragmentManager();
    private FragmentActivity myContext;
    //constant variable to denote datePickerDialog or timePickerDialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Button btnNotify = findViewById(R.id.editBtn);
        //get intent from DetailFragment
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // get the object that is passed through the intent
        currentTask = bundle.getParcelable("taskObj");
        Log.i("BEFORE_EDIT", currentTask.toString());
        //set references to widgets
        //mClearBtn             = (Button)   findViewById(R.id.clearBtn);
        mEditBtn              = (Button)   findViewById(R.id.NoBtn);
        mNameEditText         = (EditText) findViewById(R.id.nameEditText);
        mDateEditText         = (EditText) findViewById(R.id.dateEditText);
        mTimeEditText         = (EditText) findViewById(R.id.timeEditText);
        mCategoryEditText     = (EditText) findViewById(R.id.categoryEditText);

        //Populate the view with current data
        mNameEditText.setText(currentTask.getTaskName());
        mDateEditText.setText(currentTask.getTaskDate());
        mTimeEditText.setText(currentTask.getTaskTime());
        mCategoryEditText.setText(currentTask.getTaskCategory());

        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),

                false

        );
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.show(getFragmentManager(),
                        "Datepickerdialog");
            }
        });


    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);

        Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
        intent.putExtra("test", "I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title( currentTask.getTaskCategory())
                .content(currentTask.getTaskName())
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(intent, "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(intent, "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();

    }
}
