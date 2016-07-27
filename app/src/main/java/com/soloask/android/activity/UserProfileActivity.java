package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.soloask.android.R;
import com.soloask.android.adapter.HistoryQuestionAdapter;
import com.soloask.android.data.bmob.AskManager;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
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
    private SwipeRefreshLayout mRefreshLayout;
    private HistoryQuestionAdapter mAdapter;
    private IabHelper mHelper;
    private List mDatas;
    private int mLastVisibleItem, mSkipNum;
    private LinearLayoutManager mLayoutManager;
    private User mRespondent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mRespondent = (User) getIntent().getSerializableExtra("user");
        mDatas = new ArrayList();
        mDatas.add(null);
        mAdapter = new HistoryQuestionAdapter(this, mRespondent, mDatas);
        initView();
        initData(Constant.MSG_REFRESH_DATA);
        getCurrentUser();
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

    private void initData(int actionType) {
        AskManager askManager = new AskManager();
        askManager.setOnRespondentQuestionListener(new AskManager.OnRespondentQuestionListener() {
            @Override
            public void onSuccess(List<Question> list) {
                mSkipNum += list.size();
                mDatas.addAll(list);
                mRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                mRefreshLayout.setRefreshing(false);
            }
        });
        if (actionType == Constant.MSG_REFRESH_DATA) {
            refreshUserInfo(mRespondent.getObjectId());
            mSkipNum = 0;
            askManager.getHistoryQuestions(mSkipNum, mRespondent);
        } else {
            askManager.getHistoryQuestions(mSkipNum, mRespondent);
        }
    }

    private void refreshUserInfo(String userId) {
        UserManager userManager = new UserManager();
        userManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                mRespondent = user;
                mAdapter.updateHeaderView(mRespondent);
            }

            @Override
            public void onFailed() {

            }
        });
        userManager.getUserInfo(userId);
    }

    private void getCurrentUser() {
        UserManager userManager = new UserManager();
        userManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                mAdapter.setQuestioner(user);
            }

            @Override
            public void onFailed() {

            }
        });
        userManager.getUserInfo(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_OBJECT_ID, null));
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout_user);
        mRefreshLayout.setRefreshing(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_user_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        addListener();
    }

    private void addListener() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(Constant.MSG_REFRESH_DATA, 2000L);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() >= 10 && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
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
                    initData(Constant.MSG_LOAD_MORE_DATA);
                    break;
                case Constant.MSG_REFRESH_DATA:
                    mDatas.clear();
                    mDatas.add(null);
                    mAdapter.notifyDataSetChanged();
                    initData(Constant.MSG_REFRESH_DATA);
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
