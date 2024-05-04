package com.nayan.taskschedulerreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nayan.taskschedulerreminder.databinding.ActivityNotificationDataBinding;

public class NotificationData extends AppCompatActivity {

    ActivityNotificationDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String notif_title = intent.getStringExtra("NOTIFICATION_TITLE");
        String notif_message = intent.getStringExtra("NOTIFICATION_MESSAGE");

        binding.notificationTitle.setText(notif_title);
        binding.notificationMessage.setText(notif_message);
    }
}