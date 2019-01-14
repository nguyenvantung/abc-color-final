package com.color.kid.coloring.object;

import android.opengl.GLES20;

import com.color.kid.coloring.data.VertexArray;
import com.color.kid.coloring.program.TextureShaderProgram;


public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = 16;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final float[] VERTEX_DATA;
    private final VertexArray vertexArray;

    static {
        VERTEX_DATA = new float[]{0.0f, 0.0f, 0.5f, 0.5f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f};
    }

    public Table() {
        this.vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        this.vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
        this.vertexArray.setVertexAttribPointer(TEXTURE_COORDINATES_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw(boolean blendingOn) {
        if (blendingOn) {
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(1, 771);
        }
        GLES20.glDrawArrays(6, 0, 6);
        if (blendingOn) {
            GLES20.glDisable(3042);
        }
    }
}
