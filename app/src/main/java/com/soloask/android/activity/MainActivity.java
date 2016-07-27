package com.soloask.android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mCircleIcon = (BoundImageView) findViewById(R.id.img_user_icon);
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
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mCircleIcon.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.CODE_RESULT_LOGIN) {
            SharedPreferencesHelper.setPreferenceString(MainActivity.this, Constant.KEY_LOGINED_OBJECT_ID, data.getStringExtra("user_object_id"));
            SharedPreferencesHelper.setPreferenceString(MainActivity.this, Constant.KEY_LOGINED_ICON_URL, data.getStringExtra("user_icon_url"));
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
    public void onClick(View v) {
        if (v.getId() == R.id.img_user_icon) {
            if (SharedPreferencesHelper.getPreferenceString(MainActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
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
