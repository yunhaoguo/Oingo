package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.yunhaoguo.oingo.MainActivity;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.adapter.FilterListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private ExpandableListView filterListView;
    private FilterListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_filter);

        setData();
        initView();
        setAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.add_filter_action:
//                startActivity(new Intent(FilterActivity.this, CreateFilterActivity.class));
//                break;
//        }
        if (item.getItemId() == R.id.add_filter_action) {
            startActivity(new Intent(FilterActivity.this, CreateFilterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                startActivity(intent);
                // TODO: Send back the chosen filter
                // TODO: Or add an global setting in AccountUtil
            }
        });
        filterListView = (ExpandableListView) findViewById(R.id.expand_list);
        filterListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        filterListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void setData() {
        // TODO: Use data from Back-end
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new FilterListAdapter(this, new ArrayList<List<Integer>>());
            filterListView.setAdapter(adapter);
        } else {
            // TODO: adapter.flashData(List<List<Filters>>)
        }
    }



}
