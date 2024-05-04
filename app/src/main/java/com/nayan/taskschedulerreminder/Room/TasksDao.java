package com.nayan.taskschedulerreminder.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TasksDao {
    @Insert
    void insertTasks(ModelClass modelClass);

    @Query("UPDATE tasks_table set task = :titleText,date=:date, time=:time, task_message = :discri where id=:id")
    void update(String titleText,String discri, String time,String date,int id);

    @Delete
    void deleteTasks(ModelClass modelClass);


    @Query("select * from tasks_table")
    LiveData<List<ModelClass>> getTasks();

    @Query("SELECT * FROM tasks_table WHERE task LIKE '%' || :searchQuery || '%'")
    List<ModelClass> searchTasks(String searchQuery);


}
