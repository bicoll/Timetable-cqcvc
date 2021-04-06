package com.evercocer.educationhelper.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.ui.layouts.CourseLayout;
import com.evercocer.educationhelper.ui.views.CourseView;
import com.evercocer.educationhelper.ui.views.WeekthView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private CourseLayout cl;
    private LinearLayout ll_date;
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
                        //添加WeekView
                        WeekthView weekthView = new WeekthView(MainActivity.this);
                        weekthView.setWeekTH(week);
                        weekthView.setmWidth(dip2px(MainActivity.this,45));
                        weekthView.setmHeight(dip2px(MainActivity.this,42));
                        ll_date.addView(weekthView,0);
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
                    break;
            }
        }
    };
    //okHttp客户端
    private OkHttpClient okHttpClient = new OkHttpClient();

    private void parseData(String responseBody) {
        try {
            JSONArray jsonArray = new JSONArray(responseBody);
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
                String dateInfo_str = jsonObject.getString("kcsj");
                int day = Integer.parseInt(dateInfo_str.substring(0, 1));
                int times = (dateInfo_str.length() / 2)+1;
                int[] chapters = new int[times];
                chapters[0] = day;
                StringBuffer stringBuffer = new StringBuffer(dateInfo_str.substring(1, dateInfo_str.length()));
                for (int j = 1; j < times; j++) {
                    int chapter = Integer.parseInt(stringBuffer.substring(0, 2));
                    chapters[j] = chapter;
                    stringBuffer.delete(0, 2);
                }
                courseView.setChapters(chapters);
                cl.addView(courseView);
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
        setContentView(R.layout.activity_main);
        initViews();
        getCurrentTime();
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
        cl = findViewById(R.id.cl);
        ll_date = findViewById(R.id.ll_date);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}