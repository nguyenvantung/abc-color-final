package com.color.kid.colorpaintkids.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Tung on 1/18/2017.
 */

public class QueueLinearFloodFillerSingleton {
    private static final int mHeight = 896;
    private static final int[] mPixels;
    private static final boolean[] mPixelsChecked;
    private static final int[] mPixelsImage;
    private static final int mWidth = 896;
    private static QueueLinearFloodFillerSingleton sInstance;
    private int mFillColor;
    private Queue<FloodFillRange> mRanges;
    protected int[] mStartColor;
    protected int mStartColorSingle;

    private class FloodFillRange {
        public int f5Y;
        public int endX;
        public int startX;

        public FloodFillRange(int startX, int endX, int y) {
            this.startX = startX;
            this.endX = endX;
            this.f5Y = y;
        }
    }

    static {
        mPixels = new int[802816];
        mPixelsImage = new int[802816];
        mPixelsChecked = new boolean[802816];
    }

    public static synchronized QueueLinearFloodFillerSingleton getInstance() {
        QueueLinearFloodFillerSingleton queueLinearFloodFillerSingleton;
        synchronized (QueueLinearFloodFillerSingleton.class) {
            if (sInstance == null) {
                sInstance = new QueueLinearFloodFillerSingleton();
            }
            queueLinearFloodFillerSingleton = sInstance;
        }
        return queueLinearFloodFillerSingleton;
    }

    public QueueLinearFloodFillerSingleton() {
        this.mFillColor = 0;
        this.mStartColor = new int[]{0, 0, 0, 0};
    }

    public void setup(Bitmap imgOverlay, Bitmap imgColoring) {
        imgOverlay.getPixels(mPixels, 0, mWidth, 0, 0, mWidth, mWidth);
        imgColoring.getPixels(mPixelsImage, 0, mWidth, 0, 0, mWidth, mWidth);
        this.mRanges = new LinkedList();
        for (int i = 0; i < mPixelsChecked.length; i++) {
            mPixelsChecked[i] = false;
        }
    }

    public void clearImage() {
        if (this.mRanges != null) {
            this.mRanges.clear();
        }
        sInstance = null;
    }

    public void floodFill(int x, int y, int newColor, Bitmap bitmap) {
        bitmap.getPixels(mPixelsImage, 0, mWidth, 0, 0, mWidth, mWidth);
        setStartColor(mPixelsImage[(y * mWidth) + x]);
        setFillColor(newColor);
        if (Color.alpha(mPixels[(y * mWidth) + x]) != 255) {
            LinearFill(x, y);
            while (this.mRanges.size() > 0) {
                FloodFillRange range = (FloodFillRange) this.mRanges.remove();
                int upY = range.f5Y - 1;
                int downY = range.f5Y + 1;
                int downPxIdx = (downY * mWidth) + range.startX;
                int upPxIdx = (upY * mWidth) + range.startX;
                int i;
                if (range.f5Y > 0 && range.f5Y < 895) {
                    for (i = range.startX; i <= range.endX; i++) {
                        if (!mPixelsChecked[upPxIdx] && CheckPixel(upPxIdx)) {
                            LinearFill(i, upY);
                        }
                        if (!mPixelsChecked[downPxIdx] && CheckPixel(downPxIdx)) {
                            LinearFill(i, downY);
                        }
                        downPxIdx++;
                        upPxIdx++;
                    }
                } else if (range.f5Y > 0) {
                    for (i = range.startX; i <= range.endX; i++) {
                        if (!mPixelsChecked[upPxIdx] && CheckPixel(upPxIdx)) {
                            LinearFill(i, upY);
                        }
                        upPxIdx++;
                    }
                } else if (range.f5Y < 895) {
                    for (i = range.startX; i <= range.endX; i++) {
                        if (!mPixelsChecked[downPxIdx] && CheckPixel(downPxIdx)) {
                            LinearFill(i, downY);
                        }
                        downPxIdx++;
                    }
                }
            }
            bitmap.setPixels(mPixelsImage, 0, mWidth, 0, 0, mWidth, mWidth);
        }
    }

    public void floodFillFinish() {
        this.mRanges.clear();
        for (int i = 0; i < mPixelsChecked.length; i++) {
            mPixelsChecked[i] = false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void LinearFill(int r9, int r10) {
        /*
        r8 = this;
        r7 = 896; // 0x380 float:1.256E-42 double:4.427E-321;
        r6 = 1;
        r0 = r9;
        r4 = r10 * 896;
        r1 = r4 + r9;
    L_0x0008:
        r4 = mPixelsImage;
        r5 = r8.mFillColor;
        r4[r1] = r5;
        r4 = mPixelsChecked;
        r4[r1] = r6;
        r0 = r0 + -1;
        r1 = r1 + -1;
        if (r0 < 0) goto L_0x0024;
    L_0x0018:
        r4 = mPixelsChecked;
        r4 = r4[r1];
        if (r4 != 0) goto L_0x0024;
    L_0x001e:
        r4 = r8.CheckPixel(r1);
        if (r4 != 0) goto L_0x0008;
    L_0x0024:
        r0 = r0 + 1;
        r3 = r9 + 1;
        r4 = r10 * 896;
        r4 = r4 + r9;
        r1 = r4 + 1;
        if (r3 >= r7) goto L_0x0057;
    L_0x002f:
        r4 = mPixelsChecked;
        r4 = r4[r1];
        if (r4 != 0) goto L_0x0057;
    L_0x0035:
        r4 = r8.CheckPixel(r1);
        if (r4 == 0) goto L_0x0057;
    L_0x003b:
        r4 = mPixelsImage;
        r5 = r8.mFillColor;
        r4[r1] = r5;
        r4 = mPixelsChecked;
        r4[r1] = r6;
        r3 = r3 + 1;
        r1 = r1 + 1;
        if (r3 >= r7) goto L_0x0057;
    L_0x004b:
        r4 = mPixelsChecked;
        r4 = r4[r1];
        if (r4 != 0) goto L_0x0057;
    L_0x0051:
        r4 = r8.CheckPixel(r1);
        if (r4 != 0) goto L_0x003b;
    L_0x0057:
        r3 = r3 + -1;
        r2 = new com.coloring.book.animals.algorithm.QueueLinearFloodFillerSingleton$FloodFillRange;
        r2.<init>(r0, r3, r10);
        r4 = r8.mRanges;
        r4.offer(r2);
        return;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.color.kid.opengldemo1.utils.QueueLinearFloodFillerSingleton.LinearFill(int, int):void");
    }

    private boolean CheckPixel(int px) {
        if (Color.alpha(mPixels[px]) == 255) {
            mPixelsImage[px] = this.mFillColor;
            return false;
        } else if (mPixelsImage[px] == this.mStartColorSingle) {
            return true;
        } else {
            return false;
        }
    }

    private void setFillColor(int value) {
        this.mFillColor = value;
    }

    private void setStartColor(int startColor) {
        this.mStartColorSingle = startColor;
        this.mStartColor[0] = Color.red(startColor);
        this.mStartColor[1] = Color.green(startColor);
        this.mStartColor[2] = Color.blue(startColor);
        this.mStartColor[3] = Color.alpha(startColor);
    }
}
