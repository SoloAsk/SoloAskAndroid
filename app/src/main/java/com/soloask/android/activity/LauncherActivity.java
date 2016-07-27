package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.soloask.android.R;

import cn.bmob.v3.Bmob;

/**
 * Created by Lebron on 2016/6/29.
 */
public class LauncherActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable Bmob
        Bmob.initialize(this, "26cc3d0d29e618b194be911c994efd11");

        setContentView(R.layout.activity_launcher);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);
        mTextView = (TextView) findViewById(R.id.tv_start_slogan);
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
