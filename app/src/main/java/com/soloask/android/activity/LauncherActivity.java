package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.util.Constant;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by Lebron on 2016/6/29.
 */
public class LauncherActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable UmengPush
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        Log.d("Lebron", registrationId);
                    }
                });
            }
        });

        setContentView(R.layout.activity_launcher);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTextView = (TextView) findViewById(R.id.tv_start_slogan);
        if (getIntent().getBooleanExtra(Constant.KEY_FROM_ABOUT, false)) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setTitle(R.string.title_about);
            mTextView.setText("V1.0.0");
        } else {
            mToolbar.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent mainIntent = new Intent(LauncherActivity.this,
                            MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }, 1000L);
        }

    }

}
