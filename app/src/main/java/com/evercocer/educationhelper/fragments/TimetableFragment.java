package com.evercocer.educationhelper.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.other.DateInfo;
import com.evercocer.educationhelper.ui.layouts.CourseLayout;
import com.evercocer.educationhelper.ui.views.ChapterView;
import com.evercocer.educationhelper.ui.views.CourseView;
import com.evercocer.educationhelper.ui.views.DateInfoView;
import com.evercocer.educationhelper.ui.views.WeekthView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TimetableFragment extends Fragment {
    private Activity activity;
    private CourseLayout cl_courseLayout;
    private RelativeLayout rl_main;
    private WeekthView wv_week;
    private ChapterView cv_chapterInfo;
    private ArrayList<DateInfo> dateInfos;
    private static final String TAG = "MainActivity";
    private DateInfoView dateInfoView;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        initViews(view);
        loadChapterInfo();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadTimeInfo();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    String timetableBody = (String) msg.obj;
                    //解析课表数据信息,绑定CourseView
                    parseTimetableInfo(timetableBody);
                    break;
            }
        }
    };

    //初始化View
    public void initViews(View view) {
        cl_courseLayout = view.findViewById(R.id.cl_main_courseLayout);
        rl_main = view.findViewById(R.id.rl_main_container);
        wv_week = view.findViewById(R.id.wv_main_weekth);
        cv_chapterInfo = view.findViewById(R.id.cv_main_chapterInfo);
        dateInfoView = view.findViewById(R.id.div_main_dateInfo);
    }

    //加载章节信息
    private void loadChapterInfo() {
        String[] data = {"08:45", "09:40", "10:35", "11:30", "14:55", "15:50", "16:45", "17:40", "19:30", "20:25"};
        cv_chapterInfo.setChapterInfo(data);
        cv_chapterInfo.invalidate();
    }

    //加载时间信息
    public void loadTimeInfo() {
        Calendar mCalendar = Calendar.getInstance();
        Date time = mCalendar.getTime();
        //设置一周的第一天为星期一
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        //将日期定位到开学第一天
        mCalendar.set(2021,1,28);
        //获取开学时日期这一年的是第几周
        int weekBegin = mCalendar.get(Calendar.WEEK_OF_YEAR);

        //将日期定位到当前周星期一
        mCalendar.setTime(time);
        mCalendar.set(Calendar.DAY_OF_WEEK,2);
        //获取当前周是这一年的第几周
        int weekNow = mCalendar.get(Calendar.WEEK_OF_YEAR);
        //获取当前周是这学期的第几周
        String weekTh = String.valueOf(weekNow - weekBegin);
        //为WeekthView绑定数据
        wv_week.setWeekTH(weekTh);
        //为DateInfoView绑定数据
        dateInfos = new ArrayList<>();
        int beginMonth = mCalendar.get(Calendar.MONTH)+1;
        int beginDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        dateInfos.add(new DateInfo(beginMonth, beginDay));
        for (int i = 0; i < 6; i++) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            int month = mCalendar.get(Calendar.MONTH)+1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            dateInfos.add(new DateInfo(month, day));
        }
        dateInfoView.setDateInfos(dateInfos);
        //网络获取课表信息(Post)
        loadTimetableInfo(weekTh);
    }

    //解析课表信息并刷新CourseView
    private void parseTimetableInfo(String responseBody) {
        try {
            JSONArray jsonArray = new JSONArray(responseBody);
            //加载颜色
            ArrayList<int[]> colors = new ArrayList<>();
            int[] color1 = {25, 202, 173};
            int[] color2 = {236, 173, 158};
            int[] color3 = {190, 231, 233};
            int[] color4 = {214, 213, 183};
            int[] color5 = {244, 96, 108};
            int[] color6 = {209, 186, 116};
            int[] color7 = {190, 237, 199};
            int[] color8 = {230, 206, 172};
            int[] color9 = {140, 199, 181};
            int[] color10 = {160, 238, 225};

            colors.add(color1);
            colors.add(color2);
            colors.add(color3);
            colors.add(color4);
            colors.add(color5);
            colors.add(color6);
            colors.add(color7);
            colors.add(color8);
            colors.add(color9);
            colors.add(color10);

            for (int i = 0; i < jsonArray.length(); i++) {
                CourseView courseView = new CourseView(activity);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //课程名称
                String courseName = jsonObject.getString("kcmc");
                courseView.setCourseName(courseName);
                Log.d(TAG, courseName);
                //授课老师
                String courseTeacher = "@" + jsonObject.getString("jsxm") ;
                courseView.setCourseTeacher(courseTeacher);
                //授课教室
                String courseRoom = jsonObject.getString("jsmc");
                courseView.setCourseRoom(courseRoom);
                //时间信息
                String dateInfo_str = jsonObject.getString("kcsj");
                int day = Integer.parseInt(dateInfo_str.substring(0, 1));
                int times = (dateInfo_str.length() / 2) + 1;
                int[] chapters = new int[times];
                chapters[0] = day;
                StringBuffer stringBuffer = new StringBuffer(dateInfo_str.substring(1, dateInfo_str.length()));
                for (int j = 1; j < times; j++) {
                    int chapter = Integer.parseInt(stringBuffer.substring(0, 2));
                    chapters[j] = chapter;
                    stringBuffer.delete(0, 2);
                }
                courseView.setChapters(chapters);

                int[] color = {123, 43, 56};
                Random random = new Random();
                switch (random.nextInt(10)) {
                    case 0:
                        color = colors.get(0);
                        break;
                    case 1:
                        color = colors.get(1);
                        break;
                    case 2:
                        color = colors.get(2);
                        break;
                    case 3:
                        color = colors.get(3);
                        break;
                    case 4:
                        color = colors.get(4);
                        break;
                    case 5:
                        color = colors.get(5);
                        break;
                    case 6:
                        color = colors.get(6);
                        break;
                    case 7:
                        color = colors.get(7);
                        break;
                    case 8:
                        color = colors.get(8);
                        break;
                    case 9:
                        color = colors.get(9);
                        break;

                }
                courseView.setRgbColor(color);
                cl_courseLayout.addView(courseView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //加载课程表信息
    private void loadTimetableInfo(String week) {
        String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getKbcxAzc&xh=2040403192&xnxqid=2020-2021-2&zc=" + week;
        FormBody formBody = new FormBody.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("token", "BABBFA7442D4F967E7017E2578E413BC")
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Message message = Message.obtain();
                    message.obj = responseBody;
                    message.what = 2;
                    mHandler.sendMessage(message);
                } else Log.d(TAG, "失败");
            }
        });
    }

}
