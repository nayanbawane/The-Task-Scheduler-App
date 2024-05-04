package com.nayan.taskschedulerreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = this.getSharedPreferences("splash",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("isMain",false)){
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    editor.putBoolean("isMain",true);
                    editor.apply();

                    TaskStackBuilder.create(SplashActivity.this)
                            .addNextIntentWithParentStack(new Intent(SplashActivity.this,MainActivity.class))
                            .addNextIntent(new Intent(SplashActivity.this,IntroActivity.class))
                            .startActivities();
                }

            }
        },1000);

    }
}