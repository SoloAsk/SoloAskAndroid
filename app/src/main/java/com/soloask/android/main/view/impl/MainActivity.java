package com.soloask.android.main.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.view.impl.LoginActivity;
import com.soloask.android.account.view.impl.UserActivity;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.main.adapter.MainAdapter;
import com.soloask.android.main.module.MainModule;
import com.soloask.android.search.view.impl.SearchActivity;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.BoundImageView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by lebron on 16-8-5.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager_main)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.img_user_icon)
    BoundImageView mCircleIcon;

    @Inject
    Bus mBus;

    private MainAdapter mMainAdapter;
    private long mClickTime = 0;

    @OnClick(R.id.img_user_icon)
    public void gotoUserActivity() {
        if (SharedPreferencesHelper.getPreferenceString(MainActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, Constant.CODE_REQUEST);
        }
    }

    @Subscribe
    public void logout(String event) {
        if (event.equals(Constant.BUS_EVENT_LOGOUT)) {
            Glide.with(MainActivity.this)
                    .load(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_ICON_URL, null))
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mCircleIcon);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new MainModule())
                .inject(this);
        this.setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndData() {
        mCircleIcon.showMention(true);
        Glide.with(MainActivity.this)
                .load(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_ICON_URL, null))
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCircleIcon);
        String[] tabs = new String[]{getResources().getString(R.string.tab_name1), getResources().getString(R.string.tab_name2)};
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), tabs);
        mViewPager.setAdapter(mMainAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            if (SharedPreferencesHelper.getPreferenceString(MainActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, Constant.CODE_REQUEST);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.CODE_RESULT_LOGIN) {
            SharedPreferencesHelper.setPreferenceString(MainActivity.this, Constant.KEY_LOGINED_OBJECT_ID, data.getStringExtra("user_id"));
            SharedPreferencesHelper.setPreferenceString(MainActivity.this, Constant.KEY_LOGINED_ICON_URL, data.getStringExtra("user_icon_url"));
            SharedPreferencesHelper.setPreferenceString(MainActivity.this, Constant.KEY_TOKEN, data.getStringExtra("token"));
            Log.i("MainActivity", data.getStringExtra("token"));
            Glide.with(MainActivity.this)
                    .load(data.getStringExtra("user_icon_url"))
                    //.placeholder(R.drawable.ic_me_default)
                    .error(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mCircleIcon);
        } else if (resultCode == Constant.KEY_FROM_MY_QUESTION) {
            mViewPager.setCurrentItem(1);
        } else {
            Log.i("MainActivity", "  do nothing");
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
}
