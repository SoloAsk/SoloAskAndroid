package com.soloask.android.main.view.impl;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.view.impl.LoginActivity;
import com.soloask.android.common.base.BaseFragment;
import com.soloask.android.data.model.User;
import com.soloask.android.main.adapter.PersonAdapter;
import com.soloask.android.main.module.PersonModule;
import com.soloask.android.main.presenter.PersonPresenter;
import com.soloask.android.main.view.PersonView;
import com.soloask.android.question.view.impl.UserProfileActivity;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 16-8-5.
 */
public class PersonFragment extends BaseFragment implements PersonView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.refresh_layout_person)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_person_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressbar_loading)
    MaterialProgressBar mProgressBar;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetworkLayout;
    @BindView(R.id.tv_retry)
    TextView mRetryView;

    @Inject
    PersonPresenter mPresenter;

    private PersonAdapter mPersonAdapter;
    private List<User> mPersonList = new ArrayList<>();

    @OnClick(R.id.tv_retry)
    public void retry() {
        showNetworkError(false);
        getPersonList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity())
                .getAppComponent()
                .plus(new PersonModule(this))
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void getPersonList() {
        if (mPresenter != null) {
            mPresenter.getPersonList();
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initViewsAndData(View view) {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mPersonAdapter = new PersonAdapter(mPersonList);
        mPersonAdapter.setOnLoadMoreListener(this);
        mPersonAdapter.setOnRecyclerViewItemClickListener(this);
        mPersonAdapter.setLoadingView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_footer, null));
        mPersonAdapter.setEmptyView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_common_empty, (ViewGroup) mRecyclerView.getParent(), false));
        mPersonAdapter.openLoadMore(10, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mPersonAdapter);
    }

    @Override
    public void onRefresh() {
        mPersonList.clear();
        mPresenter.resetSkipNum();
        getPersonList();
    }

    @Override
    public void onLoadMoreRequested() {
        getPersonList();
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
        if (stringId == R.string.toast_no_more) {
            mPersonAdapter.notifyDataChangedAfterLoadMore(false);
        }
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetworkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean show) {
        mRefreshLayout.setRefreshing(show);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPopularPersons(List<User> personList) {
        mRefreshLayout.setRefreshing(false);
        mPersonAdapter.notifyDataChangedAfterLoadMore(personList, true);
    }

    @Override
    public int getDataSize() {
        return mPersonList.size();
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (SharedPreferencesHelper.getPreferenceString(getViewContext(), Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
            Intent intent = new Intent(getViewContext(), UserProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mPersonList.get(position));
            intent.putExtras(bundle);
            getViewContext().startActivity(intent);
        } else {
            Intent intent = new Intent(getViewContext(), LoginActivity.class);
            ((Activity) getViewContext()).startActivityForResult(intent, Constant.CODE_REQUEST);
        }
    }
}
