package com.evercocer.educationhelper.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class CourseLayout extends LinearLayout {
    public CourseLayout(Context context) {
        this(context,null);
    }

    public CourseLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CourseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //CourseLayout的宽度
        int width = getMeasuredWidth();
        int singleHeight = getMeasuredHeight()/10;
        //子View顶边到CourseLayout顶边的距离
        int top = 0;
        int bottom = 0;
        for (int i = 0; i <getChildCount(); i++) {
            CourseView courseView = (CourseView) getChildAt(i);
            int[] chapters = courseView.getChapters();
            top = (chapters[0] - 1)*singleHeight;
            bottom = top+singleHeight*chapters.length;
            courseView.layout(0,top,width,bottom);
        }
    }
}
