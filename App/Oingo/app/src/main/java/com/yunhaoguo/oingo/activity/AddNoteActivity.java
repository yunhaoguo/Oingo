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
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.entity.Note;
import com.yunhaoguo.oingo.utils.AccountUtils;
import com.yunhaoguo.oingo.utils.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivOpenMap;


    private Button btnFromDate;
    private Button btnToDate;
    private Button btnFromTime;
    private Button btnToTime;

    private EditText etContent;
    private EditText etRadius;
    private EditText etFromDate;
    private EditText etToDate;
    private EditText etFromTime;
    private EditText etToTime;
    private EditText etLocation;
    private CheckBox cbAllowComment;
    private RadioGroup rgRepeatType;
    private EditText etTag;

    private Button btnPublish;

    private String repeatType = "Every day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_add_note);
        initView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Note");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivOpenMap = findViewById(R.id.iv_open_map);
        ivOpenMap.setOnClickListener(this);
        etLocation = findViewById(R.id.et_location);

        btnFromDate = findViewById(R.id.btn_select_from_date);
        btnFromDate.setOnClickListener(this);
        btnToDate = findViewById(R.id.btn_select_to_date);
        btnToDate.setOnClickListener(this);
        btnFromTime = findViewById(R.id.btn_select_from_time);
        btnFromTime.setOnClickListener(this);
        btnToTime = findViewById(R.id.btn_select_to_time);
        btnToTime.setOnClickListener(this);

        etFromDate = findViewById(R.id.et_nstart_date);
        etToDate = findViewById(R.id.et_nend_date);
        etFromTime = findViewById(R.id.et_nstart_time);
        etToTime = findViewById(R.id.et_nend_time);
        etContent = findViewById(R.id.et_note_content);
        etRadius = findViewById(R.id.et_nradius);
        cbAllowComment = findViewById(R.id.cbx_allow_comment);
        rgRepeatType = findViewById(R.id.rg_repeat_type);
        rgRepeatType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        repeatType = "Every day";
                        break;
                    case R.id.rb_sun:
                        repeatType = "Sunday";
                        break;
                    case R.id.rb_mon:
                        repeatType = "Monday";
                        break;
                    case R.id.rb_tue:
                        repeatType = "Tuesday";
                        break;
                    case R.id.rb_wed:
                        repeatType = "Wednesday";
                        break;
                    case R.id.rb_thu:
                        repeatType = "Thursday";
                        break;
                    case R.id.rb_fri:
                        repeatType = "Friday";
                        break;
                    case R.id.rb_sat:
                        repeatType = "Saturday";
                        break;

                }
            }
        });
        etTag = findViewById(R.id.et_tag);
        btnPublish = findViewById(R.id.btn_publish_note);
        btnPublish.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_map:
                startActivityForResult(new Intent(this, MapActivity.class), 1);
                break;
            case R.id.btn_select_from_date:
                showDatePickerDialog(etFromDate);
                break;
            case R.id.btn_select_to_date:
                showDatePickerDialog(etToDate);
                break;
            case R.id.btn_select_from_time:
                showTimePickerDialog(etFromTime);
                break;
            case R.id.btn_select_to_time:
                showTimePickerDialog(etToTime);
                break;

            case R.id.btn_publish_note:
                checkValidityAndPublish();
                break;
        }
    }

    private void checkValidityAndPublish() {
        if (TextUtils.isEmpty(etContent.getText().toString()) || TextUtils.isEmpty(etLocation.getText().toString())
                || TextUtils.isEmpty(etRadius.getText().toString()) || TextUtils.isEmpty(etFromDate.getText().toString())
                || TextUtils.isEmpty(etToDate.getText().toString()) || TextUtils.isEmpty(etToTime.getText().toString())
                || TextUtils.isEmpty(etFromTime.getText().toString()) || TextUtils.isEmpty(etTag.getText().toString())) {
            Toast.makeText(this, "You have empty field", Toast.LENGTH_SHORT).show();
        } else {
            Note note = new Note();
            note.setUid(AccountUtils.getUid());
            note.setNcontent(etContent.getText().toString());
            note.setStartTime(etFromDate.getText().toString() + " " + etFromTime.getText().toString() + ":00");
            note.setEndTime(etToDate.getText().toString() + " " + etToTime.getText().toString() + ":00");
            note.setAllowComment(cbAllowComment.isChecked() ? 1 : 0);
            note.setNlocation(etLocation.getText().toString());
            note.setRepeatType(repeatType);
            note.setNradius(Integer.parseInt(etRadius.getText().toString()));
            note.setNtag(etTag.getText().toString());
            QueryUtils.addNote(note, new Callback() {
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
                                    Toast.makeText(AddNoteActivity.this, "Add note success", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AddNoteActivity.this.finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddNoteActivity.this, "Add note failed", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            double lat = data.getExtras().getDouble("lat", 0);
            double lng = data.getExtras().getDouble("lng", 0);
            if (lat != 0 && lng != 0) {
                etLocation.setText(String.valueOf(lat).substring(0,11) + "," + String.valueOf(lng).substring(0,11));
            }
        }



    }


}
