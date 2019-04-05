package com.example.chillaxown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.example.chillaxown.GetAllAsyncTask;
import com.example.chillaxown.MainActivity;
import com.example.chillaxown.R;
import com.example.chillaxown.TaskDetails;
import com.example.chillaxown.UpdateAsyncTask;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

/**
 * Handles the logic to let user edit existing data and stores it into the database.
 */
public class EditActivity extends AppCompatActivity
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        UpdateAsyncTask.AsyncResponse,
        GetAllAsyncTask.AsyncResponse {

    //used to get location from PlacePicker
    private static final int GET_LOCATION = 1;
    //vibrates the phone when there is errors in the input
    private Vibrator vib;
    //shake animation to indicate there is input error
    //in editText
    Animation animShake;

    //widget variable declaration
    private Button mClearBtn, mEditBtn;
    private EditText mNameEditText, mLocationEditText, mReporterNameEditText, mDateEditText, mTimeEditText, mCategoryEditText;
    //constant variable to denote datePickerDialog or timePickerDialog
    private static final int DATE_DIALOG_ID = 1, TIME_DIALOG_ID = 2, RADIO_DIALOG_ID = 3, CONFIRM_DIALOG_ID = 4;
    //Variables to hold the input from textView
    private String name, location, reporterName, date, time, amPm, category, imgURI;
    //variables to hold the value for date and time
    private int year, monthOfYear, dayOfMonth, hour, minute;
    //A String array to store the category list
    private String[] categoryList = {"Trip", "Study", "Chores", "Family", "Relaxation", "Urgent"};
    //saves the old activity coming from the previous fragment and
    //the new edited activity
    TaskDetails task, currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent from DetailFragment
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // get the object that is passed through the intent
        currentTask = bundle.getParcelable("taskObj");
        Log.i("BEFORE_EDIT", currentTask.toString());
        //set references to widgets
        mClearBtn             = (Button)   findViewById(R.id.clearBtn);
        mEditBtn              = (Button)   findViewById(R.id.editBtn);
        mNameEditText         = (EditText) findViewById(R.id.nameEditText);
        mDateEditText         = (EditText) findViewById(R.id.dateEditText);
        mTimeEditText         = (EditText) findViewById(R.id.timeEditText);
        mCategoryEditText     = (EditText) findViewById(R.id.categoryEditText);

        //Populate the view with current data
        mNameEditText.setText(currentTask.getTaskName());
        mDateEditText.setText(currentTask.getTaskDate());
        mTimeEditText.setText(currentTask.getTaskTime());
        mCategoryEditText.setText(currentTask.getTaskCategory());

        //load shaking animation and initialise vibrator
        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //register the event handler for buttons and textViews
        mEditBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mDateEditText.setOnClickListener(this);
        mTimeEditText.setOnClickListener(this);
        mCategoryEditText.setOnClickListener(this);

        //animation on the app logo
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translateleft);
        //reset animation to its start state
        anim.reset();
    } //end onCreate

    //callback method for the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // to use this method, define the parent activity in androidManifest.xml
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //end onOptionsItemSelected

    //override onClick method to listen for callbacks from the button clicking
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.editBtn:
                //validate before asking for confirmation
                if (validate()) {
                    //add record to database
                    updateActivity();
                }
                break;
            case R.id.clearBtn:
                resetForm();
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Form cleared!",
                        Snackbar.LENGTH_LONG)
                        .show();
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

    //perform input validation
    private boolean validate() {
        //get input
        name            = mNameEditText.getText().toString();
        date            = mDateEditText.getText().toString();
        time            = mTimeEditText.getText().toString();
        category        = mCategoryEditText.getText().toString();

        //if name is empty
        if (name.trim().isEmpty()) {
            //return focus back to the textView
            mNameEditText.requestFocus();
            mNameEditText.setError("Activity name is required.");
            mNameEditText.setAnimation(animShake);
            mNameEditText.startAnimation(animShake);
            vib.vibrate(120);
            return false;
        }

        //if date is empty
        if (date.trim().isEmpty()) {
            mDateEditText.setError("Date attended is required.");
            mDateEditText.setAnimation(animShake);
            mDateEditText.startAnimation(animShake);
            vib.vibrate(120);
            // a workaround since mDateEditText must be unfocusable to disable the soft keyboard
            // but an unfocusable edittext cannot display the validation error
            Toast.makeText(this, "Date attended is required.", Toast.LENGTH_LONG).show();
            return false;
        }

        //if reporterName is empty
        if (category.trim().isEmpty()) {
            mCategoryEditText.requestFocus();
            mCategoryEditText.setError("Please select a category.");
            mCategoryEditText.setAnimation(animShake);
            mCategoryEditText.startAnimation(animShake);
            vib.vibrate(120);
            return false;
        }

        //Sanitize the input before adding to list(Eg: replace empty entries with "nil")
        if (time.trim().isEmpty()) {
            time = "nil";
        }
        return true;
    } //end of validate

    /**
     * display dialogs based on the id being passed
     * @param id
     */
    private void displayDialog(int id) {
        //Use Calendar to get the current local time
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        switch (id) {
            case DATE_DIALOG_ID:
                //instantiate a datePickerDialog and set the default date to today's date
                DatePickerDialog dDialog =
                        new DatePickerDialog(EditActivity.this, this, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dDialog.show();
                break;
            case TIME_DIALOG_ID:
                //instantiate a timePickerDialog and set the default time to today's time
                TimePickerDialog tDialog =
                        new TimePickerDialog(EditActivity.this,this,
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                false);
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
                String cfm = getResources().getString(R.string.yes);
                confirmationText.append("<b>Name:</b><br>" + task.getTaskName() + "<br>")
                        .append("<br>")
                        .append("<b>Category:</b><br>" + task.getTaskCategory() + "<br>")
                        .append("<br>")
                        .append("<b>Date attended:</b><br>" + task.getTaskDate() + "<br>")
                        .append("<br>")
                        .append("<b>Time attended:</b><br>" + task.getTaskTime() + "<br>");
                cfmBuilder.setTitle(cfm)
                        .setMessage(Html.fromHtml(confirmationText.toString()))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("EDIT", "Positive button pressed!");
                                UpdateAsyncTask updateAsync = new UpdateAsyncTask(getApplicationContext(), EditActivity.this);
                                updateAsync.execute(task);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("EDIT", "Task not updated!");
                            }
                        });
                cfmBuilder.show();
        } //end switch
    } //end displayDialog

    /**
     * Asks for confirmation to update record.
     */
    private void updateActivity() {
        //instantiate a new ActivityDetail object to store data
        task = new TaskDetails(currentTask.getTaskID(), name, date, time, category, currentTask.getIsComelpted());
        displayDialog(CONFIRM_DIALOG_ID);
        //for debugging purposes
        Log.i("AFTER_EDIT", task.toString());
    } //end updateActivity

    /**
     * Resets form to its pristine state.
     */
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

    /**
     * Overrides onDateSet to listen to DatePickerDialog selection.
     * @param datePicker
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear+1;
        this.dayOfMonth = dayOfMonth;
        //update the date editText
        updateDisplay(DATE_DIALOG_ID);
    } //end onDateSet

    /**
     * Overrides onTimeSet to listen to TimePickerDialog selection.
     * @param timePicker
     * @param hour
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // get the hour in 12hr format
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

    /**
     * Populate the edit texts with the value retrieved from both
     * PickerDialogs.
     * @param id
     */
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

    /**
     * Listens to asynchronous updateAsyncTask when it completes.
     * @param result
     */
    @Override
    public void processFinish(int result) {
        //if update success
        if(result == 0){
            Log.i("EDIT", task.toString());
            Log.i("EDIT", "Task updated!");
            //Retrieve the new records from database
            GetAllAsyncTask getAllAsync = new GetAllAsyncTask(this, this);
            getAllAsync.execute();
        }
    } //end processFinish

    /**
     * Handler when the database returns data asynchronously via
     * GetAllAsyncTask
     * @param result
     */
    @Override
    public void processFinish(ArrayList<TaskDetails> result) {
        // clear the data in existing listView
        MainActivity.adapter.clear();
        Collections.sort(result);
        MainActivity.adapter.addAll(result);
        MainActivity.adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
} //end of class
