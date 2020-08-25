package com.color.kid.paint.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.color.kid.paint.R;


/**
 * Created by Tung Nguyen on 12/28/2016.
 */
@SuppressLint("AppCompatCustomView")
public class GradientTextView extends TextView {
    private int colorEndGradient;
    private int colorStartGradient;

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        this.colorStartGradient = a.getColor(R.styleable.GradientTextView_colorStartGradient, ContextCompat.getColor(getContext(), R.color.select_picture_text_start));
        this.colorEndGradient = a.getColor(R.styleable.GradientTextView_colorEndGradient, ContextCompat.getColor(getContext(), R.color.select_picture_text_ends));
        a.recycle();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        this.colorStartGradient = a.getColor(R.styleable.GradientTextView_colorStartGradient, ContextCompat.getColor(getContext(), R.color.select_picture_text_start));
        this.colorEndGradient = a.getColor(R.styleable.GradientTextView_colorEndGradient, ContextCompat.getColor(getContext(), R.color.select_picture_text_ends));
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        getPaint().setShadowLayer(1.0f, 0.0f, TypedValue.applyDimension(1, 3.0f, getResources().getDisplayMetrics()), ContextCompat.getColor(getContext(), R.color.select_picture_text_shadow));
        getPaint().setShader(null);
        super.onDraw(canvas);
        TextPaint paint = getPaint();
        paint.clearShadowLayer();
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        setTextColor(ContextCompat.getColor(getContext(), R.color.select_picture_text_outline));
        paint.setStrokeWidth((float) ((int) TypedValue.applyDimension(1, 3.0f, getResources().getDisplayMetrics())));
        super.onDraw(canvas);
        if (this.colorStartGradient != ContextCompat.getColor(getContext(), R.color.select_picture_text_start) && this.colorEndGradient != ContextCompat.getColor(getContext(), R.color.select_picture_text_ends)) {
            getPaint().setStrokeWidth(0.0f);
            getPaint().setStyle(Paint.Style.FILL);
            setTextColor(this.colorEndGradient);
            getPaint().setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) getHeight(), this.colorStartGradient, this.colorEndGradient, Shader.TileMode.CLAMP));
            super.onDraw(canvas);
        }
    }
}

