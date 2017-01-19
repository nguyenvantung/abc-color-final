package com.color.kid.colorpaintkids.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.util.DebugLog;
import com.color.kid.colorpaintkids.util.QueueLinearFloodFiller;
import com.color.kid.colorpaintkids.util.QueueLinearFloodFillerSingleton;
import com.color.kid.colorpaintkids.util.SharePreferencesUtil;
import com.color.kid.colorpaintkids.util.Util;
import com.color.kid.colorpaintkids.view.RenderColor;

/**
 * Created by Tung on 1/18/2017.
 */

public class ColorActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GLSurfaceView surfaceView;
    private RenderColor renderColor;
    private ColoringGLRendererSaver mRendererSaver;
    private Bitmap mColoringBitmap;
    private Bitmap mOverlayBitmap;
    private Bitmap mBackgroundBitmap;
    private GestureDetectorCompat gestureDetectorCompat;
    private QueueLinearFloodFillerSingleton queueLinearFloodFillerSingleton;
    private float mScaleFactor = 1.0f;
    private int mColorBucket = Color.BLUE;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private float mX;
    private float mY;
    private Tool mSelectedTool;

    private boolean mIsScaling;
    private float mPrevScale;
    private boolean mRendererSet;
    private ScaleGestureDetector mScaleDetector;

    public enum Tool {
        BRUSH,
        BUCKET,
        ZOOM,
        ERASER
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        surfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setEGLContextClientVersion(2);
        renderColor = new RenderColor(this);
        mRendererSaver = new ColoringGLRendererSaver();
        intitData();
        renderColor.setColoringBitmap(this.mColoringBitmap);
        renderColor.setOverlayBitmap(this.mOverlayBitmap);
        renderColor.setBackgroundBitmap(this.mBackgroundBitmap);
        renderColor.setScaleRatioResume(this.mScaleFactor, this.mRendererSaver.mCenterScaleX, this.mRendererSaver.mCenterScaleY, this.mRendererSaver.mDistanceX, this.mRendererSaver.mDistanceY);
        surfaceView.setRenderer(this.renderColor);
        this.mRendererSet = true;
        this.mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        gestureDetectorCompat = new GestureDetectorCompat(this, this);
    }

    public void intitData() {
        mColoringBitmap = Bitmap.createBitmap(Constants.WIDTH_BITMAP, Constants.HEIGHT_BITMAP, Bitmap.Config.ARGB_8888);
        this.mColoringBitmap.eraseColor(Color.WHITE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        this.mOverlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture_1, options);
        BitmapFactory.Options optionsBackground = new BitmapFactory.Options();
        options.inScaled = false;
        this.mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg, optionsBackground);
        this.queueLinearFloodFillerSingleton = QueueLinearFloodFillerSingleton.getInstance();
        mColoringBitmap = Util.overlay(mColoringBitmap, mOverlayBitmap);
        this.queueLinearFloodFillerSingleton.setup(this.mOverlayBitmap, this.mColoringBitmap);
        //
        this.mCanvas = new Canvas(this.mColoringBitmap);
        this.mPath = new Path();
        this.mPaint = new Paint();
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeWidth(20.0f);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mPaint.setAlpha(255);
        this.mPaint.setXfermode(null);
        mSelectedTool = Tool.BRUSH;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {

        if (mSelectedTool == Tool.BRUSH) {
            this.mPaint.setColor(getResources().getColor(R.color.colorAccent));
            this.mPaint.setXfermode(null);
            Log.e("onDown", "onDown:" + surfaceView.getWidth() + surfaceView.getHeight());
            this.handleDrawDown((event.getX() * mColoringBitmap.getWidth()) / ((float) this.surfaceView.getWidth()) ,
                    (event.getY() * mColoringBitmap.getHeight()) / ((float) this.surfaceView.getHeight()) - 190);
        } else if (mSelectedTool == Tool.ERASER) {
            mPaint.setColor(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            handleDrawDown((event.getX() * 896.0f) / ((float) surfaceView.getWidth()), (event.getY() * 896.0f) / ((float) surfaceView.getHeight()));
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {

        if (mSelectedTool == Tool.BUCKET) {
            handleFloodFillTouch(((event.getX() / ((float) this.surfaceView.getWidth())) * 2.0f) - 1.0f,
                    -(((event.getY() / ((float) this.surfaceView.getHeight())) * 2.0f) - 1.0f));
        }
        if (mSelectedTool == Tool.ZOOM) {
            float normalizedX = ((event.getX() / ((float) surfaceView.getWidth())) * 2.0f) - 1.0f;
            float normalizedY = -(((event.getY() / ((float) surfaceView.getHeight())) * 2.0f) - 1.0f);
            renderColor.handleBeginScale();
            handleZoomTouch(normalizedX, normalizedY);
        }

        return false;
    }

    private void handleZoomTouch(float normalizedX, float normalizedY) {
        float finalDistanceX = (this.renderColor.getTotalDistanceX() / this.mScaleFactor) + ((normalizedX / this.mScaleFactor) * -1.0f);
        float finalDistanceY = (this.renderColor.getTotalDistanceY() / this.mScaleFactor) + ((normalizedY / this.mScaleFactor) * -1.0f);
        if (this.mScaleFactor < 2.5f) {
            this.mPrevScale = 5.0f;
            this.mScaleFactor = 5.0f;
        } else {
            this.mPrevScale = 1.0f;
            this.mScaleFactor = 1.0f;
        }
        surfaceView.queueEvent(new AnimationZoom(finalDistanceX, finalDistanceY));
    }

    class AnimationZoom implements Runnable {
        final /* synthetic */ float val$finalDistanceX;
        final /* synthetic */ float val$finalDistanceY;

        AnimationZoom(float f, float f2) {
            this.val$finalDistanceX = f;
            this.val$finalDistanceY = f2;
        }

        public void run() {
            renderColor.setScaleRatio(mScaleFactor, this.val$finalDistanceX * mScaleFactor, this.val$finalDistanceY * mScaleFactor);
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mSelectedTool == Tool.BRUSH){
            this.handleDrawMove((e2.getX() * 896.0f) / ((float) this.surfaceView.getWidth()), (e2.getY() * 896.0f) / ((float) this.surfaceView.getHeight())-(surfaceView.getHeight()/4 + 25));
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void handleFloodFillTouch(float normalizedX, float normalizedY) {
        double pixelPerPointX;
        double pixelPerPointY;
        double distanceX = (double) this.renderColor.getTotalDistanceX();
        double distanceY = (double) this.renderColor.getTotalDistanceY();
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            if (distanceX > 0.0d) {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) this.mScaleFactor) - 1.0d;
            } else {
                distanceY = (((double) this.mScaleFactor) - 1.0d) - (SharePreferencesUtil.NULL_DOUBLE * distanceY);
            }
        } else {
            if (distanceX > 0.0d) {
                distanceX = (((double) this.mScaleFactor) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) this.mScaleFactor) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d;
            } else {
                distanceY = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) - (SharePreferencesUtil.NULL_DOUBLE * distanceY);
            }
        }
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            pixelPerPointX = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
            pixelPerPointY = 896.0d / (((double) this.mScaleFactor) * 2.0d);
        } else {
            pixelPerPointX = 896.0d / (((double) this.mScaleFactor) * 2.0d);
            pixelPerPointY = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
        }
        long pixelX = Math.round((distanceX * pixelPerPointX) + ((1.0d + ((double) normalizedX)) * pixelPerPointX));
        long pixelY = Math.round((distanceY * pixelPerPointY) + ((1.0d + (SharePreferencesUtil.NULL_DOUBLE * ((double) normalizedY))) * pixelPerPointY)-225);
        DebugLog.e("fill:" + pixelY + pixelX);
        mColorBucket = getResources().getColor(R.color.colorPrimary);
        if (pixelX >= 0 && pixelX < 896 && pixelY >= 0 && pixelY < 896) {
            int replacementColor = getResources().getColor(R.color.colorPrimary);
            int targetColor = mColoringBitmap.getPixel((int) pixelX, (int) pixelY);
            QueueLinearFloodFiller queueLinearFloodFiller = new QueueLinearFloodFiller(mColoringBitmap, targetColor, replacementColor);
            queueLinearFloodFiller.setTolerance(100);
            queueLinearFloodFiller.floodFill((int) pixelX, (int) pixelY);
            this.surfaceView.queueEvent(new Runnable() {
                public void run() {
                    renderColor.floodFillRefresh();
                }
            });
        }
    }

    public void handleDrawDown(float x, float y) {
        double pixelPerPointX;
        double pixelPerPointY;
        double distanceX = (double) this.renderColor.getTotalDistanceX();
        double distanceY = (double) this.renderColor.getTotalDistanceY();
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            if (distanceX > 0.0d) {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) this.mScaleFactor) - 1.0d;
            } else {
                distanceY = (((double) this.mScaleFactor) - 1.0d) - (SharePreferencesUtil.NULL_DOUBLE * distanceY);
            }
        } else {
            if (distanceX > 0.0d) {
                distanceX = (((double) this.mScaleFactor) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) this.mScaleFactor) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d;
            } else {
                distanceY = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) ;
            }
        }
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            pixelPerPointX = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
            pixelPerPointY = 896.0d / (((double) this.mScaleFactor) * 2.0d);
        } else {
            pixelPerPointX = 896.0d / (((double) this.mScaleFactor) * 2.0d);
            pixelPerPointY = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
        }
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            x *= this.renderColor.getAspectRatio();
        } else {
            y *= this.renderColor.getAspectRatio();
        }
        x = (float) ((distanceX * pixelPerPointX) + ((double) (x / this.mScaleFactor)));
        y = (float) ((distanceY * pixelPerPointY) + ((double) (y / this.mScaleFactor)));
        this.mPath.reset();
        this.mPath.moveTo(x, y);
        this.mPath.quadTo(x, y, x, 1.0f + y);
        this.mCanvas.drawPath(this.mPath, this.mPaint);
        surfaceView.queueEvent(new Runnable() {
            public void run() {
                renderColor.pathDrawRefresh();
            }
        });
        this.mX = x;
        this.mY = y;
    }

    public void handleDrawMove(float x, float y) {
        double pixelPerPointX;
        double pixelPerPointY;
        double distanceX = (double) this.renderColor.getTotalDistanceX();
        double distanceY = (double) this.renderColor.getTotalDistanceY();
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            if (distanceX > 0.0d) {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) this.mScaleFactor) - 1.0d;
            } else {
                distanceY = (((double) this.mScaleFactor) - 1.0d) - (SharePreferencesUtil.NULL_DOUBLE * distanceY);
            }
        } else {
            if (distanceX > 0.0d) {
                distanceX = (((double) this.mScaleFactor) - 1.0d) - distanceX;
            } else {
                distanceX = (((double) this.mScaleFactor) - 1.0d) + (SharePreferencesUtil.NULL_DOUBLE * distanceX);
            }
            if (distanceY > 0.0d) {
                distanceY += ((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d;
            } else {
                distanceY = (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) - 1.0d) - (SharePreferencesUtil.NULL_DOUBLE * distanceY);
            }
        }
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            pixelPerPointX = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
            pixelPerPointY = 896.0d / (((double) this.mScaleFactor) * 2.0d);
        } else {
            pixelPerPointX = 896.0d / (((double) this.mScaleFactor) * 2.0d);
            pixelPerPointY = 896.0d / (((double) (this.mScaleFactor / this.renderColor.getAspectRatio())) * 2.0d);
        }
        if (this.renderColor.getWidth() > this.renderColor.getHeigh()) {
            x *= this.renderColor.getAspectRatio();
        } else {
            y *= this.renderColor.getAspectRatio();
        }
        x = (float) ((distanceX * pixelPerPointX) + ((double) (x / this.mScaleFactor)));
        y = (float) ((distanceY * pixelPerPointY) + ((double) (y / this.mScaleFactor)));
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= 4.0f || dy >= 4.0f) {
            this.mPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            this.mX = x;
            this.mY = y;
            this.mCanvas.drawPath(this.mPath, this.mPaint);
            this.surfaceView.queueEvent(new Runnable() {
                public void run() {
                    renderColor.pathDrawRefresh();
                }
            });
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float mFirstFocusX;
        float mFirstFocusY;
        float mPrevCenterX;
        float mPrevCenterY;
        float mPrevDifX;
        float mPrevDifY;

        private ScaleListener() {
            this.mFirstFocusX = 0.0f;
            this.mFirstFocusY = 0.0f;
            this.mPrevDifX = 0.0f;
            this.mPrevDifY = 0.0f;
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mIsScaling = true;
            float centerY = -(((detector.getFocusY() / ((float) surfaceView.getHeight())) * 2.0f) - 1.0f);
            this.mFirstFocusX = ((detector.getFocusX() / ((float) surfaceView.getWidth())) * 2.0f) - 1.0f;
            this.mFirstFocusY = centerY;
            renderColor.handleBeginScale();
            this.mPrevDifX = (renderColor.getCenterX() / mPrevScale) * -1.0f;
            this.mPrevDifY = (renderColor.getCenterY() / mPrevScale) * -1.0f;
            this.mPrevCenterX = AutoScrollHelper.NO_MAX;
            this.mPrevCenterY = AutoScrollHelper.NO_MAX;
            return true;
        }

        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = mScaleFactor * detector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f));
            float helpScale = mScaleFactor / mPrevScale;
            float dx = 1.0f - Math.abs(this.mFirstFocusX);
            float dy = 1.0f - Math.abs(this.mFirstFocusY);
            float difX = (1.0f - (dx - (dx / helpScale))) - (1.0f / helpScale);
            float difY = (1.0f - (dy - (dy / helpScale))) - (1.0f / helpScale);
            if (this.mFirstFocusX < 0.0f) {
                difX *= -1.0f;
            }
            if (this.mFirstFocusY < 0.0f) {
                difY *= -1.0f;
            }
            difX = this.mPrevDifX + (difX / mPrevScale);
            difY = this.mPrevDifY + (difY / mPrevScale);
            float currentSpanX = ((detector.getFocusX() / ((float) surfaceView.getWidth())) * 2.0f) - 1.0f;
            float currentSpanY = ((detector.getFocusY() / ((float) surfaceView.getWidth())) * 2.0f) - 1.0f;
            float distanceX = this.mPrevCenterX - currentSpanX;
            float distanceY = this.mPrevCenterY - currentSpanY;
            if (this.mPrevCenterX == AutoScrollHelper.NO_MAX || this.mPrevCenterY == AutoScrollHelper.NO_MAX) {
                distanceX = 0.0f;
                distanceY = 0.0f;
            }
            this.mPrevCenterX = currentSpanX;
            this.mPrevCenterY = currentSpanY;
            handleScale(mScaleFactor, (-1.0f * difX) * mScaleFactor, (-1.0f * difY) * mScaleFactor, distanceX, -1.0f * distanceY);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            mPrevScale = mScaleFactor;
            this.mFirstFocusX = 0.0f;
            this.mFirstFocusY = 0.0f;
        }
    }

    private void handleScale(float scale, float centerX, float centerY, float distanceX, float distanceY) {
        surfaceView.queueEvent(new AnonymousClass(scale, centerX, centerY, distanceX, distanceY));
        surfaceView.invalidate();
    }

    class AnonymousClass implements Runnable {
        final /* synthetic */ float val$centerX;
        final /* synthetic */ float val$centerY;
        final /* synthetic */ float val$distanceX;
        final /* synthetic */ float val$distanceY;
        final /* synthetic */ float val$scale;

        AnonymousClass(float f, float f2, float f3, float f4, float f5) {
            this.val$scale = f;
            this.val$centerX = f2;
            this.val$centerY = f3;
            this.val$distanceX = f4;
            this.val$distanceY = f5;
        }

        public void run() {
            renderColor.setScaleRatio(this.val$scale, this.val$centerX, this.val$centerY, this.val$distanceX, this.val$distanceY);
        }
    }

    private class ColoringGLRendererSaver {
        float mCenterScaleX;
        float mCenterScaleY;
        float mDistanceX;
        float mDistanceY;

        public ColoringGLRendererSaver() {
            this.mCenterScaleX = 0.0f;
            this.mCenterScaleY = 0.0f;
            this.mDistanceX = 0.0f;
            this.mDistanceY = 0.0f;
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mRendererSet) {
            surfaceView.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mRendererSet) {
            surfaceView.onPause();
        }
        if (this.renderColor != null) {
            this.mRendererSaver.mCenterScaleX = this.renderColor.getCenterX();
            this.mRendererSaver.mCenterScaleY = this.renderColor.getCenterY();
            this.mRendererSaver.mDistanceX = this.renderColor.getDistanceX();
            this.mRendererSaver.mDistanceY = this.renderColor.getDistanceY();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.renderColor != null) {
            this.renderColor.onDestroyView();
        }
    }
}
