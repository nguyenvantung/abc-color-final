package com.color.kid.colorpaintkids.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.util.DebugLog;
import com.color.kid.colorpaintkids.util.QueueLinearFloodFiller;
import com.color.kid.colorpaintkids.util.QueueLinearFloodFillerSingleton;
import com.color.kid.colorpaintkids.util.SharePreferencesUtil;
import com.color.kid.colorpaintkids.util.Util;
import com.color.kid.colorpaintkids.view.RenderColor;

import butterknife.Bind;

/**
 * Created by Tung on 1/18/2017.
 */

public class ColorFragment extends BaseFragment implements GestureDetector.OnGestureListener , View.OnTouchListener{

    @Bind(R.id.surfaceView)
    GLSurfaceView surfaceView;

    private RenderColor renderColor;
    private Bitmap mColoringBitmap;
    private Bitmap mOverlayBitmap;
    private Bitmap mBackgroundBitmap;
    private GestureDetectorCompat gestureDetectorCompat;
    private QueueLinearFloodFillerSingleton queueLinearFloodFillerSingleton;
    private float mScaleFactor = 1.0f;
    private int mColorBucket = Color.BLUE;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_color;
    }

    @Override
    protected void initView(View root) {

        initDataView();

    }

    @Override
    protected void initData() {
        surfaceView.setEGLContextClientVersion(2);
        renderColor = new RenderColor(getActivity());
        renderColor.setColoringBitmap(this.mColoringBitmap);
        renderColor.setOverlayBitmap(this.mOverlayBitmap);
        renderColor.setBackgroundBitmap(this.mBackgroundBitmap);
        //renderColor.setScaleRatioResume(this.mScaleFactor, this.mRendererSaver.mCenterScaleX, this.mRendererSaver.mCenterScaleY, this.mRendererSaver.mDistanceX, this.mRendererSaver.mDistanceY);
        surfaceView.setRenderer(this.renderColor);
        gestureDetectorCompat = new GestureDetectorCompat(getActivity(),ColorFragment.this);
        surfaceView.setOnTouchListener(this);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        handleFloodFillTouch(((event.getX() / ((float) this.surfaceView.getWidth())) * 2.0f) - 1.0f,
                -(((event.getY() / ((float) this.surfaceView.getHeight())) * 2.0f) - 1.0f));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void initDataView(){
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

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
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
        long pixelY = Math.round((distanceY * pixelPerPointY) + ((1.0d + (SharePreferencesUtil.NULL_DOUBLE * ((double) normalizedY))) * pixelPerPointY));
        DebugLog.e("pixelY" + pixelY);
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
           /* this.queueLinearFloodFillerSingleton.floodFill((int) pixelX, (int) pixelY, this.mColorBucket, Util.overlay(mColoringBitmap, mOverlayBitmap));
            this.surfaceView.queueEvent(new Runnable() {
                public void run() {
                    renderColor.floodFillRefresh();
                }
            });
            queueLinearFloodFillerSingleton.floodFillFinish();*/
        }
    }
}
