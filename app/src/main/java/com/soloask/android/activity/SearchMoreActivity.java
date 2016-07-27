package com.soloask.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.soloask.android.R;
import com.soloask.android.adapter.SearchPersonAdapter;
import com.soloask.android.adapter.SearchQuestionAdapter;
import com.soloask.android.data.bmob.SearchManager;
import com.soloask.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeBron on 2016/7/20.
 */
public class SearchMoreActivity extends BaseActivity {
    private RecyclerView mRecyclerview;
    private SearchPersonAdapter mPersonAdapter;
    private SearchQuestionAdapter mQuestionAdapter;
    private LinearLayoutManager mLayoutManager;

    private List mData = new ArrayList();
    private boolean isFromPerson;
    private String mSearchContent;
    private int mLastVisibleItem, mSkipNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_more);
        initView();
        initData();
    }

    private void initView() {
        mSearchContent = getIntent().getStringExtra(Constant.KEY_SEARCH_CONTENT);
        this.setTitle(mSearchContent);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_search_more_view);
        isFromPerson = getIntent().getBooleanExtra(Constant.KEY_FROM_SEARCH, false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        if (isFromPerson) {
            mPersonAdapter = new SearchPersonAdapter(this, mData);
            mRecyclerview.setAdapter(mPersonAdapter);
        } else {
            mQuestionAdapter = new SearchQuestionAdapter(this, mData);
            mRecyclerview.setAdapter(mQuestionAdapter);
        }
        addListener();
    }

    private void addListener() {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mPersonAdapter.getItemCount() >= 10 && mLastVisibleItem + 1 == mPersonAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(Constant.MSG_LOAD_MORE_DATA, 500L);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initData() {
        if (isFromPerson) {
            SearchManager searchManager = new SearchManager();
            searchManager.setOnSearchUserListener(new SearchManager.OnSearchUserListener() {
                @Override
                public void onSuccess(List users) {
                    if (users == null) {
                        return;
                    }
                    mSkipNum += users.size();
                    mData.addAll(users);
                    mPersonAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed() {
                    Toast.makeText(SearchMoreActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                }
            });
            searchManager.getSearchUsers(mSearchContent, 10, mSkipNum);
        } else {
            SearchManager searchManager = new SearchManager();
            searchManager.setOnSearchQuestionListener(new SearchManager.OnSearchQuestionListener() {
                @Override
                public void onSuccess(List questions) {
                    if (questions == null) {
                        return;
                    }
                    mSkipNum += questions.size();
                    mData.addAll(questions);
                    mQuestionAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed() {
                    Toast.makeText(SearchMoreActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                }
            });
            searchManager.getSearchQuestions(mSearchContent, 10, mSkipNum);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MSG_LOAD_MORE_DATA:
                    initData();
                    break;
            }
        }
    };
}
