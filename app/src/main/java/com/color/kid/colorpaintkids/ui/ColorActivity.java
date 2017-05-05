package com.color.kid.colorpaintkids.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.color.kid.colorpaintkids.R;
import com.color.kid.colorpaintkids.adapter.OptionColorAdapter;
import com.color.kid.colorpaintkids.adapter.viewHolder.ColorViewHolder;
import com.color.kid.colorpaintkids.constance.Constants;
import com.color.kid.colorpaintkids.util.DebugLog;
import com.color.kid.colorpaintkids.util.QueueLinearFloodFiller;
import com.color.kid.colorpaintkids.util.SharePreferencesUtil;
import com.color.kid.colorpaintkids.util.Util;
import com.color.kid.colorpaintkids.view.DialogShareImage;
import com.color.kid.colorpaintkids.view.ItemOffsetDecoration;
import com.color.kid.colorpaintkids.view.RenderColor;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tung on 1/18/2017.
 */

public class ColorActivity extends FragmentActivity implements GestureDetector.OnGestureListener, ColorViewHolder.SelectItemColor {

    private static final int PERMISSON_READ_EXTERNAL_STORAGE = 69;
    @Bind(R.id.surfaceView)
    GLSurfaceView surfaceView;

    @Bind(R.id.listToolColor)
    RecyclerView listToolColor;

    @Bind(R.id.toolBucket)
    ImageView imgBucket;

    @Bind(R.id.toolBush)
    ImageView imgBush;

    @Bind(R.id.toolEraser)
    ImageView imgEraser;

    @Bind(R.id.toolDelete)
    ImageView imgDelete;

    @Bind(R.id.toolDone)
    ImageView imgDone;

    @Bind(R.id.img_select_color)
    ImageView imgSelect;

    private RenderColor renderColor;
    private ColoringGLRendererSaver mRendererSaver;
    private Bitmap mColoringBitmap;
    private Bitmap mOverlayBitmap;
    private Bitmap mBackgroundBitmap;
    private GestureDetectorCompat gestureDetectorCompat;
    private float mScaleFactor = 1.0f;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private float mX;
    private float mY;
    private Tool mSelectedTool;

    private boolean mIsScaling;
    private float mPrevScale = 1.0f;
    private boolean mRendererSet;

    private boolean isDelete = false;
    private boolean isBucket = false;

    // handle undo
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Paint> paints = new ArrayList<Paint>();

    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<Paint> undonePaints = new ArrayList<Paint>();

    private int drawableData = 0;
    private int colorDraw;

    private MediaPlayer mediaPlayer;
    private SharePreferencesUtil sharePreferencesUtil;
    private StartAppAd startAppAd;

    @Override
    public void onSelectColor(int color) {
       colorDraw = getResources().getColor(color);
        setColorOption(color);
    }

    public void setColorOption(int color){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            imgSelect.setColorFilter(this.getResources().getColor(color), PorterDuff.Mode.MULTIPLY);
        }else {
            imgSelect.setColorFilter(getResources().getColor(color), PorterDuff.Mode.MULTIPLY);
        }

    }

    public enum Tool {
        BRUSH,
        BUCKET,
        ZOOM,
        ERASER
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_color);

        ButterKnife.bind(this);

        intitData();

    }

    public void intitData() {
        sharePreferencesUtil = new SharePreferencesUtil(this);
        drawableData = getIntent().getExtras().getInt(Constants.KEY_DRAWABLE);
        surfaceView.setEGLContextClientVersion(2);

        mediaPlayer = MediaPlayer.create(this, R.raw.bgr_happy_sunshine);
        mediaPlayer.setLooping(true);
        if (sharePreferencesUtil.getSoundPlayed()){
            mediaPlayer.start();
        }


        colorDraw = getResources().getColor(R.color.aquamarine);
        renderColor = new RenderColor(this);
        mRendererSaver = new ColoringGLRendererSaver();
        setupBitmap();

        surfaceView.setRenderer(renderColor);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);
        this.mRendererSet = true;

        setupPaint();
        this.mIsScaling = false;
        mSelectedTool = Tool.BRUSH;
        imgBush.setSelected(true);

        listToolColor.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_color);
        listToolColor.addItemDecoration(itemDecoration);
        OptionColorAdapter optionColorAdapter = new OptionColorAdapter();
        optionColorAdapter.setSelectItemColor(this);
        listToolColor.setAdapter(optionColorAdapter);
        imgSelect.setColorFilter(getBaseContext().getResources().getColor(R.color.aquamarine));

    }

    public void loadStartApp(){
        StartAppSDK.init(this, Constants.START_APP_ID, true);
        startAppAd = new StartAppAd(this);
    }

    public void setupPaint(){
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (startAppAd == null){
            loadStartApp();
        }else {
            startAppAd.onBackPressed();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setupBitmap(){
        mColoringBitmap = Bitmap.createBitmap(Constants.WIDTH_BITMAP, Constants.HEIGHT_BITMAP, Bitmap.Config.ARGB_8888);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mColoringBitmap.eraseColor(ContextCompat.getColor(this, R.color.white));
        }else {
            mColoringBitmap.eraseColor(getResources().getColor(R.color.white));
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        BitmapFactory.Options optionsBackground = new BitmapFactory.Options();
        options.inScaled = false;
        this.mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg, optionsBackground);
        this.mOverlayBitmap = BitmapFactory.decodeResource(getResources(), drawableData, options);
        mColoringBitmap = Util.overlay(mColoringBitmap, mOverlayBitmap);

        renderColor.setColoringBitmap(this.mColoringBitmap);
        renderColor.setOverlayBitmap(this.mOverlayBitmap);
        renderColor.setBackgroundBitmap(this.mBackgroundBitmap);
        renderColor.setScaleRatioResume(this.mScaleFactor, this.mRendererSaver.mCenterScaleX, this.mRendererSaver.mCenterScaleY, this.mRendererSaver.mDistanceX, this.mRendererSaver.mDistanceY);

    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (mSelectedTool == Tool.BRUSH) {
            this.mPaint.setColor(colorDraw);
            this.mPaint.setXfermode(null);
            this.handleDrawDown((event.getX() * 896.0f) / ((float) surfaceView.getWidth()), (event.getY() * 896.0f) / ((float) surfaceView.getHeight()));

        } else if (mSelectedTool == Tool.ERASER) {
            mPaint.setColor(Color.WHITE);
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
            this.handleDrawMove((e2.getX() * 896.0f) / ((float) this.surfaceView.getWidth()), (e2.getY() * 896.0f) / ((float) this.surfaceView.getHeight()));
        }else if (mSelectedTool == Tool.ERASER) {
            if (!mIsScaling) {
                handleDrawMove((e2.getX() * 896.0f) / ((float) surfaceView.getWidth()), (e2.getY() * 896.0f) / ((float) surfaceView.getHeight()));
            }
        } else if (mSelectedTool == Tool.ZOOM || mSelectedTool == Tool.BUCKET) {
            surfaceView.queueEvent(new HandleMoveZoom((distanceX / ((float) surfaceView.getWidth())) * 2.0f, -((distanceY / ((float) surfaceView.getHeight())) * 2.0f)));
        }

        return false;
    }
    class HandleMoveZoom implements Runnable {
        final /* synthetic */ float val$distanceNormalizedX;
        final /* synthetic */ float val$distanceNormalizedY;

        HandleMoveZoom(float f, float f2) {
            this.val$distanceNormalizedX = f;
            this.val$distanceNormalizedY = f2;
        }

        public void run() {
           renderColor.handleMove(this.val$distanceNormalizedX, this.val$distanceNormalizedY);
        }
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
        long pixelY = Math.round((distanceY * pixelPerPointY) + ((1.0d + (SharePreferencesUtil.NULL_DOUBLE * ((double) normalizedY))) * pixelPerPointY));

        if (pixelX >= 0 && pixelX < 896 && pixelY >= 0 && pixelY < 896) {
            int targetColor = mColoringBitmap.getPixel((int) pixelX, (int) pixelY);
            QueueLinearFloodFiller queueLinearFloodFiller = new QueueLinearFloodFiller(mColoringBitmap, targetColor, colorDraw);
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
        // handle undo

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
            // handle undo
          /*  paths.add(mPath);
            paints.add(mPaint);
            for (Path p : paths) {
                Paint paint = paints.get(paintIndex);
                mCanvas.drawPath(p, paint);
                paintIndex++;
            }*/
            this.mCanvas.drawPath(this.mPath, this.mPaint);
            this.surfaceView.queueEvent(new Runnable() {
                public void run() {
                    renderColor.pathDrawRefresh();
                }
            });
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
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && sharePreferencesUtil.getSoundPlayed()){
            mediaPlayer.start();
        }
        if (startAppAd == null){
            loadStartApp();
        }else {
            startAppAd.onResume();
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
        mediaPlayer.pause();
        if (startAppAd == null){
            loadStartApp();
        }else {
            startAppAd.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.renderColor != null) {
            this.renderColor.onDestroyView();
        }
        mediaPlayer.stop();
    }

    @OnClick(R.id.toolBush)
    void handleBush(){
        mSelectedTool = Tool.BRUSH;
        if (isDelete && isBucket){
            mColoringBitmap = Util.overlay(mColoringBitmap, mOverlayBitmap);
            renderColor.setColoringBitmap(this.mColoringBitmap);
            renderColor.setOverlayBitmap(this.mOverlayBitmap);
            renderColor.setBackgroundBitmap(this.mBackgroundBitmap);
            setupPaint();
            isDelete = false;
            isBucket = false;
        }
        imgBucket.setSelected(false);
        imgBush.setSelected(true);
        imgEraser.setSelected(false);
    }

    @OnClick(R.id.toolBucket)
    void handleBucket(){
        mSelectedTool = Tool.BUCKET;
        isBucket = true;
        if (isDelete){
            mColoringBitmap = Util.overlay(mColoringBitmap, mOverlayBitmap);
            renderColor.setColoringBitmap(this.mColoringBitmap);
        }

        imgBucket.setSelected(true);
        imgBush.setSelected(false);
        imgEraser.setSelected(false);

    }

    @OnClick(R.id.toolEraser)
    void handleEraser(){
        mSelectedTool = Tool.ERASER;
        imgBucket.setSelected(false);
        imgBush.setSelected(false);
        imgEraser.setSelected(true);
    }

    @OnClick(R.id.toolZoom)
    void handleZoom(){
        mSelectedTool = Tool.ZOOM;
    }

    @OnClick(R.id.toolDelete)
    void handleDelete(){
        isDelete = true;
        mColoringBitmap.eraseColor(Color.WHITE);
        surfaceView.queueEvent(new DeleteTool());
    }

    class DeleteTool implements Runnable {
        DeleteTool() {
        }

        public void run() {
            renderColor.floodFillRefresh();
        }
    }

   @OnClick(R.id.toolDone)
    void handleDone(){
       if (paths.size() > 0) {
           undonePaths.add(paths.remove(paths.size() - 1));
           undonePaints.add(paints.remove(paints.size() - 1));
           if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
               mPaint.setColor(ContextCompat.getColor(this,R.color.colorAccent));
           }else {
               mPaint.setColor(getResources().getColor(R.color.colorAccent));
           }

           surfaceView.queueEvent(new DeleteTool());
       }
       checkPermission();

  }

    private boolean checkWriteExternalPermission(){
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkWriteExternalPermission()) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSON_READ_EXTERNAL_STORAGE);
        } else {
            showDialogShare();
        }
    }

    public void showDialogShare(){
        if (isDelete){
            mColoringBitmap = Util.overlay(mColoringBitmap, mOverlayBitmap);
            renderColor.setColoringBitmap(this.mColoringBitmap);
        }
        DialogShareImage dialogShareImage = new DialogShareImage(this, mColoringBitmap, new DialogShareImage.ShareCallBack() {
            @Override
            public void onCallBackDialog(boolean select) {
                if (select){
                    shareImage();
                }else {
                    if (startAppAd == null){
                        loadStartApp();
                    }else {
                        startAppAd.show();
                    }
                }
            }
        });
        dialogShareImage.show();
    }

    public void shareImage(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
        String path = Util.savebitmap(mColoringBitmap, getResources().getResourceEntryName(drawableData)).getPath();
        Uri screenshotUri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share image via..."));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSON_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DebugLog.e("Permission" + "Granted");
                    showDialogShare();
                }

                break;
        }
    }


}
