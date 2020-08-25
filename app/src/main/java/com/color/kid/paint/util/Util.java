package com.color.kid.paint.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tung on 1/18/2017.
 */

public class Util {
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static Bitmap getBitmapFromView(int width, int height, int color, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            bitmap.eraseColor(ContextCompat.getColor(context, color));
        }else {
            bitmap.eraseColor(color);
        }
        return bitmap;
    }

    public static File saveBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            return saveImage(bitmap);
        }
        return null;
    }

    private static File saveImage(Bitmap finalBitmap) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "saved_images");
        DebugLog.e("saveImage:" + myDir.mkdirs());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fName = "kid" + timeStamp +".jpg";
        File file = null;
        try {
            myDir.mkdirs();
            if (!myDir.exists()){
                myDir.createNewFile();
            }
            file = new File(myDir, fName);
            if (file.exists()) file.delete ();
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void  playSong(Context context, int sound){
        MediaPlayer  mediaPlayer = MediaPlayer.create(context, sound);
        mediaPlayer.start();
    }
}
