package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.soloask.android.R;
import com.soloask.android.adapter.HistoryQuestionAdapter;
import com.soloask.android.util.Constant;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.view.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lebron on 2016/6/21.
 */
public class UserProfileActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private HistoryQuestionAdapter mAdapter;
    private IabHelper mHelper;
    private List mDatas;
    private int mLastVisibleItem;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initData();
        initView();
        initIabHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ShareDialog shareDialog = new ShareDialog(UserProfileActivity.this);
            shareDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mDatas = new ArrayList();
        int i = 0;
        while (i < 10) {
            i++;
            mDatas.add("Young brother , the Warriors lost the finals miserable how to do ?" + i);
        }
    }

    private void initView() {
        mAdapter = new HistoryQuestionAdapter(this, mDatas);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_user_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        addListener();
    }

    private void addListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(Constant.MSG_LOAD_MORE_DATA, 2000L);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initIabHelper() {
        try {
            if (mHelper == null) {
                mHelper = new IabHelper(this, Constant.BASE64_ENCODED_PUBLIC_KEY);
            }
            mHelper.enableDebugLogging(true);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        Toast.makeText(UserProfileActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mAdapter != null) {
                        mAdapter.setIABHelper(mHelper);
                    }
                }
            });
        } catch (Exception e) {
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MSG_LOAD_MORE_DATA:
                    List<String> lists = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        lists.add("Added item" + i);
                    }
                    mDatas.addAll(lists);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (Exception e) {
            }
            mHelper = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.i("Lebron", "onActivityResult handled by IABUtil.");
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
