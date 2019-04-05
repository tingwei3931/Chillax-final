package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by USER on 12/4/2018.
 * Performs the deletion operation of database asynchronously.
 */
public class DeleteAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    public static final String DBERROR = "err";
    Context context;

    //declare interface AsyncResponse to handle feedback for
    //onPostExecute
    public interface AsyncResponse {
        void processFinish(boolean result);
    } //end interface

    public AsyncResponse delegate = null;

    //Constructor
    public DeleteAsyncTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    @Override
    protected Boolean doInBackground(Integer... taskID) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            database.delete(taskID[0]);
            return true;
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return false;
        }
    } //end doInBackground

    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
    } //end of onPostExecute

} //end of class
