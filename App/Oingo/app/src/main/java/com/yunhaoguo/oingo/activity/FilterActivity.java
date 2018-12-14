package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.adapter.FilterListAdapter;
import com.yunhaoguo.oingo.entity.Filter;
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

public class FilterActivity extends AppCompatActivity {

    private List<Filter> filterList = new ArrayList<>();
    private ExpandableListView filterListView;
    private FilterListAdapter adapter;

    private EditText etInputDelete;
    private Button btnDelete;
    private Button btnAdd;

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
    protected void onResume() {
        super.onResume();
        // setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                finish();
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

        etInputDelete = findViewById(R.id.et_input_delete);
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etInputDelete.getText().toString())) {
                    int fposition = Integer.parseInt(etInputDelete.getText().toString());
                    if (fposition <= filterList.size()) {
                        deleteFilter(filterList.get(fposition - 1).getFid(), fposition - 1);
                    }

                }
            }
        });
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etInputDelete.getText().toString())) {
                    int fposition = Integer.parseInt(etInputDelete.getText().toString());
                    AccountUtils.setFid(filterList.get(fposition - 1).getFid());
                }
            }
        });
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new FilterListAdapter(this, formatIdData(filterList), formatNameData(filterList), formatAttrData(filterList));
            adapter.setOnItemClickListener(new FilterListAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    deleteFilter(filterList.get(position).getFid(), position);
                }
            });
            filterListView.setAdapter(adapter);

        }
    }

    private void deleteFilter(int fid, final int position) {
        QueryUtils.deleteFilter(fid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    int res = responseObj.getInt("result");
                    if (res == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FilterActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                                filterList.remove(position);
                                adapter.updateData(formatIdData(filterList), formatNameData(filterList), formatAttrData(filterList));
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FilterActivity.this, "delete failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData() {
        QueryUtils.getFilterList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Gson gson = new Gson();
                    filterList = gson.fromJson(responseObj.getString("result"), new TypeToken<List<Filter>>() {
                    }.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter.updateData(formatIdData(filterList), formatNameData(filterList), formatAttrData(filterList));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private List<Integer> formatIdData(List<Filter> filters) {
        List<Integer> formattedData = new ArrayList<>();
        for (Filter filter : filters) {
            formattedData.add(filter.getFid());
        }
        return formattedData;
    }
    private List<String> formatNameData(List<Filter> filters) {
        List<String> formattedData = new ArrayList<>();
        for (Filter filter : filters) {
            formattedData.add(filter.getFname());
        }
        return formattedData;
    }

    private List<List<String>> formatAttrData(List<Filter> filters) {
        List<List<String>> formattedData = new ArrayList<>();
        for (Filter filter : filters) {
            formattedData.add(filter.getAttributes());
        }
        return formattedData;
    }



}
