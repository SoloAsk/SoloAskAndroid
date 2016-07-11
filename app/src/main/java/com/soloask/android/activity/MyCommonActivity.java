package com.soloask.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.adapter.BaseAdapter;
import com.soloask.android.adapter.MyAnswerAdapter;
import com.soloask.android.adapter.MyListenAdapter;
import com.soloask.android.adapter.MyQuestionAdapter;
import com.soloask.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyCommonActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private TextView mCountView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mEmptyLayout;
    private TextView mEmptyView;
    private TextView mToOtherView;
    private MyQuestionAdapter mQuestionAdapter;
    private MyAnswerAdapter mAnswerAdapter;
    private MyListenAdapter mListenAdapter;
    private List mDatas;
    private int mLastVisibleItem;
    private BaseAdapter mBaseAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_common);
        initData();
        initView();
    }

    private void initView() {
        mCountView = (TextView) findViewById(R.id.tv_common_count);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.rl_empty);
        mEmptyView = (TextView) findViewById(R.id.tv_hint_empty);
        mToOtherView = (TextView) findViewById(R.id.btn_empty);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_common_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout_mine);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
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
        switch (getIntent().getIntExtra(Constant.KEY_FROM_MINE, 0)) {
            case Constant.KEY_FROM_MY_QUESTION:
                this.setTitle(R.string.title_my_ask);
                /*mCountView.setText(String.format(getResources().getString(R.string.format_asked), 3));
                mQuestionAdapter = new MyQuestionAdapter(this, mDatas);
                mBaseAdapter = mQuestionAdapter;
                mRecyclerView.setAdapter(mQuestionAdapter);*/
                mEmptyView.setText(R.string.notice_no_question);
                mToOtherView.setText(R.string.btn_ask_question);
                mCountView.setVisibility(View.GONE);
                mRefreshLayout.setVisibility(View.GONE);
                mToOtherView.setOnClickListener(this);
                break;
            case Constant.KEY_FROM_MY_ANSWER:
                this.setTitle(R.string.title_my_answer);
                mCountView.setText(String.format(getResources().getString(R.string.format_answered), 3));
                mAnswerAdapter = new MyAnswerAdapter(this, mDatas);
                mBaseAdapter = mAnswerAdapter;
                mRecyclerView.setAdapter(mAnswerAdapter);
                /*mEmptyView.setText(R.string.notice_no_answer);
                mToOtherView.setText(R.string.btn_ask_question);
                mCountView.setVisibility(View.GONE);
                mRefreshLayout.setVisibility(View.GONE);
                mToOtherView.setOnClickListener(this);*/
                break;
            case Constant.KEY_FROM_MY_LISTEN:
                this.setTitle(R.string.title_my_listen);
                mCountView.setText(String.format(getResources().getString(R.string.format_heard), 3));
                mListenAdapter = new MyListenAdapter(this, mDatas);
                mBaseAdapter = mListenAdapter;
                mRecyclerView.setAdapter(mListenAdapter);
                /*mEmptyView.setText(R.string.notice_no_listen);
                mToOtherView.setText(R.string.btn_look_around);
                mCountView.setVisibility(View.GONE);
                mRefreshLayout.setVisibility(View.GONE);
                mToOtherView.setOnClickListener(this);*/
                break;
        }
    }

    private void initData() {
        mDatas = new ArrayList();
        int i = 0;
        while (i < 10) {
            i++;
            mDatas.add("Steven" + i);
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
                    List<String> lists = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        lists.add("Added item" + i);
                    }
                    mDatas.addAll(lists);
                    mBaseAdapter.notifyDataSetChanged();
                    break;
                case Constant.MSG_REFRESH_DATA:
                    mDatas.clear();
                    for (int i = 0; i < 10; i++) {
                        mDatas.add("LeBron James" + i + " | NBA player");
                    }
                    mBaseAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
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
    public void onClick(View v) {
        if (v.getId() == R.id.btn_empty) {
            switch (getIntent().getIntExtra(Constant.KEY_FROM_MINE, 0)) {
                case Constant.KEY_FROM_MY_QUESTION:
                    setResult(Constant.KEY_FROM_MY_QUESTION);
                    finish();
                    break;
                case Constant.KEY_FROM_MY_LISTEN:
                    setResult(Constant.KEY_FROM_MY_LISTEN);
                    finish();
                    break;
                case Constant.KEY_FROM_MY_ANSWER:
                    setResult(Constant.KEY_FROM_MY_ANSWER);
                    finish();
                    break;
            }
        }
    }
}
