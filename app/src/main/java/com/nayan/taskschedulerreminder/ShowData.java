package com.nayan.taskschedulerreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nayan.taskschedulerreminder.databinding.ActivityShowDataBinding;

public class ShowData extends AppCompatActivity {
    ActivityShowDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        String task = intent.getStringExtra("KEY_TASK");
        String date = intent.getStringExtra("KEY_DATE");
        String time = intent.getStringExtra("KEY_TIME");
        String task_message = intent.getStringExtra("KEY_MESSAGE");

        binding.showTitle.setText(task);
        binding.showDate.setText(date);
        binding.showTime.setText(time);
        binding.showMessage.setText(task_message);

    }
}