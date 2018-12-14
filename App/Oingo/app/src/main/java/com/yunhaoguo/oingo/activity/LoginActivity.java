package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunhaoguo.oingo.MainActivity;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.User;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRegister;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRememberPass;
    private TextView tvForgetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        etUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        cbRememberPass = findViewById(R.id.cb_remember_pass);
        tvForgetPass = findViewById(R.id.tv_forget_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                Log.i("name", name);
                Log.i("password", password);
                verify(name, password);
                break;
            case R.id.tv_forget_password:
                break;
        }
    }

    private void verify(String name, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            return;
        }

        QueryUtils.verify(name, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    User user = gson.fromJson(responseObj.getString("result"), User.class);
                    if (user.getUid() != 0) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("uid", user.getUid());
                        intent.putExtra("uname", user.getUname());
                        intent.putExtra("uemail", user.getUemail());
                        intent.putExtra("ustate", user.getUstate());
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
