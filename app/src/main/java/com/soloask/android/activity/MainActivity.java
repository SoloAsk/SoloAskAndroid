package com.soloask.android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.adapter.MainAdapter;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.BoundImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Lebron on 2016/6/21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainAdapter mMainAdapter;
    private BoundImageView mCircleIcon;
    private long mClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setDisplayHomeAsUpEnabled(false);
        initView();
        //SharedPreferencesHelper.setPreferenceBoolean(MainActivity.this, Constant.KEY_IS_LOGINED, true);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mCircleIcon = (BoundImageView) findViewById(R.id.img_user_icon);
        //mCircleIcon.showMark(true);
        String[] tabs = new String[]{getResources().getString(R.string.tab_name1), getResources().getString(R.string.tab_name2)};
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), tabs);
        mViewPager.setAdapter(mMainAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mCircleIcon.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.CODE_RESULT_LOGIN) {
            SharedPreferencesHelper.setPreferenceBoolean(MainActivity.this, Constant.KEY_IS_LOGINED, true);
            //https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xft1/v/t1.0-1/p720x720/11703164_1432811433714866_750303433030121790_n.jpg?oh=f9aa1e1d275210264a6efe9c4610a43e&oe=57FA6E1E&__gda__=1475159383_29c1bff1cd0a7cea3cc22126082aca73
            Log.i("Lebron", data.getStringExtra("user_icon_url"));
            Glide.with(MainActivity.this)
                    .load(data.getStringExtra("user_icon_url"))
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mCircleIcon);
        } else {
            Log.i("Lebron", "  do nothing");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_user_icon) {
            if (SharedPreferencesHelper.getPreferenceBoolean(MainActivity.this, Constant.KEY_IS_LOGINED, false)) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, Constant.CODE_REQUEST);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - mClickTime) > 2000) {
            Toast.makeText(getApplicationContext(), R.string.notice_back_to_exit, Toast.LENGTH_SHORT).show();
            mClickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
