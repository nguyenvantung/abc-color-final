package com.color.kid.colorpaintkids.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int[] loadTexture(Context context, int resourceId) {
        int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            return null;
        }
        Options options = new Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return null;
        }
        GLES20.glBindTexture(3553, textureObjectIds[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glBindTexture(3553, 0);
        return textureObjectIds;
    }

    public static int[] loadTexture(Context context, Bitmap bitmap) {
        int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            return null;
        }
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return null;
        }
        GLES20.glBindTexture(3553, textureObjectIds[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
        return textureObjectIds;
    }

    public static int[] loadRepeatingTexture(Context context, Bitmap bitmap) {
        int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            return null;
        }
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return null;
        }
        GLES20.glBindTexture(3553, textureObjectIds[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 10497);
        GLES20.glTexParameteri(3553, 10243, 10497);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
        return textureObjectIds;
    }
}
