package com.nayan.taskschedulerreminder.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ModelClass.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TasksDao tasksDao();
    private static volatile TaskDatabase INSTANCE;
    public static TaskDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TaskDatabase.class, "ReminderTasksDB")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }
}
