package com.color.kid.paint.program;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.color.kid.paint.util.ShaderHelper;


abstract class ShaderProgram {
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected final int program;

    protected ShaderProgram(Context context, String a, String b) {
        this.program = ShaderHelper.buildProgram(a, b);
    }

    public void useProgram() {
        GLES20.glUseProgram(this.program);
    }

    public void deleteProgram() {
        try {
            GLES20.glDeleteProgram(this.program);
        } catch (Exception ex) {
            Log.e("Textures", ex.toString());
        }
    }
}
