package com.yunhaoguo.oingo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.adapter.RequestsListAdapter;
import com.yunhaoguo.oingo.entity.User;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FriendRequestsActivity extends AppCompatActivity {

    private RecyclerView rvRequests;

    private List<User> requestsList = new ArrayList<>();

    private RequestsListAdapter requestsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        initView();
        initData();
    }

    private void initData() {
        QueryUtils.getRequestsList(AccountUtils.getUid(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    requestsList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<User>>() {
                    }.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestsListAdapter.updateData(requestsList);
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.requests_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvRequests = findViewById(R.id.rv_friend_requests);
        rvRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        requestsListAdapter = new RequestsListAdapter(requestsList);
        rvRequests.setAdapter(requestsListAdapter);
        requestsListAdapter.setOnItemClickListener(new RequestsListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, int accept) {
                QueryUtils.updateRequestsList(requestsList.get(position).getUid(), accept, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseObj = new JSONObject(response.body().string());
                            if (responseObj.getString("result").equals("SUCCESS")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FriendRequestsActivity.this, "operation success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FriendRequestsActivity.this, "operation failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                requestsList.remove(position);
                final List<User> tmp = new ArrayList<>();
                tmp.addAll(requestsList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestsListAdapter.updateData(tmp);
                    }
                });



            }
        });
    }
}
