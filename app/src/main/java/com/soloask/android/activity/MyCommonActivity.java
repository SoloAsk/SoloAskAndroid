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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.adapter.BaseAdapter;
import com.soloask.android.adapter.MyAnswerAdapter;
import com.soloask.android.adapter.MyListenAdapter;
import com.soloask.android.adapter.MyQuestionAdapter;
import com.soloask.android.data.bmob.MineManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyCommonActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mEmptyLayout;
    private TextView mEmptyView;
    private TextView mToOtherView;
    private MyQuestionAdapter mQuestionAdapter;
    private MyAnswerAdapter mAnswerAdapter;
    private MyListenAdapter mListenAdapter;
    private List mDatas = new ArrayList();
    private int mFrom, mLastVisibleItem, mSkipNum;
    private User mUser;
    private BaseAdapter mBaseAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_common);
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser == null) {
            return;
        }
        mFrom = getIntent().getIntExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_QUESTION);
        initData(Constant.MSG_REFRESH_DATA, mFrom);
        initView();
    }

    private void initView() {
        mEmptyLayout = (RelativeLayout) findViewById(R.id.rl_empty);
        mEmptyView = (TextView) findViewById(R.id.tv_hint_empty);
        mToOtherView = (TextView) findViewById(R.id.btn_empty);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_common_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout_mine);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);
        mToOtherView.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mBaseAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(Constant.MSG_LOAD_MORE_DATA, 2000L);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initAdapter();
    }

    private void initAdapter() {
        switch (mFrom) {
            case Constant.KEY_FROM_MY_QUESTION:
                this.setTitle(R.string.title_my_ask);
                mEmptyView.setText(R.string.notice_no_question);
                mQuestionAdapter = new MyQuestionAdapter(this, mDatas);
                mBaseAdapter = mQuestionAdapter;
                mRecyclerView.setAdapter(mQuestionAdapter);
                break;
            case Constant.KEY_FROM_MY_ANSWER:
                this.setTitle(R.string.title_my_answer);
                mEmptyView.setText(R.string.notice_no_answer);
                mAnswerAdapter = new MyAnswerAdapter(this, mDatas);
                mBaseAdapter = mAnswerAdapter;
                mRecyclerView.setAdapter(mAnswerAdapter);
                break;
            case Constant.KEY_FROM_MY_LISTEN:
                this.setTitle(R.string.title_my_listen);
                mEmptyView.setText(R.string.notice_no_listen);
                mListenAdapter = new MyListenAdapter(this, mDatas);
                mBaseAdapter = mListenAdapter;
                mRecyclerView.setAdapter(mListenAdapter);
                break;
        }
    }

    private void initData(int actionType, int from) {
        MineManager mineManager = new MineManager(mUser);
        mineManager.setOnGetQuestionListener(new MineManager.OnGetQuestionListener() {
            @Override
            public void onSuccess(List<Question> list) {
                //如果是首次并且没有数据
                if (isFirst && list.size() == 0) {
                    mRefreshLayout.setVisibility(View.GONE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
                    mToOtherView.setText(R.string.btn_ask_question);
                } else {
                    isFirst = false;
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    mEmptyLayout.setVisibility(View.GONE);
                    mSkipNum += list.size();
                    mDatas.addAll(list);
                    mRefreshLayout.setRefreshing(false);
                    mBaseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed() {
                if (mDatas.size() == 0) {
                    mEmptyView.setText(R.string.failed_to_load_data);
                    mToOtherView.setText(R.string.btn_retry);
                    mRefreshLayout.setVisibility(View.GONE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
        if (actionType == Constant.MSG_REFRESH_DATA) {
            mSkipNum = 0;
            mineManager.getQuestions(mSkipNum, from);
        } else {
            mineManager.getQuestions(mSkipNum, from);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(Constant.MSG_REFRESH_DATA, 2000L);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MSG_LOAD_MORE_DATA:
                    initData(Constant.MSG_LOAD_MORE_DATA, mFrom);
                    break;
                case Constant.MSG_REFRESH_DATA:
                    mDatas.clear();
                    mBaseAdapter.notifyDataSetChanged();
                    initData(Constant.MSG_REFRESH_DATA, mFrom);
                    break;
            }
        }
    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.KEY_FROM_ANSWER) {
            mDatas.clear();
            mBaseAdapter.notifyDataSetChanged();
            initData(Constant.MSG_REFRESH_DATA, mFrom);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_empty) {
            if (mToOtherView.getText().equals(getString(R.string.btn_ask_question))) {
                setResult(Constant.KEY_FROM_MY_QUESTION);
                finish();
            } else {
                initData(Constant.MSG_REFRESH_DATA, mFrom);
            }

        }
    }
}
