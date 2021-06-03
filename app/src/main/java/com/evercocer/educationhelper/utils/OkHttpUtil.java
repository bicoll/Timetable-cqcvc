package com.evercocer.educationhelper.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 采用单例模式
 */
public class OkHttpUtil {
    private static OkHttpUtil okHttpUtil;
    private OkHttpClient okHttpClient;

    private OkHttpUtil() {
        okHttpClient = new OkHttpClient();
    }

    public static synchronized OkHttpUtil getInstance() {
        if (okHttpUtil == null) {
            okHttpUtil = new OkHttpUtil();
        }
        return okHttpUtil;
    }


    public void get(String url, CallBack callBack) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }


    public void post(Request request, OkHttpUtil.CallBack callBack) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }

    public interface CallBack {
        void onSuccess(Call call, Response response) throws IOException;

        void onFailed(IOException e);
    }
}
