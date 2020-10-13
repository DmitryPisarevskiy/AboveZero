package com.dmitry.pisarevskiy.abovezero;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

public class SunriseSunsetView extends View {
    private static final String TAG = "SunriseSunsetView";
    private static final int SUN_RADIUS = 40;
    private int sunColor = R.color.yellow;
    private int mainColor = R.color.colorAccent;
    private final RectF mainRectangle = new RectF();
    private final RectF levelRectangle = new RectF();
    private Paint mainPaint;
    private Paint sunPaint;
    private int width;
    private int height;
    private int sunrise;
    private int sunset;
    private int dt;

    private final static int PADDING = 10;

    public SunriseSunsetView(Context context) {
        super(context);
        init();
    }

    public SunriseSunsetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public SunriseSunsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public SunriseSunsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SunriseSunsetView, 0, 0);
        sunrise = typedArray.getInteger(R.styleable.SunriseSunsetView_sunrise, 0);
        sunset = typedArray.getInteger(R.styleable.SunriseSunsetView_sunset, 18000);
        dt = typedArray.getInteger(R.styleable.SunriseSunsetView_dt, 9000);
        typedArray.recycle();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        sunPaint = new Paint();
        sunPaint.setColor(sunColor);
        sunPaint.setStyle(Paint.Style.FILL);

        mainPaint = new Paint();
        mainPaint.setColor(mainColor);
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setTextSize(200f);
        mainPaint.setAlpha(50);
        mainPaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Получить реальные ширину и высоту
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();
        // Отрисовка View
        mainRectangle.set(
                PADDING,
                PADDING,
                width - PADDING,
                2 * (height - PADDING));
        float x = width * (float) (dt - sunrise) / (sunset - sunrise) - (float) width / 2;
        float y = (float) ((float) height / width * 2 * Math.sqrt((float) width * width / 4 - x * x));
        levelRectangle.set(
                PADDING + (float) width / 2 + x - (float) SUN_RADIUS / 2,
                PADDING + height - y - (float) SUN_RADIUS / 2,
                PADDING + (float) width / 2 + x + (float) SUN_RADIUS / 2,
                PADDING + height - y + (float) SUN_RADIUS / 2);
    }

    // Вызывается, когда надо нарисовать элемент
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mainRectangle, 180, 360, false, mainPaint);
        canvas.drawArc(levelRectangle, 0, 360, false, sunPaint);
        Date date = new java.util.Date(sunrise*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", Locale.US);
        canvas.drawText(sdf.format(date),2*PADDING,height-2*PADDING,mainPaint);
        date = new java.util.Date(sunset*1000L);
        canvas.drawText(sdf.format(date),width - 2*PADDING,height-2*PADDING,mainPaint);
    }
}

