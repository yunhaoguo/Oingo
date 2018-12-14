package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.yunhaoguo.oingo.utils.ShareUtils;

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
        initData();
    }

    private void initData() {
        if (ShareUtils.getBoolean(this, "remember_pass", false)) {
            cbRememberPass.setChecked(true);
            etUsername.setText(ShareUtils.getString(this, "name", ""));
            etPassword.setText(ShareUtils.getString(this, "password", ""));
        }
    }

    private void initView() {


        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        etUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        cbRememberPass = findViewById(R.id.cb_remember_pass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
            case R.id.btn_login:
                String name = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                verify(name, password);
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
                        checkRememberPass();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        ShareUtils.putInt(LoginActivity.this, "uid", user.getUid());
                        ShareUtils.putString(LoginActivity.this, "uname", user.getUname());
                        ShareUtils.putString(LoginActivity.this, "uemail", user.getUemail());
                        ShareUtils.putString(LoginActivity.this, "ustate", user.getUstate());
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

    private void checkRememberPass() {
        if (cbRememberPass.isChecked()) {
            ShareUtils.putBoolean(this, "remember_pass", true);
            ShareUtils.putString(this, "name", etUsername.getText().toString().trim());
            ShareUtils.putString(this, "password", etPassword.getText().toString().trim());
        } else {
            ShareUtils.putBoolean(this, "remember_pass", false);
            ShareUtils.delete(this, "name");
            ShareUtils.delete(this, "password");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            etUsername.setText(data.getExtras().getString("name"));
            etPassword.setText(data.getExtras().getString("password"));
        }
    }
}
