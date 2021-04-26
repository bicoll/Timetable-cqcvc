package com.evercocer.educationhelper.fragments;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.evercocer.educationhelper.model.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<String> json;
    private ArrayList<Room> rooms;
    private OkHttpClient okHttpClient;
    private Calendar calendar;


    public ArrayList<Room> getRooms() {
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public MutableLiveData<String> getJson() {
        if (json == null) {
            json = new MutableLiveData<>();
        }
        return json;
    }

    public void setJson(MutableLiveData<String> json) {
        this.json = json;
    }

    public void loadClassroom(String url,String token, Callback callback) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        Log.d(TAG, "loadClassroom: 网络请求");
        FormBody formBody = new FormBody.Builder().build();

        Request request = new Request.Builder().addHeader("token",token).url(url).post(formBody).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(callback);
    }

    public String getCurrent() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder stringBuilder = new StringBuilder();
        String current = stringBuilder.append(year).append("-").append(month).append("-").append(day).toString();
        return current;
    }

    public void parseData(String data){
        try {
            JSONArray jsonList = new JSONArray(data);
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject campus = (JSONObject) jsonList.get(i);
                JSONArray jsList = campus.getJSONArray("jsList");
                for (int j = 0; j < jsList.length(); j++) {
                    Room room = new Room();
                    JSONObject room_json = (JSONObject) jsList.get(j);
                    //教室
                    room.setClassRoom(room_json.getString("jsmc"));
                    //容量
                    room.setVolume(room_json.getString("zws"));
                    //校区
                    room.setCampus(room_json.getString("xqmc"));
                    //教室编号
                    room.setNumber(room_json.getString("jsh"));
                    rooms.add(room);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
