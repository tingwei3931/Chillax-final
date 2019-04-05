package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by USER on 12/4/2018.
 * Asynchronously retrieve all the data from database
 */

public class GetAllAsyncTask extends AsyncTask<Void, Void, ArrayList<TaskDetails>> {
    public static final String DBERROR = "err";
    Context context;

    //interface for AsyncResponse
    public interface AsyncResponse {
        void processFinish(ArrayList<TaskDetails> result);
    } //end interface

    public AsyncResponse delegate = null;

    //Constructor for GetAllAsyncTask
    public GetAllAsyncTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    /**
     * Retrieves the data in the background
     * @param params
     * @return
     */
    @Override
    protected ArrayList<TaskDetails> doInBackground(Void... params) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            return database.getTaskDetailsList();
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return null;
        }
    } //end of doInBackground

    public ArrayList<TaskDetails> getlist(String menu)
    {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            return database.getTaskDetailsList(menu);
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return null;
        }
    }
    /**
     * Returns the result to the UI thread when operation finishes
     * @param result
     */
    @Override
    protected void onPostExecute(ArrayList<TaskDetails> result) {
        delegate.processFinish(result);
    } //end of onPostExecute
} //end of class
