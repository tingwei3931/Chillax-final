package com.example.chillaxown;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String TaskName;
    private String TaskCategory;
    private String TaskDate;
    private String TaskTime;
    private String userId;
    private Object timeStamp ;


    public Post(String TaskName, String TaskCategory, String TaskDate, String TaskTime, String userId) {
        this.TaskName = TaskName;
        this.TaskCategory = TaskCategory;
        this.TaskDate = TaskDate;
        this.TaskTime = TaskTime;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTaskName() {
        return TaskName;
    }

    public String getTaskCategory() {
        return TaskCategory;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public String getUserId() {
        return userId;
    }

    public String gettime() {
        return TaskTime;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public void setTaskCategory(String description) {
        this.TaskCategory = TaskCategory;
    }

    public void setTaskDate(String TaskDate) {
        this.TaskDate = TaskDate;
    }

    public void setTaskTime(String TaskTime) {
        this.TaskTime = TaskTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}