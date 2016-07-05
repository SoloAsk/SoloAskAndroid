package com.soloask.android.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;

import com.soloask.android.R;
import com.umeng.message.PushAgent;

/**
 * Created by Lebron on 2016/6/21.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setEnterTransition(
                    new Explode().excludeTarget(android.R.id.navigationBarBackground, true).excludeTarget(
                            android.R.id.statusBarBackground, true));
            getWindow().setReturnTransition(new Explode().excludeTarget(android.R.id.navigationBarBackground, true));
            postponeEnterTransition();
        }
        setDisplayHomeAsUpEnabled(true);

    }

    public void setDisplayHomeAsUpEnabled(boolean show) {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(show);
    }

    public void setBackGround(int resId) {
        mToolbar.setBackgroundColor(getResources().getColor(resId));
    }

    public void setTitle(int titleResId) {
        setTitle(getString(titleResId));
    }

    protected void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}