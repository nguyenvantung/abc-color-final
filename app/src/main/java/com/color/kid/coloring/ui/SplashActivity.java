package com.color.kid.coloring.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.color.kid.coloring.R;

public class SplashActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nextScreen();
    }

    public void nextScreen() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // TODO check update
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();},3000);
    }

}
