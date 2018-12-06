package com.yunhaoguo.oingo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
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

    private List<String> friendNameList = new ArrayList<>();

    private RecyclerView rvFriendList;

    private FriendListAdapter friendListAdapter;

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
        friendNameList.add("a");
        friendListAdapter = new FriendListAdapter(friendNameList);
        rvFriendList.setAdapter(friendListAdapter);
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
                        List<User> friendList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<User>>() {
                        }.getType());
                        List<String> tmp = new ArrayList<>();
                        for (User user: friendList) {
                            tmp.add(user.getUname());
                        }
                        friendListAdapter.updateData(tmp);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
