package com.yunhaoguo.oingo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.User;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvEditProfile;
    private EditText etName;
    private EditText etEmail;
    private EditText etState;
    private ImageView ivAddFriend;
    private TextView tvSave;

    private int profileUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        initData();
    }

    private void initData() {
        QueryUtils.getUserInfo(profileUid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    User user = gson.fromJson(responseObj.getString("result"), User.class);
                    if (user != null) {
                        etName.setText(user.getUname());
                        etEmail.setText(user.getUemail());
                        etState.setText(user.getUstate());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        tvEditProfile = findViewById(R.id.tv_edit_profile);
        etName = findViewById(R.id.et_me_username);
        etEmail = findViewById(R.id.et_me_email);
        etState = findViewById(R.id.et_me_state);
        ivAddFriend = findViewById(R.id.iv_add_friend);
        ivAddFriend.setOnClickListener(this);
        tvSave = findViewById(R.id.tv_save_profile);
        etName.setEnabled(false);
        etState.setEnabled(false);
        etEmail.setEnabled(false);
        tvEditProfile.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        profileUid = getIntent().getIntExtra("uid", -1);
        if (profileUid == AccountUtils.getUid()) {
            ivAddFriend.setVisibility(View.GONE);
            tvEditProfile.setVisibility(View.VISIBLE);

        } else {
            tvEditProfile.setVisibility(View.GONE);
            ivAddFriend.setVisibility(View.VISIBLE);
            checkFriendOrHasRequested(AccountUtils.getUid());
        }

    }

    private void checkFriendOrHasRequested(int uid) {
        QueryUtils.getFriendList(uid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    List<User> friendList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<User>>() {
                    }.getType());
                    Set<Integer> fuids = new HashSet<>();
                    for (User u : friendList) {
                        fuids.add(u.getUid());
                    }
                    if (fuids.contains(profileUid)) {
                        ivAddFriend.setVisibility(View.GONE);
                    } else {
                        ivAddFriend.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_profile:
                etName.setEnabled(true);
                etState.setEnabled(true);
                etEmail.setEnabled(true);
                tvEditProfile.setVisibility(View.GONE);
                tvSave.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_save_profile:
                etName.setEnabled(false);
                etState.setEnabled(false);
                etEmail.setEnabled(false);
                if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etEmail.getText().toString())
                        && !TextUtils.isEmpty(etState.getText().toString())) {
                    QueryUtils.editInfo(etName.getText().toString(), etEmail.getText().toString(), etState.getText().toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                JSONObject responseObj = new JSONObject(response.body().string());
                                if (responseObj.getInt("result") == 1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "save info success", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "save info failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Info can not be empty", Toast.LENGTH_SHORT).show();
                }

                tvEditProfile.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                break;
            case R.id.iv_add_friend:
                ivAddFriend.setVisibility(View.GONE);
                QueryUtils.addFriend(profileUid, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseObj = new JSONObject(response.body().string());
                            int result = responseObj.getInt("result");
                            if (result == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "request success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "you have already send request", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
        }
    }
}
