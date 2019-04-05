package com.example.chillaxown;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to initialise DB and create table.
 */
public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 10;
    SQLiteDatabase db ;
    // Database Name
    private static final String DATABASE_NAME = "taskDetails.db";

    // Constructor for DBHelper
    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the table when the class calls onCreate()
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables are created here
        String CREATE_TABLE_CLASSDETAILS = "CREATE TABLE " + TaskDetails.TABLE  + "("
                + TaskDetails.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TaskDetails.KEY_TASKNAME + " TEXT, "
                + TaskDetails.KEY_CATEGORY + " TEXT, "
                + TaskDetails.KEY_DATE + " TEXT, "
                + TaskDetails.KEY_TIME + " INTEGER, "
                + TaskDetails.KEY_ISCOMPLETED + " TEXT)";
        db.execSQL(CREATE_TABLE_CLASSDETAILS);
    } //end of onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + TaskDetails.TABLE);
        // Create tables again
        onCreate(db);
    } //end of onUpgrade
} //end of class

