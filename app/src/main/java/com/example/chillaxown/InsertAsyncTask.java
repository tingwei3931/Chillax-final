package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.Task;

/**
 * Created by USER on 12/4/2018.
 * Asynchronously handles the insert operation of database
 * in background thread
 */
public class InsertAsyncTask extends AsyncTask<TaskDetails, Void, Integer> {
    public static final String DBERROR = "err";
    Context context;

    //interface for AsyncResponse
    public interface AsyncResponse {
        void processFinish(int result);
    } //end interface

    public AsyncResponse delegate = null;

    //Constructor for InsertAsyncTask
    public InsertAsyncTask(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    /**
     * Inserts the data in the background
     * @param tasks
     * @return
     */
    @Override
    protected Integer doInBackground(TaskDetails... tasks) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            return database.insert(tasks[0]);
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return -1;
        }
    } //end doInBackground

    /**
     * Returns the result to the UI thread when operation finishes
     * @param result
     */
    protected void onPostExecute(Integer result) {
        delegate.processFinish(result);
    }
} //end of class

