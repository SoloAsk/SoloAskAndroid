package com.soloask.android;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.main.view.impl.MainActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by Lebron on 2016/6/29.
 */
public class LauncherActivity extends BaseActivity {
    private Toolbar mToolbar;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initViewsAndData() {
        //enable Bmob
        Bmob.initialize(this, "26cc3d0d29e618b194be911c994efd11");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
