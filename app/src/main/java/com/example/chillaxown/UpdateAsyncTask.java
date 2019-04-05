package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by USER on 12/4/2018.
 * Asynchronously handles the update operation of database
 * in background thread
 */
public class UpdateAsyncTask extends AsyncTask<TaskDetails, Void, Integer> {
    public static final String DBERROR = "err";
    Context context;

    //declare interface AsyncResponse
    public interface AsyncResponse {
        void processFinish(int result);
    } //end interface

    public AsyncResponse delegate = null;

    //Constructor for UpdateAsyncTask
    public UpdateAsyncTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    /**
     * Performs the update operation in the background
     * @param activities
     * @return
     */
    @Override
    protected Integer doInBackground(TaskDetails... activities) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            database.update(activities[0]);
            return 0;
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return -1;
        }
    } //end doInBackground

    /**
     * Returns the data after the update operation finished
     * @param result
     */
    protected void onPostExecute(Integer result) {
        delegate.processFinish(result);
    } //end onPostExecute
} //end ofclass
