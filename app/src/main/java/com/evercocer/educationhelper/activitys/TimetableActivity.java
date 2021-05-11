package com.evercocer.educationhelper.activitys;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.dialog.WeekPickerDialog;
import com.evercocer.educationhelper.model.CourseInfo;
import com.evercocer.educationhelper.model.DateInfo;
import com.evercocer.educationhelper.ui.layouts.CourseLayout;
import com.evercocer.educationhelper.ui.views.ChapterView;
import com.evercocer.educationhelper.ui.views.CourseView;
import com.evercocer.educationhelper.ui.views.DateInfoView;
import com.evercocer.educationhelper.ui.views.WeekthView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TimetableActivity extends AppCompatActivity {
    private CourseLayout cl_courseLayout;
    private WeekthView wv_week;
    private ChapterView cv_chapterInfo;
    private DateInfoView dateInfoView;
    private TimetableViewModel viewModel;
    private static final String TAG = "TimetableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intViews();
        load();
    }

    private void load() {
        //初始化ViewModel
        viewModel = new ViewModelProvider(this).get(TimetableViewModel.class);
        //获取ui数据
        MutableLiveData<String[]> chapterDateInfo = viewModel.getChapterDateInfo();
        MutableLiveData<String> weekTh = viewModel.getWeekTh();
        MutableLiveData<String> json = viewModel.getJson();
        MutableLiveData<ArrayList<DateInfo>> dateInfo = viewModel.getDateInfo();


        //为ChapterView绑定ui数据
        cv_chapterInfo.setChapterInfo(chapterDateInfo.getValue());

        //添加监听事件
        if (weekTh.hasObservers() == false) {
            weekTh.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    System.out.println("week改变:"+s);
                    //初始化SharedPreferences的账号数据
                    SharedPreferences sharedPreferences = TimetableActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String account_str = sharedPreferences.getString("account", null);
                    String token_str = sharedPreferences.getString("token", null);
                    //判空
                    if (token_str == null) {
                        Toast.makeText(TimetableActivity.this, "TimetableActivity:Token is null!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //URL
                    String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getKbcxAzc&xh=" + account_str + "&xnxqid=2020-2021-2&zc=" + weekTh.getValue();
                    //POST请求
                    viewModel.loadTimeInfo(url, account_str, token_str, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            //更新json数据
                            System.out.println("网络请求");
                            if (response.body().contentLength() == 0) {
                                System.out.println("未返回内容");
                                return;
                            }
                            String string = response.body().string();
                            System.out.println(string);
                            json.postValue(string);
                            viewModel.getCourseInfos().clear();
                        }
                    });
                }
            });
        }
        if (dateInfo.hasObservers() == false) {
            dateInfo.observe(this, new Observer<ArrayList<DateInfo>>() {
                @Override
                public void onChanged(ArrayList<DateInfo> dateInfos) {
                    dateInfoView.setDateInfos(dateInfos);
                    dateInfoView.invalidate();
                }
            });
        }
        json.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println("json改变");
                cl_courseLayout.removeAllViews();
                ArrayList<CourseInfo> courseInfos = viewModel.getCourseInfos();
                if (courseInfos.isEmpty())
                    viewModel.parseCourseInfo(s);
                for (int i = 0; i < courseInfos.size(); i++) {
                    CourseView courseView = new CourseView(TimetableActivity.this);
                    courseView.setCourseInfo(courseInfos.get(i));
                    cl_courseLayout.addView(courseView);
                }
                wv_week.setWeekTH(weekTh.getValue());
                wv_week.invalidate();
            }
        });


        wv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(viewModel.getCurrentWeek());
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                WeekPickerDialog weekPickerDialog = new WeekPickerDialog.Builder(TimetableActivity.this)
                        .setCurrentWeek(viewModel.getCurrentWeek())
                        .setContentView(R.layout.dialog_weekpicker)
                        .setSize(0, displayMetrics.heightPixels / 3)
                        .setLocation(Gravity.BOTTOM)
                        .setListener(new WeekPickerDialog.Listener() {
                            @Override
                            public void check(String week ){
                                int oldWeek = Integer.parseInt(weekTh.getValue());
                                int newWeek = Integer.parseInt(week);
                                if (newWeek ==oldWeek)
                                    return;
                                viewModel.flushDateInfo(oldWeek,newWeek);
                                weekTh.postValue(week);
                            }
                        })
                        .build();

                weekPickerDialog.show();
            }
        });

        //为DateInfoView绑定数据
        dateInfoView.setDateInfos(dateInfo.getValue());

    }


    private void intViews() {
        cl_courseLayout = findViewById(R.id.cl_main_courseLayout);
        wv_week =findViewById(R.id.wv_main_weekth);
        cv_chapterInfo = findViewById(R.id.cv_main_chapterInfo);
        dateInfoView = findViewById(R.id.div_main_dateInfo);
    }

}