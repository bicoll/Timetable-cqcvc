package com.evercocer.educationhelper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evercocer.educationhelper.R;
import com.evercocer.educationhelper.adapters.RoomAdapter;
import com.evercocer.educationhelper.model.Room;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {

    private ListView listView;
    private SearchViewModel searchViewModel;
    private static final String BASE_URL = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=getKxJscx&idleTime=allday&time=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.RoomList);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        MutableLiveData<String> json = searchViewModel.getJson();
        json.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //解析json
                searchViewModel.parseData(s);
                RoomAdapter roomAdapter = new RoomAdapter(searchViewModel.getRooms(), getActivity());
                listView.setAdapter(roomAdapter);
            }
        });


        if (searchViewModel.getRooms().isEmpty()) {
            String token = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("token", "null");
            searchViewModel.loadClassroom(BASE_URL + searchViewModel.getCurrent(), token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    json.postValue(response.body().string());
                }
            });
        }
    }
}
