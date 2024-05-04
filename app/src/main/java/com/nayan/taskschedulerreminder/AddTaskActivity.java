package com.nayan.taskschedulerreminder;

import static com.nayan.taskschedulerreminder.Notification.channelId;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nayan.taskschedulerreminder.Room.ModelClass;
import com.nayan.taskschedulerreminder.Room.TaskDatabase;
import com.nayan.taskschedulerreminder.Room.TasksDao;
import com.nayan.taskschedulerreminder.databinding.ActivityAddTaskBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    TaskDatabase database;
    TasksDao tasksDao;

    //    for datePicker and timePicker
    int cYear, cMonth, cDay, sYear, sMonth, sDay;
    int cHour, cMinute, sHour, sMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create notification channel
        createNotificationChannel();

        database = TaskDatabase.getDatabase(this);
        tasksDao = database.tasksDao();

//        Add Task
        binding.buttonNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.taskEditText.getText().toString().isEmpty() && !binding.tilDateEditText.getText().toString().isEmpty() && !binding.tilTimerEditText.getText().toString().isEmpty() && !binding.tilMessage.getText().toString().isEmpty()) {

                    try {
                        scheduleNotification();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    String task = binding.taskEditText.getText().toString();
                    String date = binding.tilDateEditText.getText().toString();
                    String time = binding.tilTimerEditText.getText().toString();
                    String task_message = binding.tilMessage.getText().toString();

                    ModelClass modelClass = new ModelClass(task, date, time, task_message);
                    tasksDao.insertTasks(modelClass);


                    Toast.makeText(AddTaskActivity.this, "Data Insert Successfully", Toast.LENGTH_SHORT).show();

                    onBackPressed();

                } else if (binding.taskEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Fill the task field", Toast.LENGTH_SHORT).show();

                } else if (binding.tilDateEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Fill the date field", Toast.LENGTH_SHORT).show();
                } else if (binding.tilTimerEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Fill the time field", Toast.LENGTH_SHORT).show();
                } else if (binding.tilMessage.getText().toString().isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Fill the Message field", Toast.LENGTH_SHORT).show();

                }
            }
        });

//       Cancel task
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        setDate
        binding.tilDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

//        setTime
        binding.tilTimer.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
    }

    private void setDate() {
//   Initialize Calendar
        Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR); // get current year
        cMonth = calendar.get(Calendar.MONTH); // get current month
        cDay = calendar.get(Calendar.DAY_OF_MONTH); // get current day

        //        Initialize DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Initialize year,month and day
                sYear = year;
                sMonth = month;
                sDay = dayOfMonth;

                String showDate = sDay + "/" + (sMonth + 1) + "/" + sYear;
                binding.tilDateEditText.setText(showDate);

            }
        }, cYear, cMonth, cDay
        );

//        Displayed previous selected date
        datePickerDialog.updateDate(sYear, sMonth, sDay);
//        Disable Past date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        show datePicker
        datePickerDialog.show();

    }

    private void setTime() {
        //   Initialize Calendar
        Calendar calendar = Calendar.getInstance();
        cHour = calendar.get(Calendar.HOUR_OF_DAY); // get current hour of the day
        cMinute = calendar.get(Calendar.MINUTE); // get current minute

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                Initialize hour and minute
                sHour = hourOfDay;
                sMinute = minute;

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.DAY_OF_MONTH, sDay);
                calendar1.set(Calendar.HOUR_OF_DAY, sHour);
                calendar1.set(Calendar.MINUTE, sMinute);

                binding.tilTimerEditText.setText(DateFormat.format("hh:mm aa", calendar1));
//                check condition
                if (calendar1.getTimeInMillis() == Calendar.getInstance()
                        .getTimeInMillis()) {
                    binding.tilTimerEditText.setText(DateFormat.format("hh:mm aa", calendar1));
                } else if (calendar1.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    binding.tilTimerEditText.setText("");
                    Toast.makeText(AddTaskActivity.this, "Firstly Select Date then\nSelect Future Time", Toast.LENGTH_SHORT).show();
                } else {
                    binding.tilTimerEditText.setText(DateFormat.format("hh:mm aa", calendar1));
                }
            }
        }, cHour, cMinute, false
        );

        timePickerDialog.show();
    }

    //Notification functions
    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification() throws ParseException {
        Intent intent = new Intent(this, Notification.class);
        String title = binding.taskEditText.getText().toString();
        String message = binding.tilMessage.getText().toString();
        intent.putExtra("titleExtra", title);
        intent.putExtra("messageExtra", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        Log.i("TimeCheck", "scheduleNotification: " + time);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );

    }

    private long getTime() throws ParseException {
        String sDate1 = binding.tilDateEditText.getText().toString();
        SimpleDateFormat date1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date = date1.parse(sDate1);
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int date2 = date.getDate();

        String sTime1 = binding.tilTimerEditText.getText().toString();
        SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");

        Date time2 = time1.parse(sTime1);

        int hour = time2.getHours();
        int minute = time2.getMinutes();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date2, hour, minute);
        return calendar.getTimeInMillis();
    }

    private void createNotificationChannel() {
        String name = "Notif Channel";
        String desc = "A description of the channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}