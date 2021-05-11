package com.evercocer.educationhelper.ui.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

public class WeekPickLayout extends ViewGroup {
    private int hang;
    private int lie;

    public int getHang() {
        return hang;
    }

    public void setHang(int hang) {
        this.hang = hang;
    }

    public int getLie() {
        return lie;
    }

    public void setLie(int lie) {
        this.lie = lie;
    }

    public WeekPickLayout(Context context) {
        super(context);
    }

    public WeekPickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekPickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int measuredWidth = getMeasuredWidth();
        int minWidth = (measuredWidth - (lie + 1) * 10) / lie;

        int measuredHeight = getMeasuredHeight();
        int minHeight = (measuredHeight - (hang + 1) * 10) / hang;

        int left = 0;
        int top = 10;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            left += 10;
            child.layout(left, top, minWidth+left, minHeight+top);
            left += minWidth;
            if ((i + 1) % lie == 0) {
                left = 0;
                top += minHeight + 10;
            }
        }
    }
}
