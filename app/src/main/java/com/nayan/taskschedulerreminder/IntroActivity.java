package com.nayan.taskschedulerreminder;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SlideFragmentBuilder()
                .title("Manage\nYour Tasks")
                .description("Allows you to efficiently create\nand manage your daily\ntasks and other\nimportant events")
                .buttonsColor(R.color.red)
                .backgroundColor(R.color.Note1)
                .image(R.drawable.img1)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Work\nOn Time")
                .description("Allows you to complete\nyour work On Time")
                .buttonsColor(R.color.red)
                .backgroundColor(R.color.Note2)
                .image(R.drawable.img2)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Get Reminder\nOn Time")
                .description("Allows you to track your\nplans and get reminder\nof your tasks on\nset Time")
                .buttonsColor(R.color.red)
                .backgroundColor(R.color.Note3)
                .image(R.drawable.img3)
                .build());

    }
}
