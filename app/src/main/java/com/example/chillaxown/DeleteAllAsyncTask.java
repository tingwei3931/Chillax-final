package com.example.chillaxown;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

public class DeleteAllAsyncTask  extends AsyncTask<Void, Void, Boolean> {
    public static final String DBERROR = "err";
    Context context;

    //declare interface AsyncResponse to handle feedback for
    //onPostExecute
    public interface AsyncResponse {
        void processFinish(boolean result);
    } //end interface

    public DeleteAllAsyncTask.AsyncResponse delegate = null;

    //Constructor
    public DeleteAllAsyncTask(Context context, DeleteAllAsyncTask.AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    } //end constructor

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            TaskDetailsSQL database = new TaskDetailsSQL(this.context);
            database.deleteAll();
            return true;
        } catch(SQLiteException e) {
            Log.e(DBERROR, "Database Error:" + e.getMessage());
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
    } //end of onPostExecute

}
