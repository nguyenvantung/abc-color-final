package com.color.kid.paint.program;

import android.content.Context;
import android.opengl.GLES20;

import com.color.kid.paint.shaders.Shaders;


public class RepeatTextureShaderProgram extends ShaderProgram {
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    public /* bridge */ /* synthetic */ void deleteProgram() {
        super.deleteProgram();
    }

    public /* bridge */ /* synthetic */ void useProgram() {
        super.useProgram();
    }

    public RepeatTextureShaderProgram(Context context) {
        super(context, Shaders.TEXTURE_VERTEX_SHADER, Shaders.TEXTURE_FRAGMENT_SHADER);
        this.uMatrixLocation = GLES20.glGetUniformLocation(this.program, "u_Matrix");
        this.uTextureUnitLocation = GLES20.glGetUniformLocation(this.program, "u_TextureUnit");
        this.aPositionLocation = GLES20.glGetAttribLocation(this.program, "a_Position");
        this.aTextureCoordinatesLocation = GLES20.glGetAttribLocation(this.program, "a_TextureCoordinates");
    }

    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(this.uMatrixLocation, 1, false, matrix, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, textureId);
        GLES20.glUniform1i(this.uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return this.aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return this.aTextureCoordinatesLocation;
    }
}
