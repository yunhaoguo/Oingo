package com.yunhaoguo.oingo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.yunhaoguo.oingo.MainActivity;
import com.yunhaoguo.oingo.R;
import com.yunhaoguo.oingo.utils.ShareUtils;

public class SplashActivity extends AppCompatActivity {

    private MyHandler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1, 2000);


    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("uid", ShareUtils.getInt(SplashActivity.this,"uid", -1));
                        intent.putExtra("uname", ShareUtils.getString(SplashActivity.this, "uname", ""));
                        intent.putExtra("uemail", ShareUtils.getString(SplashActivity.this, "uemail", ""));
                        intent.putExtra("ustate", ShareUtils.getString(SplashActivity.this, "ustate", ""));
                        startActivity(intent);
                    }
                    SplashActivity.this.finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }


    }
    //check whether it is first run
    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this, "firstopen", true);
        if (isFirst) {
            ShareUtils.putBoolean(this, "firstopen", false);
            return true;
        }
        return false;
    }
}
