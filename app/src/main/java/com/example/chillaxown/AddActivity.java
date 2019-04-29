package com.example.chillaxown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        InsertAsyncTask.AsyncResponse,
        GetAllAsyncTask.AsyncResponse{
    //Keys for starting intents
    private static final String ADD = "AddActivity";
    private static final int GET_LOCATION = 1;
    //Vibrate when error occurs
    private Vibrator vib;
    Animation animShake;

    //widget variable declaration
    private Button mClearBtn, mAddBtn;
    private EditText mNameEditText, mDateEditText, mTimeEditText, mCategoryEditText;
    //constant variable to denote datePickerDialog or timePickerDialog
    private static final int DATE_DIALOG_ID = 1, TIME_DIALOG_ID = 2, RADIO_DIALOG_ID = 3, CONFIRM_DIALOG_ID = 4;
    //Variables to hold the input from textView
    private String name, date, time, amPm, category;
    //variables to hold the value for date and time
    private int year, monthOfYear, dayOfMonth, hour, minute, taskID;

    //A String array to store the category list
    private String[] categoryList = {"Trip", "Study", "Chores", "Family", "Relaxation", "Urgent"};
    TaskDetails task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //getActionBar().setTitle("Add a new task");
        getSupportActionBar().setTitle("Add a new task");  // provide compatibility to all the versions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(83,36,54)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set references to widgets
        mClearBtn             = (Button)   findViewById(R.id.clearBtn);
        mAddBtn               = (Button)   findViewById(R.id.addBtn);
        mNameEditText         = (EditText) findViewById(R.id.nameEditText);
        mDateEditText         = (EditText) findViewById(R.id.dateEditText);
        mTimeEditText         = (EditText) findViewById(R.id.timeEditText);
        mCategoryEditText     = (EditText) findViewById(R.id.categoryEditText);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //register the event handler for buttons and textViews
        mAddBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mDateEditText.setOnClickListener(this);
        mTimeEditText.setOnClickListener(this);
        mCategoryEditText.setOnClickListener(this);
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

    //override onClick method to listen for callbacks from the button clicking
    @Override
    public void onClick(View view) {
        Log.i("CLICK", view.getId() +"");
        switch(view.getId()) {
            case R.id.addBtn:
                Log.i("ADD", "ADD BUTTON PRESSED!");
                //validate before asking for confirmation
                if (validate()) {
                    //add record to list
                    Log.i("ADD", "VALIDATION PASSED!");
                    addTask();
                }
                break;
            case R.id.clearBtn:
                resetForm();
                Toast.makeText(this, "Form has been cleared.", Toast.LENGTH_LONG).show();
                break;
            case R.id.dateEditText:
                displayDialog(DATE_DIALOG_ID);
                break;
            case R.id.timeEditText:
                displayDialog(TIME_DIALOG_ID);
                break;
            case R.id.categoryEditText:
                displayDialog(RADIO_DIALOG_ID);
                break;
        }
    } //end of onClick

    //display datePicker and timePicker dialog
    private void displayDialog(int id) {
        //Use Calendar to get the current local time
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        switch (id) {
            case DATE_DIALOG_ID:
                //instantiate a datePickerDialog and set the default date to today's date
                DatePickerDialog dDialog =
                        new DatePickerDialog(AddActivity.this, this, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dDialog.show();
                break;
            case TIME_DIALOG_ID:
                //instantiate a timePickerDialog and set the default time to today's time
                TimePickerDialog tDialog =
                        new TimePickerDialog(AddActivity.this,this,
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                false);
                tDialog.setTitle(null);
                tDialog.show();
                break;
            case RADIO_DIALOG_ID:
                //instantiate a RadioButtonDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.select_label)
                        .setItems(R.array.category_list,  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                mCategoryEditText.setText(categoryList[which]);
                            }
                        })
                        .show();
                break;
            case CONFIRM_DIALOG_ID:
                AlertDialog.Builder cfmBuilder = new AlertDialog.Builder(this);
                StringBuilder confirmationText =  new StringBuilder();
                String cfm = getResources().getString(R.string.confirmationText);

                confirmationText.append("<b>Name:</b><br>" + task.getTaskName() + "<br>")
                        .append("<br>")
                        .append("<b>Category:</b><br>" + task.getTaskCategory() + "<br>")
                        .append("<br>")
                        .append("<b>Date added:</b><br>" + task.getTaskDate() + "<br>")
                        .append("<br>")
                        .append("<b>Time added:</b><br>" + task.getTaskTime() + "<br>");

                cfmBuilder.setTitle(cfm)
                        .setMessage(Html.fromHtml(confirmationText.toString()))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("EDIT", "Positive button pressed!");
                                // TODO: INSERT INTO DB
                                //insert into db using AsyncTask
                                InsertAsyncTask insertAsync = new InsertAsyncTask(AddActivity.this, AddActivity.this);
                                insertAsync.execute(task);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("ADD", "Activity not added");
                            }
                        });
                cfmBuilder.show();
        }
    } //end displayDialog


    //method to reset input field
    private void resetForm() {
        mNameEditText.setText("");
        mDateEditText.setText("");
        mTimeEditText.setText("");
        mCategoryEditText.setText("");
        //return form to pristine state
        mNameEditText.setError(null);
        mNameEditText.clearFocus();    //clear focus from edittext
        //return form to pristine state
        mCategoryEditText.setError(null);
        mCategoryEditText.clearFocus();    //clear focus from edittext
        mDateEditText.setError(null);
        mTimeEditText.setError(null);
    } //end resetForm

    //override onDateSet to retrieve data from DatePickerDialog
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear+1;
        this.dayOfMonth = dayOfMonth;
        //update the date editText
        updateDisplay(DATE_DIALOG_ID);
    } //end onDateSet

    //override onTimeSet to retrieve data from TimePickerDialog
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // get the hour in 12hr format
        Log.i("TIME", String.valueOf(hour));
        if(hour < 12)
            this.hour = hour;
        else if (hour == 12)
            this.hour = 12;
        else
            this.hour = hour - 12;
        this.minute = minute;
        this.amPm = (hour < 12) ? "AM" : "PM";
        //update the time editText
        updateDisplay(TIME_DIALOG_ID);
    } //end onTimeSet

    //perform input validation
    private boolean validate() {
        //get input
        name         = mNameEditText.getText().toString();
        date         = mDateEditText.getText().toString();
        time         = mTimeEditText.getText().toString();
        category     = mCategoryEditText.getText().toString();

        //if name is empty
        if(name.trim().isEmpty()) {
            //return focus back to the textView
            mNameEditText.requestFocus();
            mNameEditText.setError("Activity name is required.");
            mNameEditText.setAnimation(animShake);
            mNameEditText.startAnimation(animShake);
            vib.vibrate(120);
            return false;
        }

        //if date is empty
        if(date.trim().isEmpty()) {
            mDateEditText.setError("Date attended is required.");
            mNameEditText.setAnimation(animShake);
            mNameEditText.startAnimation(animShake);
            vib.vibrate(120);
            // a workaround since mDateEditText must be unfocusable to disable the soft keyboard
            // but an unfocusable edittext cannot display the validation error
            Toast.makeText(this, "Date attended is required.", Toast.LENGTH_LONG).show();
            return false;
        }

        //if category is empty
        if(category.trim().isEmpty()) {
            mCategoryEditText.requestFocus();
            mCategoryEditText.setError("Please select a category.");
            mNameEditText.setAnimation(animShake);
            mNameEditText.startAnimation(animShake);
            Toast.makeText(this, "Category is required.", Toast.LENGTH_LONG).show();
            vib.vibrate(120);
            return false;
        }

        //Sanitize the input before adding to list(Eg: replace empty entries with "nil")
        if(time.trim().isEmpty()) {
            time = "nil";
        }
        return true;
    } //end of validate

    //update the text view for date and time once the value is selected
    private void updateDisplay(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                mDateEditText.setText(new StringBuilder()
                        .append(dayOfMonth)
                        .append('/')
                        .append(monthOfYear)
                        .append('/')
                        .append(year));
                break;
            case TIME_DIALOG_ID:
                mTimeEditText.setText(String.format("%02d:%02d %s", hour, minute, amPm));
                break;
        }
    } //end updateDisplay

    //add task to arrayList
    private void addTask() {
        //instantiate a new TaskDetails object to store data
        task = new TaskDetails(taskID, name, date, time, category, "In progress");
        displayDialog(CONFIRM_DIALOG_ID);
        //for debugging purposes
        //Log.i(ADD, act.toString());
    } //end addActivity

    @Override
    public void processFinish(ArrayList<TaskDetails> result) {
        //clear the data in existing listView
        MainActivity.adapter.clear();
        //hook back the new data retrieved
        MainActivity.adapter.addAll(result);
        //update view
        MainActivity.adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void processFinish(int result) {
        if(result != -1) {
            Log.i("ADD", task.toString());
            Log.i("ADD", "Task added!");
            //Retrieve the new records from database
            GetAllAsyncTask getAllAsync = new GetAllAsyncTask(this, this);
            getAllAsync.execute();
        }
    }
}
