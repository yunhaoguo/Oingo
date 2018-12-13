package com.yunhaoguo.oingo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.activity.FilterActivity;
import com.yunhaoguo.oingo.activity.LoginActivity;
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



public class NotesFragment extends Fragment {

    private List<Note> noteList = new ArrayList<>();

    private NoteListAdapter noteListAdapter;

    private RecyclerView rvNoteList;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, null);

        // Add tool bar at the top.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.notes_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // Explicitly call onCreateOptionsMenu() callback
        // This happens because the activity call this method before build the fragment.
        setHasOptionsMenu(true);

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
                intent.putExtra("nuid", note.getUid());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_action:
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_action:
                Toast.makeText(getActivity(), "PROFILE.", Toast.LENGTH_SHORT).show();
                // TODO: Start profile activity here.
                break;
            case R.id.logout_action:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
