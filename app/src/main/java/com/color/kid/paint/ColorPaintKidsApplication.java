package com.color.kid.paint;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tung on 1/18/2017.
 */

public class ColorPaintKidsApplication extends Application {
    private static final String PROPERTY_ID = "UA-52348719-11";
    private static ColorPaintKidsApplication mInstance;

    public ColorPaintKidsApplication() {
        mInstance = this;
    }

    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mInstance;
    }


}