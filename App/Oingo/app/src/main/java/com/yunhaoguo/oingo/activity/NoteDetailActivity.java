package com.yunhaoguo.oingo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.adapter.CommentListAdapter;
import com.yunhaoguo.oingo.entity.Comment;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.DateUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNoteContent;
    private TextView tvNoteUName;
    private TextView tvNoteTime;

    private int noteid;
    private String noteContent;
    private String noteUname;
    private String noteTime;

    private List<Comment> commentsList = new ArrayList<>();
    private RecyclerView rvCommentsList;
    private CommentListAdapter commentListAdapter;

    private LinearLayout llCommentIcon;
    private ImageView ivCommentIcon;
    private RelativeLayout rlComment;
    private TextView tvHide;
    private Button btnSend;
    private EditText etContent;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initData();
        initView();
    }

    private void initView() {
        tvNoteContent = findViewById(R.id.tv_note_item);
        tvNoteContent.setText(noteContent);
        tvNoteTime = findViewById(R.id.tv_start_date);
        tvNoteTime.setText(noteTime);
        tvNoteUName = findViewById(R.id.tv_from);
        tvNoteUName.setText(noteUname);
        rvCommentsList = findViewById(R.id.rv_comments);
        rvCommentsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentListAdapter = new CommentListAdapter(commentsList);
        rvCommentsList.setAdapter(commentListAdapter);

        llCommentIcon = findViewById(R.id.ll_comment);
        ivCommentIcon = findViewById(R.id.iv_comment);
        ivCommentIcon.setOnClickListener(this);
        rlComment = findViewById(R.id.rl_comment);
        tvHide = findViewById(R.id.tv_hide_down);
        tvHide.setOnClickListener(this);
        btnSend = findViewById(R.id.btn_comment_send);
        btnSend.setOnClickListener(this);
        etContent = findViewById(R.id.et_comment_content);

        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initData() {
        Intent intent = getIntent();
        noteid = intent.getIntExtra("nid", -1);
        if (noteid != -1) {
            QueryUtils.getNoteDetail(noteid, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject responseObj = new JSONObject(response.body().string());
                        Gson gson = new Gson();
                        commentsList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<Comment>>() {
                        }.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commentListAdapter.updateData(commentsList);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        noteContent = intent.getStringExtra("ncontent");
        noteTime = intent.getStringExtra("nstarttime");
        noteUname = intent.getStringExtra("nuname");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_comment:
                // pop up input method
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // show comment field
                llCommentIcon.setVisibility(View.GONE);
                rlComment.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_hide_down:
                // hide comment field
                llCommentIcon.setVisibility(View.VISIBLE);
                rlComment.setVisibility(View.GONE);
                // hide input field, cache the content
                imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
                break;
            case R.id.btn_comment_send:
                sendComment();
                llCommentIcon.setVisibility(View.VISIBLE);
                rlComment.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
                break;
            default:
                break;

        }
    }

    private void sendComment() {
        if(etContent.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "comment cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            Comment comment = new Comment();
            comment.setUid(AccountUtils.getUid());
            comment.setCcontent(etContent.getText().toString());
            comment.setUname(AccountUtils.getUname());
            comment.setCtime(DateUtils.getCurrentTime());
            commentsList.add(comment);
            commentListAdapter.updateData(commentsList);
            etContent.setText("");
            updateDatabase(comment);


        }

    }

    private void updateDatabase(Comment comment) {
        QueryUtils.addComment(comment, noteid, new Callback() {
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
                                Toast.makeText(getApplicationContext(), "comment success！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "comment failed！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
