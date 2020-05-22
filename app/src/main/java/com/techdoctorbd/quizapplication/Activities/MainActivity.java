package com.techdoctorbd.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.techdoctorbd.quizapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
