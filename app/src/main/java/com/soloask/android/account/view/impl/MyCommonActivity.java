package com.soloask.android.account.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.adapter.MyAnswerAdapter;
import com.soloask.android.account.adapter.MyListenAdapter;
import com.soloask.android.account.adapter.MyQuestionAdapter;
import com.soloask.android.account.injection.MyCommonModule;
import com.soloask.android.account.presenter.MyCommonPresenter;
import com.soloask.android.account.view.MyCommonView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.question.view.impl.AnswerActivity;
import com.soloask.android.question.view.impl.QuestionDetailActivity;
import com.soloask.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyCommonActivity extends BaseActivity implements MyCommonView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.network_layout)
    RelativeLayout mNoNetworkLayout;
    @BindView(R.id.progressbar_loading_layout)
    RelativeLayout mLoadingLayout;
    @BindView(R.id.recycler_common_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout_mine)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    MyCommonPresenter mPresenter;

    private MyQuestionAdapter mQuestionAdapter;
    private MyAnswerAdapter mAnswerAdapter;
    private MyListenAdapter mListenAdapter;
    private BaseQuickAdapter mBaseAdapter;
    private List<QuestionModel> mDatas = new ArrayList<QuestionModel>();
    private int mFrom;
    private String mUserId;

    @OnClick(R.id.tv_retry)
    public void retry() {
        showNetworkError(false);
        getData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_common;
    }

    @Override
    protected void initViewsAndData() {
        mFrom = getIntent().getIntExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_QUESTION);
        mUserId = getIntent().getStringExtra("user");
        if (mUserId == null) {
            return;
        }

        initAdapter();

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrom == Constant.KEY_FROM_MY_ANSWER) {
                    setResult(Constant.KEY_FROM_MY_ANSWER);
                }
                finish();
            }
        });
    }

    private void getData() {
        if (mPresenter != null) {
            mPresenter.getQuestionList(mUserId, mFrom);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new MyCommonModule(this))
                .inject(this);
        getData();
    }

    private void initAdapter() {
        switch (mFrom) {
            case Constant.KEY_FROM_MY_QUESTION:
                this.setTitle(R.string.title_my_ask);
                mQuestionAdapter = new MyQuestionAdapter(mDatas);
                mBaseAdapter = mQuestionAdapter;
                break;
            case Constant.KEY_FROM_MY_ANSWER:
                this.setTitle(R.string.title_my_answer);
                mAnswerAdapter = new MyAnswerAdapter(mDatas);
                mBaseAdapter = mAnswerAdapter;
                break;
            case Constant.KEY_FROM_MY_LISTEN:
                this.setTitle(R.string.title_my_listen);
                mListenAdapter = new MyListenAdapter(mDatas);
                mBaseAdapter = mListenAdapter;
                break;
        }
        mBaseAdapter.setOnLoadMoreListener(this);
        mBaseAdapter.openLoadMore(10, true);
        mBaseAdapter.setOnRecyclerViewItemClickListener(this);
        mBaseAdapter.setLoadingView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_footer, null));
        mBaseAdapter.setEmptyView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_common_empty, (ViewGroup) mRecyclerView.getParent(), false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mBaseAdapter);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mDatas.clear();
            mPresenter.resetSkipNum();
            getData();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.KEY_FROM_MY_ANSWER) {
            onRefresh();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrom == Constant.KEY_FROM_MY_ANSWER) {
            setResult(Constant.KEY_FROM_MY_ANSWER);
        }
        super.onBackPressed();
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
    public void showMyQuestions(List<QuestionModel> questionList) {
        mRefreshLayout.setRefreshing(false);
        mBaseAdapter.notifyDataChangedAfterLoadMore(questionList, true);
    }

    @Override
    public int getDataSize() {
        return mDatas.size();
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
        switch (mFrom) {
            case Constant.KEY_FROM_MY_QUESTION:
            case Constant.KEY_FROM_MY_LISTEN:
                Intent intentQuestion = new Intent(getViewContext(), QuestionDetailActivity.class);
                intentQuestion.putExtra(Constant.KEY_QUESTION_ID, mDatas.get(i).getId());
                startActivity(intentQuestion);
                break;
            case Constant.KEY_FROM_MY_ANSWER:
                QuestionModel question = mDatas.get(i);
                if (question.getState().intValue() == Constant.STATUS_UNANSWERED) {
                    Intent intentAnswer = new Intent(getViewContext(), AnswerActivity.class);
                    intentAnswer.putExtra(Constant.KEY_QUESTION_ID, question.getId());
                    startActivityForResult(intentAnswer, Constant.CODE_REQUEST);
                } else if (question.getState().intValue() == Constant.STATUS_ANSWERED) {
                    Intent intentListen = new Intent(getViewContext(), QuestionDetailActivity.class);
                    intentListen.putExtra(Constant.KEY_QUESTION_ID, question.getId());
                    startActivity(intentListen);
                }
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getData();
    }
}
