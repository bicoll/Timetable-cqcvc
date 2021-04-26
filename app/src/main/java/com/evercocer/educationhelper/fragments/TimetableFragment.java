package com.evercocer.educationhelper.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.model.CourseInfo;
import com.evercocer.educationhelper.ui.layouts.CourseLayout;
import com.evercocer.educationhelper.ui.views.ChapterView;
import com.evercocer.educationhelper.ui.views.CourseView;
import com.evercocer.educationhelper.ui.views.DateInfoView;
import com.evercocer.educationhelper.ui.views.WeekthView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TimetableFragment extends Fragment {
    private CourseLayout cl_courseLayout;
    private WeekthView wv_week;
    private ChapterView cv_chapterInfo;
    private static final String TAG = "MainActivity";
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = getActivity();
        //初始化ViewModel
        viewModel = new ViewModelProvider(getActivity()).get(TimetableViewModel.class);
        //为ChapterView绑定ui数据
        cv_chapterInfo.setChapterInfo(viewModel.getChapterDateInfo().getValue());
        //为WeekthView绑定数据
        wv_week.setWeekTH(viewModel.getWeekTh().getValue());
        //为DateInfoView绑定数据
        dateInfoView.setDateInfos(viewModel.getDateInfo().getValue());
        //为viewModel的json添加监听:数据变化就解析json数据,更新ui
        MutableLiveData<String> json = viewModel.getJson();
        json.observe(fragmentActivity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ArrayList<CourseInfo> courseInfos = viewModel.getCourseInfos();
                if (courseInfos.isEmpty())
                    courseInfos = viewModel.parseCourseInfo(s);
                for (int i = 0; i < courseInfos.size(); i++) {
                    CourseView courseView = new CourseView(fragmentActivity);
                    courseView.setRgbColor(viewModel.getRandomColor());
                    courseView.setCourseInfo(courseInfos.get(i));
                    cl_courseLayout.addView(courseView);
                }
            }
        });
        //网络加载课表数据
        if (viewModel.getCourseInfos().size() == 0) {
            Log.d(TAG, "网络请求");
            //初始化SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String account_str = sharedPreferences.getString("account", null);
            String token_str = sharedPreferences.getString("token", null);
            if (token_str == null) {
                Toast.makeText(getActivity(), "MainActivity:Token is null!", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getKbcxAzc&xh=" + account_str + "&xnxqid=2020-2021-2&zc=" + viewModel.getWeekTh().getValue();
            viewModel.loadTimeInfo(url, account_str, token_str, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //更新json数据
                    json.postValue(response.body().string());
                }
            });
        }
    }

}
