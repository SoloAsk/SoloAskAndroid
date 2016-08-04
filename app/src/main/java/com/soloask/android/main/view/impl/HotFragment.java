package com.soloask.android.main.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.adapter.HotAdapter;
import com.soloask.android.common.base.BaseFragment;
import com.soloask.android.data.model.Question;
import com.soloask.android.main.module.HotModule;
import com.soloask.android.main.presenter.HotPresenter;
import com.soloask.android.main.view.HotView;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 16-8-4.
 */
public class HotFragment extends BaseFragment implements HotView {
    @BindView(R.id.recycler_hot_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout_hot)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressbar_loading)
    MaterialProgressBar mProgressBar;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetworkLayout;
    @BindView(R.id.tv_retry)
    TextView mRetryView;

    @OnClick(R.id.tv_retry)
    public void retry() {
        if (mPrestenter != null) {
            showNetworkError(false);
            getHotQuestions();
        }
    }

    @Inject
    HotPresenter mPrestenter;

    private HotAdapter mHotAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Question> mQuestionList = new ArrayList<>();
    private int mLastVisibleItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity())
                .getAppComponent()
                .plus(new HotModule(this))
                .inject(this);
    }

    private void getHotQuestions() {
        if (mPrestenter != null) {
            mPrestenter.getQuestionList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPrestenter.start();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initViewsAndData(View view) {
        mHotAdapter = new HotAdapter(getActivity(), mQuestionList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHotAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mHotAdapter.getItemCount() >= 10 && mLastVisibleItem + 1 == mHotAdapter.getItemCount()) {
                    getHotQuestions();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPrestenter != null) {
                    mQuestionList.clear();
                    mHotAdapter.notifyDataSetChanged();
                    mPrestenter.resetSkipNum();
                    getHotQuestions();
                }
            }
        });

    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetworkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPopularLives(List<Question> questionList) {
        mQuestionList.addAll(questionList);
        mHotAdapter.notifyDataSetChanged();
    }

    @Override
    public int getDataSize() {
        return mQuestionList.size();
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }
}
