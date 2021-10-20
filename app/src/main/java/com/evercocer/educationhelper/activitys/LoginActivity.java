package com.evercocer.educationhelper.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.evercocer.educationhelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private Button login;

    private SharedPreferences sharedPreferences;
    private Intent intent;
    private LoginViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //如果已经登录了就跳转
        if (sharedPreferences.getBoolean("logined", false)) {
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        //获取View的引用
        userName = findViewById(R.id.username);
        login = findViewById(R.id.login);
        //初始化ViewModel
        model = new ViewModelProvider(LoginActivity.this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        //初始化Intent
        intent = new Intent(LoginActivity.this, TimetableActivity.class);
        //初始化SharedPreferences
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        //初始化按钮监听事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取ViewModel中的账号、密码
                MutableLiveData<String> account = model.getAccount();
                MutableLiveData<String> password = model.getPassword();
                //更新数据
                account.setValue(userName.getText().toString());
                //加载用户Token
                String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=authUser";
                url += "&xh=" + account.getValue() /*+ "&pwd=" + password.getValue()*/;
                model.loadToken(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            String token = json.getString("token");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("account", account.getValue())
                                    .putString("password", password.getValue())
                                    .putBoolean("logined",true)
                                    .putString("token",token)
                                    .commit();

                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }


}
