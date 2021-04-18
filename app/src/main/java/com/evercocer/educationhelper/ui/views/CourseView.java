package com.evercocer.educationhelper.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CourseView extends View {
    private Paint mPaint;
    private TextPaint textPaint;
    private String courseName;
    private String courseTeacher;
    private String courseRoom;
    private int[] chapters;

    public int[] getRgbColor() {
        return rgbColor;
    }

    public void setRgbColor(int[] rgbColor) {
        this.rgbColor = rgbColor;
    }

    private int[] rgbColor;

    public CourseView(Context context) {
        this(context, null);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //画圆角矩形背景
        RectF rectF = new RectF(1.25f, 5, width - 1.25f, height - 5);
        //mPaint.setARGB(0,131,175,155);
        mPaint.setColor(Color.rgb(rgbColor[0], rgbColor[1], rgbColor[2]));
        canvas.drawRoundRect(rectF, 20, 20, mPaint);

        //画第一个文本(课程以及授课教师)
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(28);
        String info = courseName + courseTeacher;
        StaticLayout firstPartLayout = StaticLayout.Builder.obtain(info, 0, info.length(), textPaint, width - 20).build();
        canvas.translate(15, 30);
        firstPartLayout.draw(canvas);

        //画第二个文本(授课地点)
        //计算第二个文本的高度
        int firstPartHeight = firstPartLayout.getHeight();
        int secondPartHeight = ((height - firstPartHeight) / 2)+firstPartHeight;
        /*mPaint.setColor(Color.RED);
        canvas.drawLine(0,firstPartHeight,width,firstPartHeight,mPaint);*/
        StaticLayout secondPartLayout = StaticLayout.Builder.obtain(courseRoom, 0, courseRoom.length(), textPaint, width - 10).build();
        canvas.translate(0, secondPartHeight-30);
        secondPartLayout.draw(canvas);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public int[] getChapters() {
        return chapters;
    }

    public void setChapters(int[] chapters) {
        this.chapters = chapters;
    }
}
