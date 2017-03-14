package com.color.kid.colorpaintkids.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.color.kid.colorpaintkids.ColorPaintKidsApplication;

public class SharePreferencesUtil {
    public static final double NULL_DOUBLE = -1.0d;
    public static final int NULL_INT = -1;
    public static final long NULL_LONG = -1;
    public static final String NULL_STRING;
    private static final String PREFERECES_PREMIUM_ALL_IN_ONE = "PREFERECES_PREMIUM_ALL_IN_ONE";
    private static final String PREFERECES_PREMIUM_MORE_IMAGES = "PREFERECES_PREMIUM_MORE_IMAGES";
    private static final String PREFERECES_PREMIUM_REMOVE_ADS = "PREFERECES_PREMIUM_REMOVE_ADS";
    private static final String PREFERENCES_FACEBOOK_LIKE = "PREFERENCES_FACEBOOK_LIKE";
    private static final String PREFERENCES_LAST_TIME_OPENED = "PREFERENCES_LAST_TIME_OPENED";
    private static final String PREFERENCES_MUSIC_SETTINGS = "PREFERENCES_MUSIC_SETTINGS";
    private static final String PREFERENCES_NUMBER_LAUNCHED = "PREFERENCES_NUMBER_LAUNCHED";
    private static final String PREFERENCES_SOUND_PLAYED = "PREFERENCES_SOUND_PLAYED";
    private static final String PREFERENCES_PRIVACY = "PREFERENCES_PRIVACY";
    private static final String PREFERENCES_RATE_FIRST_ITEM = "PREFERENCES_RATE_FIRST_ITEM";
    private static final String PREFERENCES_SOUND_SETTINGS = "PREFERENCES_SOUND_SETTINGS";
    private static final String PREFERENCES_TWITTER_FOLLOW = "PREFERENCES_TWITTER_FOLLOW";
    private static final String PREFERENCES_WAS_COMENTED = "PREFERENCES_WAS_COMENTED";
    private SharedPreferences mSharedPreferences;

    static {
        NULL_STRING = null;
    }

    public SharePreferencesUtil(Context context) {
        if (context == null) {
            context = ColorPaintKidsApplication.getContext();
        }
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearPreferences() {
        Editor editor = this.mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setLaunch(int launch) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putInt(PREFERENCES_NUMBER_LAUNCHED, launch);
        editor.apply();
    }

    public int getLaunch() {
        return this.mSharedPreferences.getInt(PREFERENCES_NUMBER_LAUNCHED, 0);
    }

    public void setRated(boolean rated) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_WAS_COMENTED, rated);
        editor.apply();
    }

    public boolean isRated() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_WAS_COMENTED, false);
    }

    public void setLastTimeOpened(long opened) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putLong(PREFERENCES_LAST_TIME_OPENED, opened);
        editor.apply();
    }

    public long getLastTimeOpened() {
        return this.mSharedPreferences.getLong(PREFERENCES_LAST_TIME_OPENED, 0);
    }

    public void setPremiumAllInOne(boolean isPremium) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERECES_PREMIUM_ALL_IN_ONE, isPremium);
        editor.apply();
    }

    public boolean isPremiumAllInOne() {
        return this.mSharedPreferences.getBoolean(PREFERECES_PREMIUM_ALL_IN_ONE, false);
    }

    public void setPremiumRemoveAds(boolean isPremium) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERECES_PREMIUM_REMOVE_ADS, isPremium);
        editor.apply();
    }

    public boolean isPremiumRemoveAds() {
        return this.mSharedPreferences.getBoolean(PREFERECES_PREMIUM_REMOVE_ADS, false);
    }

    public void setPremiumMoreImages(boolean isPremium) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERECES_PREMIUM_MORE_IMAGES, isPremium);
        editor.apply();
    }

    public boolean isPremiumMoreImages() {
        return this.mSharedPreferences.getBoolean(PREFERECES_PREMIUM_MORE_IMAGES, false);
    }

    public void setFacebookLike(boolean like) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_FACEBOOK_LIKE, like);
        editor.apply();
    }

    public boolean isFacebookLike() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_FACEBOOK_LIKE, false);
    }

    public void setTwitterFollow(boolean follow) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_TWITTER_FOLLOW, follow);
        editor.apply();
    }

    public boolean isTwitterFollow() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_TWITTER_FOLLOW, false);
    }

    public boolean isPrivacy() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_PRIVACY, false);
    }

    public void setPrivacy(boolean privacy) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_PRIVACY, privacy);
        editor.apply();
    }

    public boolean isRateFirstTime() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_RATE_FIRST_ITEM, true);
    }

    public void setRateFirstTime(boolean firstTime) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_RATE_FIRST_ITEM, firstTime);
        editor.apply();
    }

    public boolean isSoundSettings() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_SOUND_SETTINGS, true);
    }

    public void setSoundSettings(boolean soundSettings) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_SOUND_SETTINGS, soundSettings);
        editor.apply();
    }

    public boolean isMusicSettings() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_MUSIC_SETTINGS, false);
    }

    public void setMusicSettings(boolean soundSettings) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_MUSIC_SETTINGS, soundSettings);
        editor.apply();
    }


    public void setSoundPlayed(boolean played) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(PREFERENCES_SOUND_PLAYED, played);
        editor.apply();
    }

    public boolean getSoundPlayed() {
        return this.mSharedPreferences.getBoolean(PREFERENCES_SOUND_PLAYED, false);
    }
}
