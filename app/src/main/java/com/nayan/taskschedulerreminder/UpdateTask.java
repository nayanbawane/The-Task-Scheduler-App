package com.nayan.taskschedulerreminder;

import static com.nayan.taskschedulerreminder.Notification.channelId;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nayan.taskschedulerreminder.Room.TaskDatabase;
import com.nayan.taskschedulerreminder.Room.TasksDao;
import com.nayan.taskschedulerreminder.databinding.ActivityUpdateTaskBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateTask extends AppCompatActivity {

    ActivityUpdateTaskBinding binding;
    TaskDatabase database;
    TasksDao tasksDao;
//    public UpdateTask() throws ParseException {
//    }

//    for datePicker and timePicker
    int cYear, cMonth , cDay , sYear , sMonth, sDay;
    int cHour , cMinute , sHour , sMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create notification channel
        createNotificationChannel();

        Intent intent = getIntent();

        database = TaskDatabase.getDatabase(this);
        tasksDao = database.tasksDao();

        int KEY_ID = intent.getIntExtra("KEY_ID",0);
        String KEY_TASK = intent.getStringExtra("KEY_TASK");
        String KEY_DATE = intent.getStringExtra("KEY_DATE");
        String KEY_TIME = intent.getStringExtra("KEY_TIME");
        String KEY_MESSAGE = intent.getStringExtra("KEY_MESSAGE");

        binding.updateTaskEditText.setText(KEY_TASK);
        binding.updateDateEditText.setText(KEY_DATE);
        binding.updateTimerEditText.setText(KEY_TIME);
        binding.updateMessage.setText(KEY_MESSAGE);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!binding.updateTaskEditText.getText().toString().isEmpty() && !binding.updateDateEditText.getText().toString().isEmpty()
                        && !binding.updateTimerEditText.getText().toString().isEmpty() && !binding.updateMessage.getText().toString().isEmpty()){

                    try {
                        scheduleNotification();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    String task = binding.updateTaskEditText.getText().toString();
                    String date = binding.updateDateEditText.getText().toString();
                    String time = binding.updateTimerEditText.getText().toString();
                    String task_message = binding.updateMessage.getText().toString();

                    tasksDao.update(task,task_message,time,date,KEY_ID);
                    onBackPressed();

                }
                else if(binding.updateTaskEditText.getText().toString().isEmpty()){
                    Toast.makeText(UpdateTask.this, "Fill the task field", Toast.LENGTH_SHORT).show();

                }
                else if(binding.updateDateEditText.getText().toString().isEmpty()){
                    Toast.makeText(UpdateTask.this, "Fill the date field", Toast.LENGTH_SHORT).show();
                }
                else if(binding.updateTimerEditText.getText().toString().isEmpty()){
                    Toast.makeText(UpdateTask.this, "Fill the time field", Toast.LENGTH_SHORT).show();
                }
                else if(binding.updateMessage.getText().toString().isEmpty()){
                    Toast.makeText(UpdateTask.this, "Fill the Message field", Toast.LENGTH_SHORT).show();

                }

            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateTask.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //        setDate
        binding.updateDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Initialize datePicker dialog
                setDate();
            }
        });

//        setTime
        binding.updateTimer.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Initialize TimePicker Dialog
                setTime();
            }
        });
    }

    private void setDate() {
//   Initialize Calendar
        Calendar calendar = Calendar.getInstance();
        cYear  = calendar.get(Calendar.YEAR); // get current year
        cMonth  = calendar.get(Calendar.MONTH); // get current month
        cDay  = calendar.get(Calendar.DAY_OF_MONTH); // get current day

        //        Initialize DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Initialize year,month and day
                sYear = year;
                sMonth = month;
                sDay = dayOfMonth;

                String showDate = sDay+"/"+(sMonth+1)+"/"+sYear;
                binding.updateDateEditText.setText(showDate);

            }
        },cYear,cMonth,cDay
        );

//        Displayed previous selected date
        datePickerDialog.updateDate(sYear,sMonth,sDay);
//        Disable Past date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        show datePicker
        datePickerDialog.show();

    }

    private void setTime() {
        //   Initialize Calendar
        Calendar calendar = Calendar.getInstance();
        cHour  = calendar.get(Calendar.HOUR_OF_DAY); // get current hour of the day
        cMinute  = calendar.get(Calendar.MINUTE); // get current minute

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                Initialize hour and minute
                sHour = hourOfDay;
                sMinute = minute;

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.DAY_OF_MONTH,sDay);
                calendar1.set(Calendar.HOUR_OF_DAY,sHour);
                calendar1.set(Calendar.MINUTE,sMinute);

                binding.updateTimerEditText.setText(DateFormat.format("hh:mm aa",calendar1));
//                check condition
                if(calendar1.getTimeInMillis() == Calendar.getInstance()
                        .getTimeInMillis()){
                    binding.updateTimerEditText.setText(DateFormat.format("hh:mm aa",calendar1));
                }
                else if(calendar1.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                    binding.updateTimerEditText.setText(DateFormat.format("hh:mm aa",calendar1));
                }
                else{
                    binding.updateTimerEditText.setText("");
                    Toast.makeText(UpdateTask.this, "Firstly ReSelect Date then\nSelect Future Time", Toast.LENGTH_SHORT).show();
                }
            }
        },cHour,cMinute,false
        );

        timePickerDialog.show();
    }

    private void scheduleNotification() throws ParseException {
        Intent intent = new Intent(this, Notification.class);
        String title = binding.updateTaskEditText.getText().toString();
        String message = binding.updateMessage.getText().toString();
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
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
    }

    private long getTime() throws ParseException {
        String sDate1=binding.updateDateEditText.getText().toString();
        SimpleDateFormat date1=new SimpleDateFormat("dd/MM/yyyy");

        Date date = date1.parse(sDate1);

        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int date2 = date.getDate();

        String sTime1 = binding.updateTimerEditText.getText().toString();
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