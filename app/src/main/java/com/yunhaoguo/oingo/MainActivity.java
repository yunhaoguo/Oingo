package com.yunhaoguo.oingo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yunhaoguo.oingo.adapter.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNoteList;

    private List<String> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        noteList = new ArrayList<>();
        String temp = "item";
        for(int i = 0; i < 20; i++) {
            noteList.add(i + temp);
        }

    }

    private void initView() {
        rvNoteList = findViewById(R.id.rv_note_list);
        rvNoteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvNoteList.setAdapter(new NoteListAdapter(noteList));
    }
}
