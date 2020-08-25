package com.color.kid.paint.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.color.kid.paint.R;
import com.color.kid.paint.util.DebugLog;

import java.security.MessageDigest;

public class SplashActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        printHashKey();
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

    public  void printHashKey() {
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = new String(Base64.encode(md.digest(), 0));
                DebugLog.e("AppLog key:" + hashKey + "=");
            }
        } catch (Exception e) {
            DebugLog.e("AppLog+error:" + e);
        }
    }

}
