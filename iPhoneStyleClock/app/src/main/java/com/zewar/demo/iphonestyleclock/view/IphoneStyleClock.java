package com.zewar.demo.iphonestyleclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Zewar on 2016/10/10.
 */
public class IphoneStyleClock extends View {
    //默认宽高
    private final int DEFAULT_MIN_WIDTH_HEIGHT = 200;

    //默认时钟边框圆角 200/5
    private float mDefaultBorderRadius = 40F;

    //默认时钟表盘与边框的间距 200/20
    private float mDefaultRoundRadiusSpace = 10F;

    //默认时钟表盘中心原点半径 200/50
    private float mDefaultCenterPointRadius = 4F;

    //默认时针宽度 200/100
    private float mDefaultHourPointerWidth = 2F;

    //默认时针长度 (200 / 2 - mDefaultRoundRadiusSpace) * 0.5
    private float mDefaultHourPointerLength = 45F;

    //默认分针宽度
    private float mDefaultMinutePointerWidth;

    //默认分针长度
    private float mDefaultMinutePointerLength;

    //默认秒针宽度
    private float mDefaultSecondPointerWidth;

    //默认秒针长度
    private float mDefaultSecondPointerLength;

    private Paint mPaint;

    private boolean mIsWorking = false;

    public IphoneStyleClock(Context context) {
        this(context, null);
    }

    public IphoneStyleClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = DEFAULT_MIN_WIDTH_HEIGHT;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(result, size);
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = DEFAULT_MIN_WIDTH_HEIGHT;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(result, size);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawClockBoundBorder(canvas);

        drawClockRoundBorder(canvas);

        drawClockCenterPoint(canvas);

//        drawClockDegree(canvas);

        drawClockNumber(canvas);

        drawClockAllPointer(canvas);
    }

    //画表外边框
    private void drawClockBoundBorder(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        //根据宽算出大概的圆角
        mDefaultBorderRadius = wh / 5F;
        canvas.drawRoundRect(new RectF(0, 0, wh, wh), mDefaultBorderRadius, mDefaultBorderRadius, mPaint);
    }

    //画表盘
    private void drawClockRoundBorder(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        //根据宽算出大概的圆角
        mDefaultRoundRadiusSpace = wh / 20F;
        canvas.drawCircle(wh / 2, wh / 2, wh / 2 - mDefaultRoundRadiusSpace, mPaint);
    }

    //画表盘中心点
    private void drawClockCenterPoint(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        //根据宽算出大概的圆角
        mDefaultCenterPointRadius = wh / 50F;
        canvas.drawCircle(wh / 2, wh / 2, mDefaultCenterPointRadius, mPaint);
    }

    //画表盘刻度线
    private void drawClockDegree(Canvas canvas) {
        float degreeLength;
        float[] centerPoint = computeCenterPoint();
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setStrokeWidth(4);
                degreeLength = mDefaultRoundRadiusSpace;
            } else {
                mPaint.setStrokeWidth(2);
                degreeLength = mDefaultRoundRadiusSpace / 2;
            }
            float offset = mDefaultRoundRadiusSpace + degreeLength;
            canvas.drawLine(centerPoint[0], 0, centerPoint[0], offset, mPaint);
            canvas.rotate(6, centerPoint[0], centerPoint[1]);
        }
    }

    //画表盘数字
    private void drawClockNumber(Canvas canvas) {
        float degreeMaxOffset = mDefaultRoundRadiusSpace + mDefaultRoundRadiusSpace;
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mDefaultRoundRadiusSpace * 1.6F);
        mPaint.setFakeBoldText(true);
        for (int i = 0; i < 12; i++) {
            float[] points = computePointerPoint((i + 1) * 30, computeCenterPoint()[0] - degreeMaxOffset - mDefaultRoundRadiusSpace);
            String text = (i + 1) + "";
            Rect rect = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), rect);
            float textHeight = rect.height();
            float textWidth = rect.width();
            canvas.drawText(text, points[2], points[3] + textHeight / 2, mPaint);
        }
    }

    //画时分秒指针
    private void drawClockAllPointer(Canvas canvas) {

        float ha;
        float ma;
        float sa;

        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR);
        float minute = calendar.get(Calendar.MINUTE);
        float second = calendar.get(Calendar.SECOND);

        sa = second * 6F;
        ma = (minute + second / 60) * 6F;
        ha = (hour + minute / 60 + second / 3600) * 30F;

        drawClockHourPointer(ha, canvas);
        drawClockMinutePointer(ma, canvas);
        drawClockSecondPointer(sa, canvas);
    }

    //画时针指针
    private void drawClockHourPointer(float angle, Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        mDefaultHourPointerWidth = wh / 50F;

        //时针长度取表盘半径的一半
        mDefaultHourPointerLength = (wh / 2 - mDefaultRoundRadiusSpace) * 0.5F;

        mPaint.setStrokeWidth(mDefaultHourPointerWidth);
        float[] points = computePointerPoint(angle, mDefaultHourPointerLength);
        canvas.drawLine(points[0], points[1], points[2], points[3], mPaint);
    }

    //画分针指针
    private void drawClockMinutePointer(float angle, Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        mDefaultMinutePointerWidth = wh / 75F;

        //分针长度取表盘半径的一半
        mDefaultMinutePointerLength = (wh / 2 - mDefaultRoundRadiusSpace) * 0.8F;

        mPaint.setStrokeWidth(mDefaultMinutePointerWidth);
        float[] points = computePointerPoint(angle, mDefaultMinutePointerLength);
        canvas.drawLine(points[0], points[1], points[2], points[3], mPaint);
    }

    //画秒针指针
    private void drawClockSecondPointer(float angle, Canvas canvas) {
        mPaint.setColor(Color.RED);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //根据长宽最小决定相同宽高，防止变形以及超出屏幕范围
        float wh = Math.min(width, height);
        mDefaultSecondPointerWidth = wh / 100F;

        //秒针长度取表盘半径的一半
        mDefaultSecondPointerLength = (wh / 2 - mDefaultRoundRadiusSpace) * 0.8F;

        mPaint.setStrokeWidth(mDefaultSecondPointerWidth);
        float[] points = computePointerPoint(angle, mDefaultSecondPointerLength);
        canvas.drawLine(points[0], points[1], points[2], points[3], mPaint);
    }

    //根据角度、指针长度 计算出起始坐标
    private float[] computePointerPoint(float angle, float pointerLenght) {
        float[] linePoints = new float[4];
        float[] centerPoint = computeCenterPoint();

        linePoints[0] = centerPoint[0];//startX
        linePoints[1] = centerPoint[1];//startY
        if (angle <= 90F) {
            linePoints[2] = linePoints[0] + (float) Math.sin(Math.toRadians(angle)) * pointerLenght;//endX
            linePoints[3] = linePoints[1] - (float) Math.cos(Math.toRadians(angle)) * pointerLenght;//endY
        } else if (angle <= 180F) {
            linePoints[2] = linePoints[0] + (float) Math.cos(Math.toRadians(angle - 90F)) * pointerLenght;//endX
            linePoints[3] = linePoints[1] + (float) Math.sin(Math.toRadians(angle - 90F)) * pointerLenght;//endY
        } else if (angle <= 270F) {
            linePoints[2] = linePoints[0] - (float) Math.sin(Math.toRadians(angle - 180F)) * pointerLenght;//endX
            linePoints[3] = linePoints[1] + (float) Math.cos(Math.toRadians(angle - 180F)) * pointerLenght;//endY
        } else if (angle <= 360F) {
            linePoints[2] = linePoints[0] - (float) Math.cos(Math.toRadians(angle - 270F)) * pointerLenght;//endX
            linePoints[3] = linePoints[1] - (float) Math.sin(Math.toRadians(angle - 270F)) * pointerLenght;//endY
        }

        return linePoints;
    }

    //计算圆心坐标
    private float[] computeCenterPoint() {
        float[] centerPoint = new float[2];
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int wh = Math.min(width, height);
        int ct = wh / 2;
        centerPoint[0] = ct;
        centerPoint[1] = ct;
        return centerPoint;
    }

    public void startClockWork() {
        if (mIsWorking)
            return;
        mIsWorking = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsWorking) {
                    postInvalidate();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopClockWork() {
        mIsWorking = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startClockWork();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopClockWork();
    }
}
