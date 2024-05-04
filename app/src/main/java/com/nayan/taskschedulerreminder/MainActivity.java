package com.nayan.taskschedulerreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.nayan.taskschedulerreminder.Room.ModelClass;
import com.nayan.taskschedulerreminder.Room.TaskDatabase;
import com.nayan.taskschedulerreminder.Room.TasksDao;
import com.nayan.taskschedulerreminder.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_NOTIFICATION = 99;
    ItemAdapter adapterObj;
    TaskDatabase database;
    TasksDao tasksDao;
    ActivityMainBinding binding;
    List<ModelClass> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Ask for notification permission
        askNotificationPermission();

        dataList = new ArrayList<>();

//        searchView
        binding.searchView.clearFocus();

        database = TaskDatabase.getDatabase(this);
        tasksDao = database.tasksDao();

        //Creating the LayoutInflater instance
        LayoutInflater li = getLayoutInflater();
        View layout1 = li.inflate(R.layout.toast_no_data_found,(ViewGroup) findViewById(R.id.toast_no_data_found));

//        get data from database
        database.tasksDao().getTasks().observe(this, new Observer<List<ModelClass>>() {
            @Override
            public void onChanged(List<ModelClass> modelClasses) {
                dataList = modelClasses;
                adapterObj = new ItemAdapter(dataList,getApplicationContext());
                binding.recyclerView.setAdapter(adapterObj);

                if(dataList.isEmpty()){
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                else{
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

//        floating point button
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!query.isEmpty())
                {
                    dataList.clear();
                    dataList = tasksDao.searchTasks(query);
                    adapterObj = new ItemAdapter(dataList,getApplicationContext());
                    binding.recyclerView.setAdapter(adapterObj);
                    if (dataList.isEmpty())
                    {
                        if(binding.searchView.getQuery() == null){
                            binding.noDataFound.setVisibility(View.GONE);
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }
                        else{
                            binding.noDataFound.setVisibility(View.VISIBLE);
                            binding.emptyView.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.GONE);
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout1);//setting the view of custom toast layout
                            toast.show();
                        }
                    }
                    else{
                        binding.noDataFound.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataList.clear();
                dataList = tasksDao.searchTasks(newText);
                adapterObj = new ItemAdapter(dataList,getApplicationContext());
                binding.recyclerView.setAdapter(adapterObj);
                if(dataList.isEmpty()){
                    if(binding.searchView.getQuery() == null){
                        binding.noDataFound.setVisibility(View.GONE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                    else{
                        binding.noDataFound.setVisibility(View.VISIBLE);
                        binding.emptyView.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                }
                else{
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

    }

    private void askNotificationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "Notification Permission\nalready granted", Toast.LENGTH_SHORT).show();
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, RC_NOTIFICATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RC_NOTIFICATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ALLOW", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "DENY", Toast.LENGTH_SHORT).show();
            }
        }
    }
}