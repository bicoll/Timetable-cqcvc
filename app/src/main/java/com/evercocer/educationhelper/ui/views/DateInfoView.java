package com.evercocer.educationhelper.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.evercocer.educationhelper.other.DateInfo;

import java.util.ArrayList;

public class DateInfoView extends View {
    private ArrayList<DateInfo> dateInfos;
    private Paint mPaint;

    public ArrayList<DateInfo> getDateInfos() {
        return dateInfos;
    }

    public void setDateInfos(ArrayList<DateInfo> dateInfos) {
        this.dateInfos = dateInfos;
    }

    public DateInfoView(Context context) {
        this(context, null);
    }

    public DateInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dateInfos == null)
            return;


        int singleWidth = getWidth() / 7;
        int height = getHeight() / 2;
        int width = 0;
        String week = "星期";
        for (int i = 0; i < 7; i++) {
            width = singleWidth * i +20;
            switch (i) {
                case 0:
                    week = "周一";
                    break;
                case 1:
                    week = "周二";
                    break;
                case 2:
                    week = "周三";
                    break;
                case 3:
                    week = "周四";
                    break;
                case 4:
                    week = "周五";
                    break;
                case 5:
                    week = "周六";
                    break;
                case 6:
                    week = "周七";
                    break;
            }
            mPaint.setTextSize(35);
            mPaint.setColor(Color.rgb(41, 36, 33));
            mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            canvas.drawText(week, width, height, mPaint);

            mPaint.setTextSize(20);
            mPaint.setColor(Color.rgb(192,192,192));
            DateInfo dateInfo = dateInfos.get(i);
            canvas.drawText(dateInfo.getMonth()+"-"+dateInfo.getDay(),width+20,height+30,mPaint);
        }
    }
}
