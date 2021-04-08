package com.evercocer.educationhelper.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ChapterView extends View {

    private String[] chapterInfo;
    private Paint mPaint;

    public ChapterView(Context context) {
        this(context, null);
    }

    public ChapterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChapterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (chapterInfo == null)
            return;
        //初始化画笔
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        int singleHeight = getHeight() / chapterInfo.length;
        int width = getWidth();
        int baseY = 0;
        int baseX;
        for (int i = 0; i < chapterInfo.length; i++) {
            mPaint.setTextSize(40);
            mPaint.setColor(Color.rgb(41,36,33));
            mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            baseY = (i + 1) * singleHeight - singleHeight / 2;
            canvas.drawText(String.valueOf(i + 1), getWidth() / 3, baseY, mPaint);

            mPaint.setColor(Color.rgb(192,192,192));
            mPaint.setTextSize(20);
            baseX = 20;
            baseY += 30;
            canvas.drawText(chapterInfo[i],baseX,baseY,mPaint);
        }
    }

    public String[] getChapterInfo() {
        return chapterInfo;
    }

    public void setChapterInfo(String[] chapterInfo) {
        this.chapterInfo = chapterInfo;
    }

}
