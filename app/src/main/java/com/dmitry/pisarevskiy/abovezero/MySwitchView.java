package com.dmitry.pisarevskiy.abovezero;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import static android.graphics.Color.*;

public class MySwitchView extends View{
    private static final String TAG = "MySwitchView";
    private int backColor = R.color.colorAccent;
    private int mainColor = R.color.colorPrimary;
    private final RectF mainRectangle = new RectF();
    private final RectF shadowRectangle = new RectF();
    private final RectF levelRectangle = new RectF();
    private Paint mainPaint;
    private Paint backPaint;
    private Paint shadowPaint;
    private int width;
    private int height;

    private boolean right = false;
    private OnClickListener listener;

    private final static int PADDING = 10;
    private final static int SHADOW_SWING = 5;

    public MySwitchView(Context context) {
        super(context);
        init();
    }

    public boolean isRight() {
        return right;
    }

    public MySwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public MySwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public MySwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                       int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MySwitchView, 0, 0);
        backColor = typedArray.getColor(R.styleable.MySwitchView_back_color, R.color.colorPrimary);
        mainColor = typedArray.getColor(R.styleable.MySwitchView_main_color, R.color.colorAccent);
        right = typedArray.getBoolean(R.styleable.MySwitchView_right,false);
        typedArray.recycle();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        backPaint = new Paint();
        backPaint.setColor(backColor);
        backPaint.setStyle(Paint.Style.FILL);

        mainPaint = new Paint();
        mainPaint.setColor(mainColor);
        mainPaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint();
        shadowPaint.setColor(DKGRAY);
        shadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

         // Получить реальные ширину и высоту
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();

        // Отрисовка Switch
        mainRectangle.set(PADDING,
                PADDING,
                width - PADDING,
                height - PADDING);
        shadowRectangle.set(PADDING+ SHADOW_SWING,
                PADDING+SHADOW_SWING,
                width - PADDING+ SHADOW_SWING,
                height - PADDING+ SHADOW_SWING);
        levelRectangle.set(PADDING,
                PADDING,
                width - PADDING,
                height - PADDING);
    }

    // Вызывается, когда надо нарисовать элемент
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(shadowRectangle, 0, 360, false, shadowPaint);
        canvas.drawArc(mainRectangle,0,360,false,backPaint);
        if (right) {
            canvas.drawArc(levelRectangle,-70,140,false,mainPaint);
        } else {
            canvas.drawArc(levelRectangle,110,140,false,mainPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            right = !right;
            invalidate();
            if (listener != null)
                listener.onClick(this);
        }
        return true;
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
