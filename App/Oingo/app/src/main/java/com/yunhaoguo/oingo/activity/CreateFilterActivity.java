package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yunhaoguo.oingo.R;

public class CreateFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_create_filter);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_filter_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateFilterActivity.this, FilterActivity.class);
                startActivity(intent);
                // TODO: How to force the FilterActivity reload?
            }
        });
    }
}
