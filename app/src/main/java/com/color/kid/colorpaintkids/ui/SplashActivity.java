package com.color.kid.colorpaintkids.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.color.kid.colorpaintkids.R;

import org.jsoup.Jsoup;

public class SplashActivity extends FragmentActivity {
    String packageName;
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

    private boolean web_update(){
        try {
            packageName = "com.color.kid.colorpaintkids";
            String curVersion = getApplication().getPackageManager().getPackageInfo(packageName, 0).versionName;
            String newVersion = curVersion;
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
            return (value(curVersion) < value(newVersion)) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private long value(String string) {
        string = string.trim();
        if( string.contains( "." )){
            final int index = string.lastIndexOf( "." );
            return value( string.substring( 0, index ))* 100 + value( string.substring( index + 1 ));
        }
        else {
            return Long.valueOf( string );
        }
    }
}
