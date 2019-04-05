package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by USER on 12/4/2018.
 * Asynchronously retrieves the record from database
 * in background thread
 */


public class GetByIdAsyncTask extends AsyncTask<Integer, Void, TaskDetails> {
    public static final String DBERROR = "err";
    Context context;

    //interface for AsyncResponse
    public interface AsyncResponse {
        void processFinish(TaskDetails result);
    } //end interface

    public AsyncResponse delegate = null;

    //Constructor for GetByIdAsyncTask
    public GetByIdAsyncTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    /**
     * Retrieves the data in the background
     * @param actID
     * @return
     */
    @Override
    protected TaskDetails doInBackground(Integer... actID) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            return database.getTaskDetailById(actID[0]);
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return null;
        }
    } //end doInBackground

    /**
     * Returns the result to the UI thread when operation finishes
     * @param result
     */
    protected void onPostExecute(TaskDetails result) {
        delegate.processFinish(result);
    } //end onPostExecute
} //end of class
