package com.yunhaoguo.oingo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.activity.FriendRequestsActivity;
import com.yunhaoguo.oingo.activity.ProfileActivity;
import com.yunhaoguo.oingo.adapter.FriendListAdapter;
import com.yunhaoguo.oingo.entity.User;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.fragment
 * 文件名:     FriendsFragment
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/5 11:39 PM
 * 描述:      TODO
 */


public class FriendsFragment extends Fragment {

    private List<User> friendList = new ArrayList<>();

    private RecyclerView rvFriendList;

    private FriendListAdapter friendListAdapter;

    private TextView tvFriendRequests;

    private SwipeRefreshLayout srlFriendList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rvFriendList = view.findViewById(R.id.rv_friend_list);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        friendListAdapter = new FriendListAdapter(friendList);
        friendListAdapter.setOnItemClickListener(new FriendListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                User user = friendList.get(position);
                intent.putExtra("uid", user.getUid());
                startActivity(intent);
            }
        });
        rvFriendList.setAdapter(friendListAdapter);

        tvFriendRequests = view.findViewById(R.id.tv_friend_requests);
        tvFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FriendRequestsActivity.class));
            }
        });

        srlFriendList = view.findViewById(R.id.srl_friend_list);
        srlFriendList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                srlFriendList.setRefreshing(false);
            }
        });

    }

    private void initData() {
        int uid = getArguments().getInt("uid");
        if (uid != -1) {
            QueryUtils.getFriendList(uid, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject responseObj = new JSONObject(response.body().string());
                        Gson gson = new Gson();
                        friendList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<User>>() {
                        }.getType());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                friendListAdapter.updateData(friendList);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
