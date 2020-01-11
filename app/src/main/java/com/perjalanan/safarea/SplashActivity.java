package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String versionName = BuildConfig.VERSION_NAME;
        TextView textVersion = findViewById(R.id.textVersion);
        textVersion.setText("Versi " + versionName);

        Handler initHandler = new Handler();
        initHandler.postDelayed(this::startOnboarding, SPLASH_TIME);
    }

    private void startOnboarding() {
        startActivity(new Intent(this, OnboardingActivity.class));
        finish();
    }
}
