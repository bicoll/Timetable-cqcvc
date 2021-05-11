package com.evercocer.educationhelper.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

public class TimetableFragment extends Fragment {
    private CourseLayout cl_courseLayout;
    private WeekthView wv_week;
    private ChapterView cv_chapterInfo;
    private DateInfoView dateInfoView;
    private TimetableViewModel viewModel;
    private FragmentActivity fragmentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        cl_courseLayout = view.findViewById(R.id.cl_main_courseLayout);
        wv_week = view.findViewById(R.id.wv_main_weekth);
        cv_chapterInfo = view.findViewById(R.id.cv_main_chapterInfo);
        dateInfoView = view.findViewById(R.id.div_main_dateInfo);
        fragmentActivity = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化ViewModel
        viewModel = new ViewModelProvider(fragmentActivity).get(TimetableViewModel.class);
        //获取ui数据
        MutableLiveData<String[]> chapterDateInfo = viewModel.getChapterDateInfo();
        MutableLiveData<String> weekTh = viewModel.getWeekTh();
        MutableLiveData<String> json = viewModel.getJson();
        MutableLiveData<ArrayList<DateInfo>> dateInfo = viewModel.getDateInfo();


        //为ChapterView绑定ui数据
        cv_chapterInfo.setChapterInfo(chapterDateInfo.getValue());

        //添加监听事件
        if (weekTh.hasObservers() == false) {
            weekTh.observe(fragmentActivity, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    System.out.println("week改变:"+s);
                    //初始化SharedPreferences的账号数据
                    SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String account_str = sharedPreferences.getString("account", null);
                    String token_str = sharedPreferences.getString("token", null);
                    //判空
                    if (token_str == null) {
                        Toast.makeText(fragmentActivity, "MainActivity:Token is null!", Toast.LENGTH_SHORT).show();
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
            dateInfo.observe(fragmentActivity, new Observer<ArrayList<DateInfo>>() {
                @Override
                public void onChanged(ArrayList<DateInfo> dateInfos) {
                    dateInfoView.setDateInfos(dateInfos);
                    dateInfoView.invalidate();
                }
            });
        }
        json.observe(TimetableFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println("json改变");
                cl_courseLayout.removeAllViews();
                ArrayList<CourseInfo> courseInfos = viewModel.getCourseInfos();
                if (courseInfos.isEmpty())
                    viewModel.parseCourseInfo(s);
                for (int i = 0; i < courseInfos.size(); i++) {
                    CourseView courseView = new CourseView(fragmentActivity);
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
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                WeekPickerDialog weekPickerDialog = new WeekPickerDialog.Builder(fragmentActivity)
                        .setCurrentWeek(viewModel.getCurrentWeek())
                        .setContentView(R.layout.dialog_weekpicker)
                        .setSize(0, displayMetrics.heightPixels / 3)
                        .setLocation(Gravity.BOTTOM)
                        .setListener(new WeekPickerDialog.Listener() {
                            @Override
                            public void check(String week ){
                                if (week == weekTh.getValue())
                                    return;
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

}
