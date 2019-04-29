package com.example.chillaxown;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class ViewActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        UpdateAsyncTask.AsyncResponse,
        GetAllAsyncTask.AsyncResponse {

    TaskDetails task, currentTask;
    //widget variable declaration
    private Button sharebtn, mEditBtn, mCompletedBtn, mFBBtn;
    private EditText mNameEditText, mLocationEditText, mReporterNameEditText, mDateEditText, mTimeEditText, mCategoryEditText;
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    FragmentManager fm = getFragmentManager();
    CallbackManager mCallbackManager;
    ShareDialog mShareDialog;
    private FragmentActivity myContext;
    //constant variable to denote datePickerDialog or timePickerDialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Task Details");  // provide compatibility to all the versions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(83,36,54)));
        // Button btnNotify = findViewById(R.id.editBtn);
        //get intent from DetailFragment
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //callback manager to handle fb request
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);

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
        sharebtn              = (Button)   findViewById(R.id.family);
        mCompletedBtn         = (Button)   findViewById(R.id.completed);
        mFBBtn                = (Button)   findViewById(R.id.facebook);

        //Populate the view with current data
        mNameEditText.setText(currentTask.getTaskName());
        mDateEditText.setText(currentTask.getTaskDate());
        mTimeEditText.setText(currentTask.getTaskTime());
        mCategoryEditText.setText(currentTask.getTaskCategory());

        if (currentTask.getIsComelpted().equals("Completed")) {
            mCompletedBtn.setVisibility(View.GONE);
        }

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
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post post = new Post(
                        mNameEditText.getText().toString(),
                        mCategoryEditText.getText().toString(),
                        mDateEditText.getText().toString(),
                        mTimeEditText.getText().toString(),
                        currentUser.getUid()

                        //   currentTask.getTaskName(),
                        //  currentTask.getTaskCategory(),
                        //  currentTask.getTaskDate(),
                        //  currentTask.getTaskTime(),
                        //  currentUser.getUid()
                );

                firebasesstore(post);
            }
        });
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.show(getFragmentManager(),
                        "Datepickerdialog");
            }
        });

        mCompletedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCompleted();
            }
        });

        mFBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFBDialog();
            }
        });
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
     * Displays Facebook Dialog.
     */
    private void showFBDialog() {
        //As per Facebook's policy, user needs to enter their own caption
        //cannot programmatically set the caption for the photo
        ShareDialog dialog = new ShareDialog(this);
        if (dialog.canShow(ShareLinkContent.class)){
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.google.com"))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag("#ConnectTheWorld")
                            .build())
                     .build();
            mShareDialog.show(content);
        }
        else {
            Log.d("Activity", "failed to get share photo permission");
        }
    }



    /**
     * Moves the task to completed tab.
     */
    private void moveToCompleted() {
        AlertDialog.Builder cfmBuilder = new AlertDialog.Builder(this);
        StringBuilder confirmationText =  new StringBuilder();
        confirmationText.append("Are you sure to move this task to completed? ");
        cfmBuilder.setMessage(confirmationText)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TaskDetails updatedTask = new TaskDetails(currentTask.getTaskID(),
                                currentTask.getTaskName(), currentTask.getTaskDate(),
                                currentTask.getTaskTime(), currentTask.getTaskCategory(),
                                "Completed");
                        UpdateAsyncTask updateAsync = new UpdateAsyncTask(ViewActivity.this, ViewActivity.this);
                        updateAsync.execute(updatedTask);
                        Log.i("UPDATE", "Task moved to complete!");
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("UPDATE", "Task not moved!");
                    }
                });
        cfmBuilder.show();
    }

    public void firebasesstore( Post post)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reader").push();

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("TaskName", mNameEditText.getText().toString());
        hashMap.put("TaskCategory",  mCategoryEditText.getText().toString());
        hashMap.put("TaskDate", mDateEditText.getText().toString());
        hashMap.put("TaskTime", mTimeEditText.getText().toString());
        hashMap.put("Userid", currentUser.getUid());

        myRef.updateChildren(hashMap);


        String key = myRef.getKey();

        post.setPostKey(key);
        // myRef.setValue(post);

        Snackbar.make(getWindow().getDecorView().getRootView(), "Your detail has been shared to the sharing space!", Snackbar.LENGTH_LONG)
                .show();


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
        Snackbar.make(getWindow().getDecorView().getRootView(), "Remainder has been saved!"+hourOfDay+":"+minute+":"+second, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void processFinish(int result) {
        if (result == 0) {
            Log.i("EDIT", "Task updated!");
            //Retrieve the new records from database
            GetAllAsyncTask getAllAsync = new GetAllAsyncTask(this, this);
            getAllAsync.execute();

        }
    }


    /**
     * Handler when the database returns data asynchronously via
     * GetAllAsyncTask
     * @param result
     */
    @Override
    public void processFinish(ArrayList<TaskDetails> result) {
        //clear the data in existing listView
        MainActivity.adapter.clear();
        //add the new data to adapter
        Collections.sort(result);
        MainActivity.adapter.addAll(result);
        //update view
        MainActivity.adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    } //end of processFinish
}
