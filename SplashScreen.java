package com.example.newflixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent e = new Intent(SplashScreenActivity2.this, MainActivity.class);
                startActivity(e);
                finish();
            }

        }, 3000);


    }
}
