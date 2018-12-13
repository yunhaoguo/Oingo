package com.yunhaoguo.oingo.fragment;

import android.content.Intent;
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
import com.yunhaoguo.oingo.activity.NoteDetailActivity;
import com.yunhaoguo.oingo.adapter.NoteListAdapter;
import com.yunhaoguo.oingo.entity.Note;
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
 * 文件名:     NotesFragment
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/5 11:28 PM
 * 描述:      TODO
 */


public class NotesFragment extends Fragment {

    private List<Note> noteList = new ArrayList<>();

    private NoteListAdapter noteListAdapter;

    private RecyclerView rvNoteList;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, null);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        QueryUtils.getNotesList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    noteList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<Note>>() {
                    }.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noteListAdapter.updateData(noteList);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView(View view) {
        rvNoteList = view.findViewById(R.id.rv_note_list);
        rvNoteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        noteListAdapter = new NoteListAdapter(noteList);
        rvNoteList.setAdapter(noteListAdapter);
        noteListAdapter.setOnItemClickListener(new NoteListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                Note note = noteList.get(position);
                intent.putExtra("nid", note.getNid());
                intent.putExtra("ncontent", note.getNcontent());
                intent.putExtra("nstarttime", note.getStartTime());
                intent.putExtra("nuname", note.getUname());
                startActivity(intent);
            }
        });

    }
}
