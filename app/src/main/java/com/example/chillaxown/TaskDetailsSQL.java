package com.example.chillaxown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Contains the SQL query and methods for CRUD operations.
 */
public class TaskDetailsSQL {
    //DBHelper to initialise database
    private DBHelper dbHelper;
    ArrayList<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
    /**
     * Constructor for ActivityDetailSQL.
     * @param context
     */
    public TaskDetailsSQL(Context context) {
        dbHelper = new DBHelper(context);
    }


    public int insertDuration(Duration duration) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Duration.KEY_CATEGORY, duration.getTaskCategory());
        values.put(Duration.KEY_TIME, duration.getTaskTime());

        // Inserting Row
        long task_id = db.insert(Duration.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) task_id;
    }

    public ArrayList<Duration> getDurationByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Duration.KEY_ID + "," +
                Duration.KEY_CATEGORY + "," +
                Duration.KEY_TIME  +
                " FROM " + Duration.TABLE
                + " WHERE " +
                Duration.KEY_CATEGORY + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        ArrayList<Duration> durationList = new ArrayList<Duration>();
        //Cursor cursor = retrieve(category);

        Cursor cursor = db.rawQuery(selectQuery, new String[] { category } );

        if (cursor.moveToFirst()) {
            Duration duration = new Duration();
            do {
                duration.setPostKey(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                );
                duration.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                );
                duration.setTaskTime(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                );
                durationList.add(duration);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return durationList;

    }

    public ArrayList<Duration> getAllDuration() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Duration.KEY_ID + "," +
                Duration.KEY_CATEGORY + "," +
                Duration.KEY_TIME  +
                " FROM " + Duration.TABLE;

        ArrayList<Duration> durationList = new ArrayList<Duration>();
        //Cursor cursor = retrieve(category);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Duration duration = new Duration();
            do {
                duration.setPostKey(
                        cursor.getInt(cursor.getColumnIndex(Duration.KEY_ID))
                );
                duration.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(Duration.KEY_CATEGORY))
                );
                duration.setTaskTime(
                        cursor.getInt(cursor.getColumnIndex(Duration.KEY_TIME))
                );
                durationList.add(duration);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return durationList;
    }

    /**
     * Performs insert operation for DB.
     * @param task
     * @return int task_ID
     */
    public int insert(TaskDetails task) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDetails.KEY_TASKNAME, task.getTaskName());
        values.put(TaskDetails.KEY_DATE, task.getTaskDate());
        values.put(TaskDetails.KEY_TIME, task.getTaskTime());
        values.put(TaskDetails.KEY_CATEGORY, task.getTaskCategory());
        values.put(TaskDetails.KEY_ISCOMPLETED, task.getIsComelpted());

        // Inserting Row
        long task_id = db.insert(TaskDetails.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) task_id;
    } //end of insert

    /**
     * Performs delete operation for DB.
     * @param task_id
     */
    public void delete(int task_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(TaskDetails.TABLE, TaskDetails.KEY_ID + "= ?", new String[] { String.valueOf(task_id) });
        db.close(); // Closing database connection
    } //end of delete

    /**
     * Deletes all the rows in DB
     */
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskDetails.TABLE, "1", null);
        db.close();
    } // end of delete

    /**
     * Updates one record in db.
     * @param task
     */
    public void update(TaskDetails task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TaskDetails.KEY_TASKNAME, task.getTaskName());
        values.put(TaskDetails.KEY_CATEGORY, task.getTaskCategory());
        values.put(TaskDetails.KEY_DATE, task.getTaskDate());
        values.put(TaskDetails.KEY_TIME, task.getTaskTime());
        values.put(TaskDetails.KEY_ISCOMPLETED, task.getIsComelpted());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(TaskDetails.TABLE, values, TaskDetails.KEY_ID + "= ?", new String[] {String.valueOf(task.getTaskID()) });
        db.close(); // Closing database connection
    } //end of update

    /**
     * Returns all the activityDetails in the DB.
     * @return a list of ActivityDetail
     */ SQLiteDatabase db;
    public Cursor retrieve1(String searchterm)
    {
        String[] columns={ TaskDetails.KEY_ID,TaskDetails.KEY_TASKNAME};
        Cursor c=null;
        if(searchterm!=null&&searchterm.length()>0)
        {
            String sql="SELECT * FROM "+TaskDetails.TABLE+" WHERE "+TaskDetails.KEY_TASKNAME+" LIKE '%"+searchterm+"%'";
            c=db.rawQuery(sql,null);
            return c;
        }

        c=db.query(TaskDetails.TABLE,columns,null,null,null,null,TaskDetails.KEY_DATE+" ASC");
        return c;
    }
    public ArrayList<TaskDetails> getTaskDetailsList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                TaskDetails.KEY_ID + "," +
                TaskDetails.KEY_TASKNAME + "," +
                TaskDetails.KEY_CATEGORY + ","+
                TaskDetails.KEY_DATE + "," +
                TaskDetails.KEY_TIME + "," +
                TaskDetails.KEY_ISCOMPLETED +
                " FROM " + TaskDetails.TABLE;

        //new list to store data queried
      //  ArrayList<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskDetails task = new TaskDetails();
                task.setTaskID(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                );
                task.setTaskName(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TASKNAME))
                );
                task.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                );
                task.setTaskDate(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_DATE))
                );
                task.setTaskTime(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                );
                task.setIsCompleted(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_ISCOMPLETED))
                );
                taskDetailsList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskDetailsList;
    } //end of getActivityDetailList


    public ArrayList<TaskDetails> getTaskDetailsList(String menu) {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                TaskDetails.KEY_ID + "," +
                TaskDetails.KEY_TASKNAME + "," +
                TaskDetails.KEY_CATEGORY + ","+
                TaskDetails.KEY_DATE + "," +
                TaskDetails.KEY_TIME + "," +
                TaskDetails.KEY_ISCOMPLETED +
                " FROM " + TaskDetails.TABLE;

        //new list to store data queried
        //  ArrayList<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
        Cursor cursor= retrieve1(menu);
        //  Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskDetails task = new TaskDetails();
                task.setTaskID(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                );
                task.setTaskName(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TASKNAME))
                );
                task.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                );
                task.setTaskDate(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_DATE))
                );
                task.setTaskTime(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                );
                task.setIsCompleted(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_ISCOMPLETED))
                );
                taskDetailsList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskDetailsList;
    } //end of getActivityDetailList

    /**
     * Returns an activityDetail based on the id.
     * @param Id
     * @return an object of ActivityDetail
     */
    SQLiteDatabase db1;
    public Cursor retrieve(String searchterm)
    {
        String[] columns={ TaskDetails.KEY_ID , TaskDetails.KEY_TASKNAME};
        Cursor c=null;
        if(searchterm!=null&&searchterm.length()>0)
        {
            String sql="SELECT * FROM "+TaskDetails.TABLE+" WHERE "+TaskDetails.KEY_TASKNAME+" LIKE '%"+searchterm+"%'";
            c=db1.rawQuery(sql,null);
            return c;
        }

      //  c=db1.query(TaskDetails.TABLE,columns,null,null,null,null,COL_2+" ASC");
        return c;
    }
    public TaskDetails getTaskDetailById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                TaskDetails.KEY_ID + "," +
                TaskDetails.KEY_TASKNAME + "," +
                TaskDetails.KEY_CATEGORY + "," +
                TaskDetails.KEY_DATE + "," +
                TaskDetails.KEY_TIME + "," +
                TaskDetails.KEY_ISCOMPLETED +
                " FROM " + TaskDetails.TABLE
                + " WHERE " +
                TaskDetails.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        TaskDetails task = new TaskDetails();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );
        if (cursor.moveToFirst()) {
            do {
                task.setTaskID(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                );
                task.setTaskName(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TASKNAME))
                );
                task.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                );
                task.setTaskDate(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_DATE))
                );
                task.setTaskTime(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                );
                task.setIsCompleted(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_ISCOMPLETED))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return task;
    } //end of getActivityDetailById

     /**
     * Returns a list of activityDetail filtered by category.
     * @param category
     * @return a list of ActivityDetail
     */
  /*   public ArrayList<TaskDetails> getTaskDetailsByCategory1(String category){
         SQLiteDatabase db = dbHelper.getReadableDatabase();
         String selectQuery =  "SELECT  " +
                 TaskDetails.KEY_ID + "," +
                 TaskDetails.KEY_TASKNAME + "," +
                 TaskDetails.KEY_CATEGORY + "," +
                 TaskDetails.KEY_DATE + "," +
                 TaskDetails.KEY_TIME + "," +
                 TaskDetails.KEY_ISCOMPLETED +
                 " FROM " + TaskDetails.TABLE
                 + " WHERE " +
                 TaskDetails.KEY_CATEGORY + "=?";// It's a good practice to use parameter ?, instead of concatenate string

         ArrayList<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
         //Cursor cursor = retrieve(category);
        Cursor cursor= dbHelper.retrieve(category);
      //   Cursor cursor = db.rawQuery(selectQuery, new String[] { category } );

         if (cursor.moveToFirst()) {
             TaskDetails task = new TaskDetails();
             do {
                 task.setTaskID(
                         cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                 );
                 task.setTaskName(
                         cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TASKNAME))
                 );
                 task.setTaskCategory(
                         cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                 );
                 task.setTaskDate(
                         cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_DATE))
                 );
                 task.setTaskTime(
                         cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                 );
                 task.setIsCompleted(
                         cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_ISCOMPLETED))
                 );
                 taskDetailsList.add(task);

             } while (cursor.moveToNext());
         }
         cursor.close();
         db.close();
         return taskDetailsList;
     }*/
    public ArrayList<TaskDetails> getTaskDetailsByCategory(String category){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                TaskDetails.KEY_ID + "," +
                TaskDetails.KEY_TASKNAME + "," +
                TaskDetails.KEY_CATEGORY + "," +
                TaskDetails.KEY_DATE + "," +
                TaskDetails.KEY_TIME + "," +
                TaskDetails.KEY_ISCOMPLETED +
                " FROM " + TaskDetails.TABLE
                + " WHERE " +
                TaskDetails.KEY_CATEGORY + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        ArrayList<TaskDetails> taskDetailsList = new ArrayList<TaskDetails>();
        //Cursor cursor = retrieve(category);

         Cursor cursor = db.rawQuery(selectQuery, new String[] { category } );

        if (cursor.moveToFirst()) {
            TaskDetails task = new TaskDetails();
            do {
                task.setTaskID(
                        cursor.getInt(cursor.getColumnIndex(TaskDetails.KEY_ID))
                );
                task.setTaskName(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TASKNAME))
                );
                task.setTaskCategory(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_CATEGORY))
                );
                task.setTaskDate(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_DATE))
                );
                task.setTaskTime(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_TIME))
                );
                task.setIsCompleted(
                        cursor.getString(cursor.getColumnIndex(TaskDetails.KEY_ISCOMPLETED))
                );
                taskDetailsList.add(task);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskDetailsList;
    } //end getActivityDetailByCategory

    public float category(String category)
    {SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        float count=0,counttotal=0,countrow=0;
        String[] columns={TaskDetails.KEY_TASKNAME ,TaskDetails.KEY_CATEGORY};
        Cursor cursor;

        // String sql="SELECT "+TaskDetails.KEY_CATEGORY+" FROM "+TaskDetails.TABLE+" WHERE "+TaskDetails.KEY_CATEGORY+"="+category+"";

        //  String sql="SELECT COUNT(*)/(SELECT COUNT(*) FROM "+TaskDetails.TABLE+")*100 FROM "+TaskDetails.TABLE+" WHERE "+TaskDetails.KEY_CATEGORY +"= 'Study'";
        String sql="SELECT COUNT(*) FROM "+TaskDetails.TABLE+" WHERE "+TaskDetails.KEY_CATEGORY +"="+"'"+category+"'";
        // if(category!=null&&category.length()>0) {
//        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        cursor = db1.rawQuery(sql,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            countrow= cursor.getFloat(0);

        }
        cursor.close();
        String sql1="SELECT COUNT(*) FROM "+TaskDetails.TABLE;
        // if(category!=null&&category.length()>0) {
//        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        cursor = db1.rawQuery(sql1,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            counttotal= cursor.getFloat(0);

        }
        cursor.close();
        count =countrow/counttotal*100;
        return count;


        //  c=db.query(TABLE_NAME,columns,null,null,null,null,COL_2+" ASC");
        // return c;
    }

} //end of class