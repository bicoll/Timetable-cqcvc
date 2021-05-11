package com.evercocer.educationhelper.ui.week;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.evercocer.educationhelper.R;

import java.util.ArrayList;

public class ItemPickView extends View  {
    private Context context;
    private int week;
    private Paint paint;
    private View rootView;
    private boolean selected;
    private boolean passed;

    private int[] color;

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public ItemPickView(Context context) {
        this(context, null);
    }

    public ItemPickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemPickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        RectF rectF = new RectF(0, 0, width, height);
        paint.setTextSize(40);
        if (passed) {
            paint.setColor(Color.rgb(220, 220, 220));
            canvas.drawRoundRect(rectF, 20, 20, paint);
            paint.setColor(Color.rgb(245, 245, 245));
            canvas.drawText(String.valueOf(week), width / 3, height / 3 * 2, paint);
            return;
        }
        paint.setColor(Color.rgb(color[0], color[1], color[2]));
        canvas.drawRoundRect(rectF, 20, 20, paint);
        paint.setColor(Color.rgb(245, 245, 245));
        canvas.drawText(String.valueOf(week), width / 3, height / 3 * 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                clickListener.actionUP(String.valueOf(week));
                break;
        }

        return true;

    }

    private ClickListener clickListener;

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void actionUP(String week);
    }


}
