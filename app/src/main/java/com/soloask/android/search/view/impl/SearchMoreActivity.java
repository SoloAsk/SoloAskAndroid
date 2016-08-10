package com.soloask.android.search.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.activity.UserProfileActivity;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.question.view.impl.QuestionDetailActivity;
import com.soloask.android.search.adapter.SearchPersonAdapter;
import com.soloask.android.search.adapter.SearchQuestionAdapter;
import com.soloask.android.search.injection.SearchMoreModule;
import com.soloask.android.search.presenter.SearchMorePresenter;
import com.soloask.android.search.view.SearchMoreView;
import com.soloask.android.util.Constant;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by LeBron on 2016/7/20.
 */
public class SearchMoreActivity extends BaseActivity implements SearchMoreView
        , BaseQuickAdapter.OnRecyclerViewItemClickListener
        , BaseQuickAdapter.RequestLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_search_more_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout_search)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressbar_loading)
    MaterialProgressBar mProgressbar;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetworkLayout;

    @Inject
    SearchMorePresenter mPresenter;

    private BaseQuickAdapter mBaseAdapter;
    private List mData = new ArrayList();
    private boolean isFromPerson;
    private String mSearchContent;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_search_more;
    }

    @Override
    protected void initViewsAndData() {
        mSearchContent = getIntent().getStringExtra(Constant.KEY_SEARCH_CONTENT);
        isFromPerson = getIntent().getBooleanExtra(Constant.KEY_FROM_SEARCH, false);
        this.setTitle(mSearchContent);
        if (isFromPerson) {
            mBaseAdapter = new SearchPersonAdapter(mData);
        } else {
            mBaseAdapter = new SearchQuestionAdapter(mData);
        }
        mBaseAdapter.setOnLoadMoreListener(this);
        mBaseAdapter.openLoadMore(10, true);
        mBaseAdapter.setOnRecyclerViewItemClickListener(this);
        mBaseAdapter.setLoadingView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_footer, null));
        mBaseAdapter.setEmptyView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_common_empty, (ViewGroup) mRecyclerView.getParent(), false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mBaseAdapter);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new SearchMoreModule(this))
                .inject(this);
        getData();
    }

    private void getData() {
        if (mPresenter != null) {
            mPresenter.getSearchResults(!isFromPerson, mSearchContent);
        }
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetworkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean show) {
        mRefreshLayout.setRefreshing(show);
        mProgressbar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSearchPersons(List<User> list) {
        mRefreshLayout.setRefreshing(false);
        mBaseAdapter.notifyDataChangedAfterLoadMore(list, true);
    }

    @Override
    public void showSearchQuestions(List<Question> list) {
        mRefreshLayout.setRefreshing(false);
        mBaseAdapter.notifyDataChangedAfterLoadMore(list, true);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
        if (stringId == R.string.toast_no_more) {
            mBaseAdapter.notifyDataChangedAfterLoadMore(false);
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onItemClick(View view, int i) {
        if (isFromPerson) {
            Intent intent = new Intent(getViewContext(), UserProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", (User) mData.get(i));
            intent.putExtras(bundle);
            getViewContext().startActivity(intent);
        } else {
            Intent intent = new Intent(getViewContext(), QuestionDetailActivity.class);
            intent.putExtra(Constant.KEY_QUESTION_ID, ((Question) mData.get(i)).getObjectId());
            getViewContext().startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mData.clear();
            mPresenter.resetSkipNum();
            getData();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getData();
    }
}
