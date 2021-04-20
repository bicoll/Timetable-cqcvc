package com.evercocer.educationhelper.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evercocer.educationhelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button login;

    private static final String TAG = "LoginActivity";
    private String account_str;
    private String password_str;

    private SharedPreferences sharedPreferences;
    private OkHttpClient okHttpClient;
    private Intent intent;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                String data = (String) msg.obj;
                Log.d(TAG, data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String token = jsonObject.getString("token");
                    if (token == null) {
                        Toast.makeText(LoginActivity.this, "LoginActivity:Token is null!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sharedPreferences.edit()
                            .putBoolean("isLogined", true)
                            .putString("userName", account_str)
                            .putString("token", token)
                            .commit();
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(LoginActivity.this, MainActivity.class);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLogined", false)) {
            startActivity(intent);
            finish();
        }
        intView();

    }

    private void intView() {
        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okHttpClient = new OkHttpClient();

                account_str = userName.getText().toString();
                if (account_str.length() != 10) {
                    Toast.makeText(LoginActivity.this, "账号长度不对,请重新输入!", Toast.LENGTH_SHORT).show();
                    return;
                }
                password_str = passWord.getText().toString();
                password_str = password_str == null ? "1" : password_str;
                String url = "http://edu.cqcvc.com.cn:800/app/app.ashx?method=authUser";
                url += "&xh=" + account_str + "&pwd=" + password_str;
                FormBody formBody = new FormBody.Builder().add("", "").build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message message = Message.obtain();
                        message.obj = response.body().string();
                        message.what = 200;
                        handler.sendMessage(message);
                    }
                });

            }
        });
    }


}
