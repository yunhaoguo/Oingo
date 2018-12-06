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

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.adapter.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;

/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.fragment
 * 文件名:     NotesFragment
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/5 11:28 PM
 * 描述:      TODO
 */


public class NotesFragment extends Fragment {

    private List<String> noteList;

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
        noteList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            noteList.add("item" + i);
        }
    }

    private void initView(View view) {
        rvNoteList = view.findViewById(R.id.rv_note_list);
        rvNoteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNoteList.setAdapter(new NoteListAdapter(noteList));
    }
}
