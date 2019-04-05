package com.example.chillaxown;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by TW on 27/3/2019.
 * An object used to store a a record of activity details.
 */
public class TaskDetails implements Parcelable, Comparable<TaskDetails> {

    //declare member variables
    private int taskID;
    private String taskName;
    private String taskDate;
    private String taskTime;
    private String taskCategory;
    private String isCompleted;

    // Table name label
    public static final String TABLE = "TaskDetails";

    // Table column names label
    public static final String KEY_ID = "id";
    public static final String KEY_TASKNAME = "taskName";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_ISCOMPLETED = "isCompleted";

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes data to Parcelable object.
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // write each field into the parcel. When reading
        // from parcel, will come back in the same order.
        parcel.writeInt(this.taskID);
        parcel.writeString(this.taskName);
        parcel.writeString(this.taskDate);
        parcel.writeString(this.taskTime);
        parcel.writeString(this.taskCategory);
        parcel.writeString(this.isCompleted);
    }

    /**
     * Overloaded Constructor for ActivityDetail.
     *
     * @param in
     */
    public TaskDetails(Parcel in) {
        readFromParcel(in);
    }

    /**
     * Called from the constructor to create
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    public void readFromParcel(Parcel in) {
        // read back each field in the same order
        // that is was written to the parcel
        this.taskID = in.readInt();
        this.taskName = in.readString();
        this.taskDate = in.readString();
        this.taskTime = in.readString();
        this.taskCategory = in.readString();
        this.isCompleted = in.readString();
    }

    /**
     * This field is need to create new objects, individually or as arrays.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public TaskDetails createFromParcel(Parcel in) {
            return new TaskDetails(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new TaskDetails[size];
        }
    };

    /**
     * Default constructor used to instantiate an ActivityDetail object.
     *
     * @param taskID
     * @param taskName
     * @param taskDate
     * @param taskTime
     * @param taskCategory
     * @param isCompleted
     */
    public TaskDetails(int taskID, String taskName, String taskDate, String taskTime, String taskCategory, String isCompleted) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskCategory = taskCategory;
        this.isCompleted = isCompleted;
    } //end constructor

    //Empty constructor
    public TaskDetails() {
    }

    /**
     * Accessor and Mutator methods here
     **/
    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setIsCompleted(String isCompleted) { this.isCompleted = isCompleted; }

    public String getIsComelpted() { return isCompleted; }

    /**
     * Override toString for pretty printing of the object.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + taskName + "\n" +
                "Category: " + taskCategory + "\n" +
                "Date: " + taskDate + "\n" +
                "Time added: " + taskTime + "\n" +
                "Completed? " + isCompleted + "\n";
    } //end toString

    /**
     * Override compareTo method to sort an arrayList of ActivityDetails.
     *
     * @param taskDetails
     * @return an integer used to compare two TaskDetails object.
     */
    @Override
    public int compareTo(@NonNull TaskDetails taskDetails) {
        return (this.taskDate == taskDetails.taskDate) ?
                this.taskTime.compareTo(taskDetails.taskTime) : this.taskDate.compareTo(taskDetails.taskDate);
    } //end of class
}

