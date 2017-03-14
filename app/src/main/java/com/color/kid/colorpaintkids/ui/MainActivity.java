package com.color.kid.colorpaintkids.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.fragment.SplashFragment;
import com.color.kid.colorpaintkids.util.FragmentUtil;
import com.color.kid.colorpaintkids.util.SharePreferencesUtil;

public class MainActivity extends FragmentActivity {
    MediaPlayer mediaPlayer;
    SharePreferencesUtil sharePreferencesUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgr_be_happy);
        sharePreferencesUtil = new SharePreferencesUtil(this);
        if (sharePreferencesUtil.getSoundPlayed()){
            mediaPlayer.start();
        }
        mediaPlayer.setLooping(true);
        sharePreferencesUtil = new SharePreferencesUtil(this);
        FragmentUtil.showFragment(this, new SplashFragment(),  false, null, null, false);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed(); //replaced
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && sharePreferencesUtil.getSoundPlayed()){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void playSound(boolean open){
        if (open && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }else {
            mediaPlayer.pause();
        }
    }

}
