package com.color.kid.paint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;


import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.color.kid.paint.object.BackgroundTable;
import com.color.kid.paint.object.Table;
import com.color.kid.paint.program.RepeatTextureShaderProgram;
import com.color.kid.paint.program.TextureShaderProgram;
import com.color.kid.paint.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Tung on 1/18/2017.
 */

public class RenderColor implements GLSurfaceView.Renderer{
    private float mAspectRatio;
    private Bitmap mBackgroungBitmap;
    private float mCenterScaleX;
    private float mCenterScaleY;
    private Bitmap mColoringBitmap;
    private final Context mContext;
    private float mDistanceX;
    private float mDistanceY;
    private int mHeight;
    private Bitmap mOverlayBitmap;
    private float mScaleRatio;
    private BackgroundTable mTableBackground;
    private int[] mTextureBackground;
    private RepeatTextureShaderProgram mTextureProgramBackground;
    private int mWidht;
    private final float[] modelMatrix;
    private float[] projectionMatrix;
    private final float[] projectionMatrixFirst;
    private Table table;
    private Table tableColoring;
    private int[] textureColoring;
    private int[] textureOverlay;
    private TextureShaderProgram textureProgram;
    private TextureShaderProgram textureProgramColoring;

    public RenderColor(Context context) {
        this.projectionMatrixFirst = new float[16];
        this.projectionMatrix = new float[16];
        this.modelMatrix = new float[16];
        this.mDistanceX = 0.0f;
        this.mDistanceY = 0.0f;
        this.mContext = context;
        this.mScaleRatio = 1.0f;
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 0.0f);
        this.table = new Table();
        this.tableColoring = new Table();
        this.mTableBackground = new BackgroundTable();
        this.textureProgram = new TextureShaderProgram(this.mContext);
        this.textureProgramColoring = new TextureShaderProgram(this.mContext);
        this.mTextureProgramBackground = new RepeatTextureShaderProgram(this.mContext);
        this.textureOverlay = TextureHelper.loadTexture(this.mContext, this.mOverlayBitmap);
        this.textureColoring = TextureHelper.loadTexture(this.mContext, this.mColoringBitmap);
        this.mTextureBackground = TextureHelper.loadRepeatingTexture(this.mContext, this.mBackgroungBitmap);
    }

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        this.mWidht = width;
        this.mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        this.mAspectRatio = width > height ? ((float) width) / ((float) height) : ((float) height) / ((float) width);
        if (width > height) {
            Matrix.orthoM(this.projectionMatrixFirst, 0, -this.mAspectRatio, this.mAspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        } else {
            Matrix.orthoM(this.projectionMatrixFirst, 0, -1.0f, 1.0f, -this.mAspectRatio, this.mAspectRatio, -1.0f, 1.0f);
        }
        this.projectionMatrix = (float[]) this.projectionMatrixFirst.clone();
        handleActivityRecreated();
    }

    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
        GLES20.glClear(AccessibilityNodeInfoCompat.ACTION_COPY);
        this.mTextureProgramBackground.useProgram();
        this.mTextureProgramBackground.setUniforms(this.projectionMatrix, this.mTextureBackground[0]);
        this.mTableBackground.bindData(this.textureProgramColoring);
        this.mTableBackground.draw(false);
        this.textureProgramColoring.useProgram();
        this.textureProgramColoring.setUniforms(this.projectionMatrix, this.textureColoring[0]);
        this.tableColoring.bindData(this.textureProgramColoring);
        this.tableColoring.draw(true);
        this.textureProgram.useProgram();
        this.textureProgram.setUniforms(this.projectionMatrix, this.textureOverlay[0]);
        this.table.bindData(this.textureProgram);
        this.table.draw(true);
        GLES20.glFlush();
        GLES20.glFinish();
    }

    private void handleActivityRecreated() {
        float[] localMatrix = (float[]) this.projectionMatrixFirst.clone();
        Matrix.setIdentityM(this.modelMatrix, 0);
        recalculateDistanceAndCenter();
        if (this.mWidht > this.mHeight) {
            Matrix.translateM(this.modelMatrix, 0, (this.mDistanceX + this.mCenterScaleX) * this.mAspectRatio, this.mDistanceY + this.mCenterScaleY, 0.0f);
        } else {
            Matrix.translateM(this.modelMatrix, 0, this.mDistanceX + this.mCenterScaleX, (this.mDistanceY + this.mCenterScaleY) * this.mAspectRatio, 0.0f);
        }
        if (this.mWidht > this.mHeight) {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        } else {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        }
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, localMatrix, 0, this.modelMatrix, 0);
        System.arraycopy(temp, 0, localMatrix, 0, temp.length);
        this.projectionMatrix = localMatrix;
    }

    public void setScaleRatioResume(float scaleRation, float centerScaleX, float centerScaleY, float distanceX, float distanceY) {
        this.mScaleRatio = scaleRation;
        this.mCenterScaleX = centerScaleX;
        this.mCenterScaleY = centerScaleY;
        this.mDistanceX = distanceX;
        this.mDistanceY = distanceY;
    }

    public void setScaleRatio(float scaleRation, float centerScaleX, float centerScaleY, float distanceX, float distanceY) {
        float[] localMatrix = (float[]) this.projectionMatrixFirst.clone();
        this.mScaleRatio = scaleRation;
        this.mCenterScaleX = centerScaleX;
        this.mCenterScaleY = centerScaleY;
        this.mDistanceX -= distanceX;
        this.mDistanceY -= distanceY;
        Matrix.setIdentityM(this.modelMatrix, 0);
        recalculateDistanceAndCenter();
        if (this.mWidht > this.mHeight) {
            Matrix.translateM(this.modelMatrix, 0, (this.mDistanceX + this.mCenterScaleX) * this.mAspectRatio, this.mDistanceY + this.mCenterScaleY, 0.0f);
        } else {
            Matrix.translateM(this.modelMatrix, 0, this.mDistanceX + this.mCenterScaleX, (this.mDistanceY + this.mCenterScaleY) * this.mAspectRatio, 0.0f);
        }
        if (this.mWidht > this.mHeight) {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        } else {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        }
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, localMatrix, 0, this.modelMatrix, 0);
        System.arraycopy(temp, 0, localMatrix, 0, temp.length);
        this.projectionMatrix = localMatrix;
    }

    public void setScaleRatio(float scaleRation, float centerScaleX, float centerScaleY) {
        float[] localMatrix = (float[]) this.projectionMatrixFirst.clone();
        this.mScaleRatio = scaleRation;
        this.mCenterScaleX = centerScaleX;
        this.mCenterScaleY = centerScaleY;
        this.mDistanceX = 0.0f;
        this.mDistanceY = 0.0f;
        Matrix.setIdentityM(this.modelMatrix, 0);
        recalculateDistanceAndCenter();
        if (this.mWidht > this.mHeight) {
            Matrix.translateM(this.modelMatrix, 0, (this.mDistanceX + this.mCenterScaleX) * this.mAspectRatio, this.mDistanceY + this.mCenterScaleY, 0.0f);
        } else {
            Matrix.translateM(this.modelMatrix, 0, this.mDistanceX + this.mCenterScaleX, (this.mDistanceY + this.mCenterScaleY) * this.mAspectRatio, 0.0f);
        }
        if (this.mWidht > this.mHeight) {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        } else {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        }
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, localMatrix, 0, this.modelMatrix, 0);
        System.arraycopy(temp, 0, localMatrix, 0, temp.length);
        this.projectionMatrix = localMatrix;
    }

    public void handleMove(float distanceX, float distanceY) {
        this.mDistanceX -= distanceX;
        this.mDistanceY -= distanceY;
        float[] localMatrix = (float[]) this.projectionMatrixFirst.clone();
        Matrix.setIdentityM(this.modelMatrix, 0);
        recalculateDistanceAndCenter();
        if (this.mWidht > this.mHeight) {
            Matrix.translateM(this.modelMatrix, 0, (this.mDistanceX + this.mCenterScaleX) * this.mAspectRatio, this.mDistanceY + this.mCenterScaleY, 0.0f);
        } else {
            Matrix.translateM(this.modelMatrix, 0, this.mDistanceX + this.mCenterScaleX, (this.mDistanceY + this.mCenterScaleY) * this.mAspectRatio, 0.0f);
        }
        if (this.mWidht > this.mHeight) {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        } else {
            Matrix.scaleM(this.modelMatrix, 0, this.mScaleRatio, this.mScaleRatio, 0.0f);
        }
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, localMatrix, 0, this.modelMatrix, 0);
        System.arraycopy(temp, 0, localMatrix, 0, temp.length);
        this.projectionMatrix = localMatrix;
    }

    private void recalculateDistanceAndCenter() {
        float canMoveBy;
        float canMoveByRatio;
        if (this.mWidht > this.mHeight) {
            canMoveBy = (1.05f * this.mScaleRatio) - 1.05f;
            canMoveByRatio = ((this.mScaleRatio * 1.05f) / this.mAspectRatio) - 1.05f;
            if (this.mCenterScaleY > canMoveBy) {
                this.mCenterScaleY = canMoveBy;
            }
            if (this.mCenterScaleY < -1.0f * canMoveBy) {
                this.mCenterScaleY = -1.0f * canMoveBy;
            }
            if (this.mScaleRatio > this.mAspectRatio) {
                if (this.mCenterScaleX > canMoveByRatio) {
                    this.mCenterScaleX = canMoveByRatio;
                }
                if (this.mCenterScaleX < -1.0f * canMoveByRatio) {
                    this.mCenterScaleX = -1.0f * canMoveByRatio;
                }
            } else {
                this.mCenterScaleX = 0.0f;
            }
        } else {
            canMoveBy = (1.05f * this.mScaleRatio) - 1.05f;
            canMoveByRatio = ((this.mScaleRatio * 1.05f) / this.mAspectRatio) - 1.05f;
            if (this.mCenterScaleX > canMoveBy) {
                this.mCenterScaleX = canMoveBy;
            }
            if (this.mCenterScaleX < -1.0f * canMoveBy) {
                this.mCenterScaleX = -1.0f * canMoveBy;
            }
            if (this.mScaleRatio > this.mAspectRatio) {
                if (this.mCenterScaleY > canMoveByRatio) {
                    this.mCenterScaleY = canMoveByRatio;
                }
                if (this.mCenterScaleY < -1.0f * canMoveByRatio) {
                    this.mCenterScaleY = -1.0f * canMoveByRatio;
                }
            } else {
                this.mCenterScaleY = 0.0f;
            }
        }
        if (this.mWidht > this.mHeight) {
            canMoveBy = (1.05f * this.mScaleRatio) - 1.05f;
            canMoveByRatio = ((this.mScaleRatio * 1.05f) / this.mAspectRatio) - 1.05f;
            if (this.mDistanceY + this.mCenterScaleY > canMoveBy) {
                this.mDistanceY = canMoveBy - this.mCenterScaleY;
            }
            if (this.mDistanceY + this.mCenterScaleY < -1.0f * canMoveBy) {
                this.mDistanceY = (-1.0f * canMoveBy) - this.mCenterScaleY;
            }
            if (this.mScaleRatio > this.mAspectRatio) {
                if (this.mDistanceX + this.mCenterScaleX > canMoveByRatio) {
                    this.mDistanceX = canMoveByRatio - this.mCenterScaleX;
                }
                if (this.mDistanceX + this.mCenterScaleX < -1.0f * canMoveByRatio) {
                    this.mDistanceX = (-1.0f * canMoveByRatio) - this.mCenterScaleX;
                    return;
                }
                return;
            }
            this.mDistanceX = 0.0f;
            return;
        }
        canMoveBy = (1.05f * this.mScaleRatio) - 1.05f;
        canMoveByRatio = ((this.mScaleRatio * 1.05f) / this.mAspectRatio) - 1.05f;
        if (this.mDistanceX + this.mCenterScaleX > canMoveBy) {
            this.mDistanceX = canMoveBy - this.mCenterScaleX;
        }
        if (this.mDistanceX + this.mCenterScaleX < -1.0f * canMoveBy) {
            this.mDistanceX = (-1.0f * canMoveBy) - this.mCenterScaleX;
        }
        if (this.mScaleRatio > this.mAspectRatio) {
            if (this.mDistanceY + this.mCenterScaleY > canMoveByRatio) {
                this.mDistanceY = canMoveByRatio - this.mCenterScaleY;
            }
            if (this.mDistanceY + this.mCenterScaleY < -1.0f * canMoveByRatio) {
                this.mDistanceY = (-1.0f * canMoveByRatio) - this.mCenterScaleY;
                return;
            }
            return;
        }
        this.mDistanceY = 0.0f;
    }

    public void setColoringBitmap(Bitmap coloringBitmap) {
        this.mColoringBitmap = coloringBitmap;
    }

    public void setOverlayBitmap(Bitmap overlayBitmap) {
        this.mOverlayBitmap = overlayBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.mBackgroungBitmap = backgroundBitmap;
    }

    public void floodFillRefresh() {
        if (this.textureColoring != null && this.textureColoring.length > 0) {
            GLES20.glBindTexture(3553, this.textureColoring[0]);
            GLUtils.texSubImage2D(3553, 0, 0, 0, this.mColoringBitmap);
            GLES20.glBindTexture(3553, 0);
        }
    }

    public void pathDrawRefresh() {
        GLES20.glBindTexture(3553, this.textureColoring[0]);
        GLUtils.texSubImage2D(3553, 0, 0, 0, this.mColoringBitmap);
        GLES20.glBindTexture(3553, 0);
    }

    public void onDestroyView() {
        try {
            GLES20.glDeleteTextures(1, this.textureOverlay, 0);
        } catch (Exception ex) {
            Log.e("Textures", ex.toString());
        }
        try {
            GLES20.glDeleteTextures(1, this.textureColoring, 0);
        } catch (Exception ex2) {
            Log.e("Textures", ex2.toString());
        }
        try {
            GLES20.glDeleteTextures(1, this.mTextureBackground, 0);
        } catch (Exception ex22) {
            Log.e("Textures", ex22.toString());
        }
        try {
            this.textureProgram.deleteProgram();
        } catch (Exception ex222) {
            Log.e("Textures", ex222.toString());
        }
        try {
            this.textureProgramColoring.deleteProgram();
        } catch (Exception ex2222) {
            Log.e("Textures", ex2222.toString());
        }
        try {
            this.mTextureProgramBackground.deleteProgram();
        } catch (Exception ex22222) {
            Log.e("Textures", ex22222.toString());
        }
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    public float getCenterX() {
        return this.mCenterScaleX;
    }

    public float getCenterY() {
        return this.mCenterScaleY;
    }

    public float getDistanceX() {
        return this.mDistanceX;
    }

    public float getDistanceY() {
        return this.mDistanceY;
    }

    public float getTotalDistanceX() {
        return this.mCenterScaleX + this.mDistanceX;
    }

    public float getTotalDistanceY() {
        return this.mCenterScaleY + this.mDistanceY;
    }

    public void handleBeginScale() {
        this.mCenterScaleX += this.mDistanceX;
        this.mCenterScaleY += this.mDistanceY;
        this.mDistanceY = 0.0f;
        this.mDistanceX = 0.0f;
    }

    public int getWidth() {
        return this.mWidht;
    }

    public int getHeigh() {
        return this.mHeight;
    }
}
