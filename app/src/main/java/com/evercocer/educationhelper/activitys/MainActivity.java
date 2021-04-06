package com.evercocer.educationhelper.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.ui.CourseLayout;
import com.evercocer.educationhelper.ui.CourseView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<CourseView> courseViews;
    private CourseLayout cl_day1;
    private CourseLayout cl_day2;
    private CourseLayout cl_day3;
    private CourseLayout cl_day4;
    private CourseLayout cl_day5;
    private CourseLayout cl_day6;
    private CourseLayout cl_day7;
    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String dateBody = (String) msg.obj;
                    try {
                        //解析时间
                        JSONObject jsonObject = new JSONObject(dateBody);
                        String week = jsonObject.getString("zc");
                        String startDay =jsonObject.getString("s_time");
                        String endDay =jsonObject.getString("e_time");
                        //网络请求课表数据
                        initData(week);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 200:
                    String timetableBody = (String) msg.obj;
                    //解析JSON数据
                    parseData(timetableBody);
                    //加载CourseView
                    initCourseView();
                    break;
            }
        }
    };
    //okHttp客户端
    private OkHttpClient okHttpClient = new OkHttpClient();


    private void initCourseView() {
        for (CourseView courseView : courseViews) {
            switch (courseView.getDay()) {
                case 1:
                    cl_day1.addView(courseView);
                    break;
                case 2:
                    cl_day2.addView(courseView);
                    break;
                case 3:
                    cl_day3.addView(courseView);
                    break;
                case 4:
                    cl_day4.addView(courseView);
                    break;
                case 5:
                    cl_day5.addView(courseView);
                    break;
                case 6:
                    cl_day6.addView(courseView);
                    break;
                case 7:
                    cl_day7.addView(courseView);
                    break;
            }
        }
    }

    private void parseData(String responseBody) {
        try {
            JSONArray jsonArray = new JSONArray(responseBody);
            courseViews = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                CourseView courseView = new CourseView(this);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //课程名称
                String courseName = jsonObject.getString("kcmc");
                courseView.setCourseName(courseName);
                Log.d(TAG, courseName);
                //授课老师
                String courseTeacher = "【" + jsonObject.getString("jsxm") + "】";
                courseView.setCourseTeacher(courseTeacher);
                //授课教室
                String courseRoom = jsonObject.getString("jsmc");
                courseView.setCourseRoom(courseRoom);
                //时间信息
                String dataInfo = jsonObject.getString("kcsj");
                int day = Integer.parseInt(dataInfo.substring(0, 1));
                courseView.setDay(day);
                StringBuffer stringBuffer = new StringBuffer(dataInfo.substring(1, dataInfo.length()));
                int times = (dataInfo.length() / 2);
                int[] chapters = new int[times];
                for (int j = 0; j < times; j++) {
                    int chapter = Integer.parseInt(stringBuffer.substring(0, 2));
                    chapters[j] = chapter;
                    stringBuffer.delete(0, 2);
                }
                courseView.setDay(day);
                courseView.setChapters(chapters);
                courseViews.add(courseView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentTime() {
        //获取系统时间
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String currentTime = mYear + "-" + mMonth + "-" + mDay;

        //POST提交
        String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getCurrentTime&currDate=" + currentTime;
        FormBody formBody = new FormBody.Builder().add("", "").build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("token","BABBFA7442D4F967E7017E2578E413BC")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "时间获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseBody = response.body().string();
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = responseBody;
                    mHandler.sendMessage(message);
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentTime();
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initData(String week) {
        String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getKbcxAzc&xh=2040403192&xnxqid=2020-2021-2&zc="+week;
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
                    message.what = 200;
                    mHandler.sendMessage(message);
                } else Log.d(TAG, "失败");
            }
        });
    }

    private void initViews() {
        cl_day1 = findViewById(R.id.cl_day1);
        cl_day2 = findViewById(R.id.cl_day2);
        cl_day3 = findViewById(R.id.cl_day3);
        cl_day4 = findViewById(R.id.cl_day4);
        cl_day5 = findViewById(R.id.cl_day5);
        cl_day6 = findViewById(R.id.cl_day6);
        cl_day7 = findViewById(R.id.cl_day7);
    }
}