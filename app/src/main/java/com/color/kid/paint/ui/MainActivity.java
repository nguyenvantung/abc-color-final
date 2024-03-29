package com.color.kid.paint.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.color.kid.paint.R;
import com.color.kid.paint.constance.Constants;
import com.color.kid.paint.fragment.SelectOptionFragment;
import com.color.kid.paint.util.FragmentUtil;
import com.color.kid.paint.util.SharePreferencesUtil;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;

public class MainActivity extends FragmentActivity {
    MediaPlayer mediaPlayer;
    SharePreferencesUtil sharePreferencesUtil;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, Constants.ADMOB_APP_ID);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgr_be_happy);
        sharePreferencesUtil = new SharePreferencesUtil(this);
        if (sharePreferencesUtil.getSoundPlayed()){
            mediaPlayer.start();
        }
        mediaPlayer.setLooping(true);
        sharePreferencesUtil = new SharePreferencesUtil(this);
        FragmentUtil.showFragment(this, new SelectOptionFragment(), false, null, null, false);
        setUpFirebaseAnalytic();
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            new GetVersionCode().execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setUpFirebaseAnalytic(){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "user");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    handleShowUpdate();
                }
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }

    }

    private void handleShowUpdate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_version_title);
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + MainActivity.this.getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
            }
            dialog.dismiss();
        });

        builder.create().show();
    }

}
