package com.soloask.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.adapter.HotAdapter;
import com.soloask.android.data.bmob.HotManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.NetworkManager;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lebron on 2016/6/21.
 */
public class HotFragment extends Fragment implements View.OnClickListener {
    private View mHotView;
    private RecyclerView mRecyclerView;
    private HotAdapter mHotAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mNetworkLayout;
    private MaterialProgressBar mProgressBar;
    private TextView mRetryView;
    private int mLastVisibleItem, mSkipNum;
    private List<Question> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHotView = inflater.inflate(R.layout.fragment_hot, container, false);
        return mHotView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(Constant.MSG_REFRESH_DATA);
    }

    private void initData(int actionType) {
        HotManager hotManager = new HotManager();
        hotManager.setOnGetHotListener(new HotManager.OnGetHotListener() {
            @Override
            public void onSuccess(List<Question> list) {
                mSkipNum += list.size();
                mDatas.addAll(list);
                mProgressBar.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(false);
                mHotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                mProgressBar.setVisibility(View.GONE);
                mNetworkLayout.setVisibility(View.VISIBLE);
                mRefreshLayout.setRefreshing(false);
            }
        });
        if (actionType == Constant.MSG_REFRESH_DATA) {
            mSkipNum = 0;
            hotManager.getHotList(mSkipNum);
        } else {
            Log.i("Lebron", mSkipNum + "");
            hotManager.getHotList(mSkipNum);
        }
    }


    private void setNetWorkLayout() {
        if (!NetworkManager.isNetworkValid(getActivity())) {
            mNetworkLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mNetworkLayout = (RelativeLayout) mHotView.findViewById(R.id.network_layout);
        mRefreshLayout = (SwipeRefreshLayout) mHotView.findViewById(R.id.refresh_layout_hot);
        mRecyclerView = (RecyclerView) mHotView.findViewById(R.id.recycler_hot_view);
        mProgressBar = (MaterialProgressBar) mHotView.findViewById(R.id.progressbar_loading);
        mRetryView = (TextView) mHotView.findViewById(R.id.tv_retry);
        mRetryView.setOnClickListener(this);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(Constant.MSG_REFRESH_DATA, 2000L);
            }
        });

        mHotAdapter = new HotAdapter(getActivity(), mDatas);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHotAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mHotAdapter.getItemCount() >= 10 && mLastVisibleItem + 1 == mHotAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(Constant.MSG_LOAD_MORE_DATA, 2000L);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        setNetWorkLayout();
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
                    mHotAdapter.notifyDataSetChanged();
                    initData(Constant.MSG_REFRESH_DATA);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_retry) {
            mNetworkLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mDatas.clear();
            mHotAdapter.notifyDataSetChanged();
            initData(Constant.MSG_REFRESH_DATA);
        }
    }
}
