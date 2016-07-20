package com.soloask.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    private List mData = new ArrayList();
    private boolean isFromPerson;
    private String mSearchContent;

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
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        if (isFromPerson) {
            mPersonAdapter = new SearchPersonAdapter(this, mData);
            mRecyclerview.setAdapter(mPersonAdapter);
        } else {
            mQuestionAdapter = new SearchQuestionAdapter(this, mData);
            mRecyclerview.setAdapter(mQuestionAdapter);
        }
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
                    mData.addAll(users);
                    mPersonAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed() {

                }
            });
            searchManager.getSearchUsers(mSearchContent, 10);
        } else {
            SearchManager searchManager = new SearchManager();
            searchManager.setOnSearchQuestionListener(new SearchManager.OnSearchQuestionListener() {
                @Override
                public void onSuccess(List questions) {
                    if (questions == null) {
                        return;
                    }
                    mData.addAll(questions);
                    mQuestionAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed() {

                }
            });
            searchManager.getSearchUsers(mSearchContent, 10);
        }
    }
}
