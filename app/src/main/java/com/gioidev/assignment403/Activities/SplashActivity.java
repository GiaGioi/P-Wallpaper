package com.gioidev.assignment403.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gioidev.assignment403.R;

public class SplashActivity extends AppCompatActivity {

    public static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed (new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent (SplashActivity.this, LoginActivity.class);
                startActivity(i);

                finish ();
            }
        }, SPLASH_TIME_OUT);

        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
    }
}
