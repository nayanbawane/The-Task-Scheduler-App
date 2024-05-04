package com.nayan.taskschedulerreminder.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks_table")
public class ModelClass {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String task, date, time, task_message;

    public ModelClass(String task, String date, String time, String task_message) {
        this.task = task;
        this.date = date;
        this.time = time;
        this.task_message = task_message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTask_message() {
        return task_message;
    }

    public void setTask_message(String task_message) {
        this.task_message = task_message;
    }
}
