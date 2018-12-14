package com.yunhaoguo.oingo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.yunhaoguo.oingo.activity.MapActivity;
import com.yunhaoguo.oingo.activity.NoteDetailActivity;
import com.yunhaoguo.oingo.activity.ProfileActivity;
import com.yunhaoguo.oingo.adapter.NoteListAdapter;
import com.yunhaoguo.oingo.entity.Note;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;
import com.yunhaoguo.oingo.utils.ShareUtils;

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

    private SwipeRefreshLayout srlNoteList;

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

        if (AccountUtils.getFid() == -1) {
            initData();
        } else {
            initData(AccountUtils.getFid());
        }
        initView(view);
        return view;
    }

    private void initData(int fid) {
        QueryUtils.getFilteredNotesList(fid, new Callback() {
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
                    Log.i("notelist", noteList.get(0).getNcontent());
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
                intent.putExtra("nendtime", note.getEndTime());
                intent.putExtra("nrepeattype", note.getRepeatType());
                startActivity(intent);
            }
        });
        noteListAdapter.setOnItemLongClickListener(new NoteListAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Are you sure to delete this note?")
                        .setNegativeButton("cancel", null).setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        if (noteList.get(position).getUid() != AccountUtils.getUid()) {
                                            Toast.makeText(getActivity(), "Sorry, it is not your note, you have no permission to delete it", Toast.LENGTH_LONG).show();
                                        } else {
                                            deleteNote(noteList.get(position).getNid());
                                        }
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        }).create();
                dialog.show();
            }
        });
        srlNoteList = view.findViewById(R.id.srl_note_list);
        srlNoteList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AccountUtils.getFid() != -1) {
                    initData(AccountUtils.getFid());
                } else {
                    initData();
                }
                srlNoteList.setRefreshing(false);
            }
        });
    }

    private void deleteNote(int nid) {
        QueryUtils.deleteNote(nid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    if (responseObj.getInt("result") == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "delete success", Toast.LENGTH_SHORT).show();
                                initData();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "delete failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            case R.id.map_view_action:
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                break;
            case R.id.filter_action:
                intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_action:
                // TODO: Start profile activity here.
                intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("uid", AccountUtils.getUid());
                startActivity(intent);
                break;
            case R.id.logout_action:
                ShareUtils.putBoolean(getActivity(), "firstopen", true);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
