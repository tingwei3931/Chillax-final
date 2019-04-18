package com.example.chillaxown;

import com.google.firebase.database.ServerValue;

public class Duration {

    private int postKey;
    private String taskCategory;
    private long taskTime;
    // Table name label
    public static final String TABLE = "Duration";

    // Table column names label
    public static final String KEY_ID = "id";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TIME = "time";

    public Duration(int postKey, String taskCategory, long taskTime) {
        this.postKey = postKey;
        this.taskCategory = taskCategory;
        this.taskTime = taskTime;
    }

    // make sure to have an empty constructor inside ur model class
    public Duration() {
    }


    public int getPostKey() {
        return postKey;
    }

    public void setPostKey(int postKey) {
        this.postKey = postKey;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }


    public String toString() {
        return "Duration ID: " + this.postKey + "\n" +
                "Category: " + this.taskCategory + "\n" +
                "Time: " + this.taskTime + "\n";
    }

}
