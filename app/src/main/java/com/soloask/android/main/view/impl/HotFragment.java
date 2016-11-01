package com.soloask.android.main.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.view.impl.LoginActivity;
import com.soloask.android.common.base.BaseFragment;
import com.soloask.android.main.adapter.HotAdapter;
import com.soloask.android.main.module.HotModule;
import com.soloask.android.main.presenter.HotPresenter;
import com.soloask.android.main.view.HotView;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.question.view.impl.QuestionDetailActivity;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 16-8-4.
 */
public class HotFragment extends BaseFragment implements HotView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.recycler_hot_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout_hot)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressbar_loading_layout)
    RelativeLayout mLoadingLayout;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetworkLayout;
    @BindView(R.id.tv_retry)
    TextView mRetryView;
    @Inject
    HotPresenter mPrestenter;
    private HotAdapter mHotAdapter;
    private List<QuestionModel> mQuestionList = new ArrayList<>();

    @OnClick(R.id.tv_retry)
    public void retry() {
        showNetworkError(false);
        getHotQuestions();
    }

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
        onRefresh();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initViewsAndData(View view) {
        mHotAdapter = new HotAdapter(mQuestionList);
        mHotAdapter.setOnLoadMoreListener(this);
        mHotAdapter.openLoadMore(10, true);
        mHotAdapter.setOnRecyclerViewItemClickListener(this);
        mHotAdapter.setLoadingView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_footer, null));
        mHotAdapter.setEmptyView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_common_empty, (ViewGroup) mRecyclerView.getParent(), false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mHotAdapter);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
        if (stringId == R.string.toast_no_more) {
            mHotAdapter.notifyDataChangedAfterLoadMore(false);
        }
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetworkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean show) {
        mRefreshLayout.setRefreshing(show);
        mLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHotQuestions(List<QuestionModel> questionList) {
        mRefreshLayout.setRefreshing(false);
        mHotAdapter.notifyDataChangedAfterLoadMore(questionList, true);
    }

    @Override
    public int getDataSize() {
        return mQuestionList.size();
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (SharedPreferencesHelper.getPreferenceString(getViewContext(), Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
            Intent intent = new Intent(getViewContext(), QuestionDetailActivity.class);
            intent.putExtra(Constant.KEY_QUESTION_ID, String.valueOf(mQuestionList.get(position).getId()));
            getViewContext().startActivity(intent);
        } else {
            Intent intent = new Intent(getViewContext(), LoginActivity.class);
            ((Activity) getViewContext()).startActivityForResult(intent, Constant.CODE_REQUEST);
        }
    }

    @Override
    public void onRefresh() {
        if (mPrestenter != null) {
            mQuestionList.clear();
            mPrestenter.resetSkipNum();
            getHotQuestions();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getHotQuestions();
    }
}
