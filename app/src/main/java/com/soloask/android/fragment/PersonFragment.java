package com.soloask.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.adapter.PersonAdapter;
import com.soloask.android.data.model.Movie;
import com.soloask.android.data.model.MoviesResponse;
import com.soloask.android.data.rest.ApiClient;
import com.soloask.android.data.rest.ApiInterface;
import com.soloask.android.util.Constant;
import com.soloask.android.util.NetworkManager;
import com.soloask.android.view.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lebron on 2016/6/22.
 */
public class PersonFragment extends Fragment implements View.OnClickListener {
    private View mPersonView;
    private RecyclerView mPersonsRecyclerView;
    private PersonAdapter mPersonAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mNetworkLayout;
    private MaterialProgressBar mProgressBar;
    private TextView mRetryView;
    private int mLastVisibleItem;
    private ApiInterface mApiInterface;
    private Call<MoviesResponse> mCall;
    private List<Movie> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPersonView = inflater.inflate(R.layout.fragment_person, container, false);
        return mPersonView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mDatas = new ArrayList();
        mRefreshLayout = (SwipeRefreshLayout) mPersonView.findViewById(R.id.refresh_layout_person);
        mPersonsRecyclerView = (RecyclerView) mPersonView.findViewById(R.id.recycler_person_view);
        mProgressBar = (MaterialProgressBar) mPersonView.findViewById(R.id.progressbar_loading);
        mNetworkLayout = (RelativeLayout) mPersonView.findViewById(R.id.network_layout);
        mRetryView = (TextView) mPersonView.findViewById(R.id.tv_retry);
        mRetryView.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mPersonsRecyclerView.setLayoutManager(mLayoutManager);
        mPersonAdapter = new PersonAdapter(getActivity(), mDatas);
        mPersonsRecyclerView.setAdapter(mPersonAdapter);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        setNetWorkLayout();
        addListener();
    }

    private void addListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(Constant.MSG_REFRESH_DATA, 500L);
            }
        });

        mPersonsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mPersonAdapter.getItemCount()) {
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

    private void setNetWorkLayout() {
        if (!NetworkManager.isNetworkValid(getActivity())) {
            mNetworkLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (mApiInterface == null) {
            mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        }
        if (mCall == null) {
            mCall = mApiInterface.getTopRatedMovies("7e8f60e325cd06e164799af1e317d7a7");
        }
        mCall.clone().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                mDatas.addAll(response.body().getResults());
                mProgressBar.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(false);
                mPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mNetworkLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MSG_LOAD_MORE_DATA:
                    initData();
                    break;
                case Constant.MSG_REFRESH_DATA:
                    mDatas.clear();
                    mPersonAdapter.notifyDataSetChanged();
                    initData();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_retry) {
            mNetworkLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            initData();
        }
    }
}
