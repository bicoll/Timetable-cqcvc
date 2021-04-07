package com.evercocer.educationhelper.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class WeekthView extends View {
    private String weekTH = "null";
    private String sx = "";
    private Paint mPaint;

    public WeekthView(Context context) {
        this(context, null);
    }

    public WeekthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(70);
        mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        canvas.drawText(weekTH, getWidth() / 6, getMeasuredHeight()-20, mPaint);

        mPaint.setTextSize(20);
        mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        switch (weekTH){
            case "1":
                sx = "st";
                break;
            case "2":
                sx = "nd";
                break;
            case "3":
                sx = "rd";
                break;
            case "11":
                sx = "st";
                break;
            case "12":
                sx = "nd";
                break;
            case "13":
                sx = "rd";
                break;
            default:
                sx = "th";
                break;
        }
        canvas.drawText(sx,getWidth()/4+35,getHeight()-20,mPaint);
    }

    public String getWeekTH() {
        return weekTH;
    }

    public void setWeekTH(String weekTH) {
        this.weekTH = weekTH;
    }
}
