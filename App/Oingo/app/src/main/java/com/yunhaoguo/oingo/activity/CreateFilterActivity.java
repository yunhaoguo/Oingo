package com.yunhaoguo.oingo.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.Filter;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivOpenMap;

    private Button btnFromDate;
    private Button btnToDate;
    private Button btnFromTime;
    private Button btnToTime;

    private EditText etFilterName;
    private EditText etLocation;
    private EditText etRadius;
    private EditText etFromDate;
    private EditText etToDate;
    private EditText etFromTime;
    private EditText etToTime;

    private CheckBox cbReceiveFromFriend;

    private Button btnCancel;
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_create_filter);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            double lat = data.getExtras().getDouble("lat", 0);
            double lng = data.getExtras().getDouble("lng", 0);
            if (lat != 0 && lng != 0) {
                etLocation.setText(String.valueOf(lat).substring(0, 11)
                        + ',' + String.valueOf(lng).substring(0, 11));
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fopen_map:
                startActivityForResult(new Intent(this, MapActivity.class),1);
                break;
            case R.id.btn_ffrom_date:
                showDatePickerDialog(etFromDate);
                break;
            case R.id.btn_fto_date:
                showDatePickerDialog(etToDate);
                break;
            case R.id.btn_ffrom_time:
                showTimePickerDialog(etFromTime);
                break;
            case R.id.btn_fto_time:
                showTimePickerDialog(etToTime);
                break;
            case R.id.btn_cancel_create_filter:
                finish();
                break;
            case R.id.btn_finish_create_filter:
                checkValidityAndCreate();
                break;
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_filter_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // TODO: How to force the FilterActivity reload?
            }
        });

        ivOpenMap = findViewById(R.id.iv_fopen_map);
        ivOpenMap.setOnClickListener(this);
        etLocation = findViewById(R.id.et_flocation);

        btnFromDate = findViewById(R.id.btn_ffrom_date);
        btnToDate = findViewById(R.id.btn_fto_date);
        btnFromTime = findViewById(R.id.btn_ffrom_time);
        btnToTime = findViewById(R.id.btn_fto_time);
        btnFromDate.setOnClickListener(this);
        btnToDate.setOnClickListener(this);
        btnFromTime.setOnClickListener(this);
        btnToTime.setOnClickListener(this);

        etFilterName = findViewById(R.id.et_fname);
        etRadius = findViewById(R.id.et_fradius);
        etFromDate = findViewById(R.id.et_fstart_date);
        etToDate = findViewById(R.id.et_fend_date);
        etFromTime = findViewById(R.id.et_fstart_time);
        etToTime = findViewById(R.id.et_fend_time);

        cbReceiveFromFriend = findViewById(R.id.cbx_from_friend);

        btnCancel = findViewById(R.id.btn_cancel_create_filter);
        btnFinish = findViewById(R.id.btn_finish_create_filter);
        btnCancel.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimePickerDialog(final EditText editText) {
        TimePickerDialog timeDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String hour = "";
                        String min = "";
                        if(hourOfDay<10){
                            hour ="0"+hourOfDay;
                        } else {
                            hour = "" + hourOfDay;
                        }
                        if(minute<10){
                            min="0"+minute;
                        } else {
                            min = "" + minute;
                        }
                        editText.setText(hour + ":" + min);
                    }
                }, 0, 0, true);
        timeDialog.show();
    }

    private void checkValidityAndCreate() {
        if (TextUtils.isEmpty(etFilterName.getText().toString()) || TextUtils.isEmpty(etLocation.getText().toString())
                || TextUtils.isEmpty(etRadius.getText().toString()) || TextUtils.isEmpty(etFromDate.getText().toString())
                || TextUtils.isEmpty(etToDate.getText().toString()) || TextUtils.isEmpty(etToTime.getText().toString())
                || TextUtils.isEmpty(etFromTime.getText().toString())) {
            Toast.makeText(this, "You have empty field", Toast.LENGTH_SHORT).show();
            return;
        }
        Filter filter = new Filter();
        filter.setUid(AccountUtils.getUid());
        filter.setFname(etFilterName.getText().toString());
        filter.setFlocation(etLocation.getText().toString());
        filter.setFradius(Integer.parseInt(etRadius.getText().toString()));
        filter.setFstarttime(etFromDate.getText().toString() + " " + etFromTime.getText().toString() + ":00");
        filter.setFendtime(etToDate.getText().toString() + " " + etToTime.getText().toString() + ":00");

        QueryUtils.createFilter(filter, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Connect server failure.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    if (responseObj.getInt("result") == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreateFilterActivity.this, "Create filter success", Toast.LENGTH_SHORT).show();
                            }
                        });
                        CreateFilterActivity.this.finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreateFilterActivity.this, "Create filter failed", Toast.LENGTH_SHORT).show();
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
