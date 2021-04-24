package com.evercocer.educationhelper.activitys;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> account;
    private MutableLiveData<String> password;
    private MutableLiveData<Boolean> logined;

    private OkHttpClient okHttpClient;

    public void loadToken(String url,Callback callback){
        //okHttpClient判空
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient();
        //构建表单
        FormBody formBody = new FormBody.Builder().add("", "").build();
        //POST请求
        Request request = new Request.Builder().url(url).post(formBody).build();
        //构建call
        Call call = okHttpClient.newCall(request);
        //发起异步请求
        call.enqueue(callback);
    }

    public MutableLiveData<String> getAccount() {
        if (account == null) {
            account = new MutableLiveData<>();
            account.setValue("null");
        }
        return account;
    }

    public void setAccount(MutableLiveData<String> account) {
        this.account = account;
    }

    public MutableLiveData<String> getPassword() {
        if (password == null) {
            password = new MutableLiveData<>();
            password.setValue("null");
        }
        return password;
    }

    public void setPassword(MutableLiveData<String> password) {
        this.password = password;
    }

    public MutableLiveData<Boolean> getLogined() {
        if (logined == null) {
            logined = new MutableLiveData<>();
            logined.setValue(false);
        }
        return logined;
    }

    public void setLogined(MutableLiveData<Boolean> logined) {
        this.logined = logined;
    }
}
