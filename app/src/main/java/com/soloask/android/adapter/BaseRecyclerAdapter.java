package com.soloask.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by LeBron on 2016/7/31.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mData;
    protected boolean noMore = true;

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = datas;
    }

    public void loadNoMore(boolean noMore) {
        this.noMore = noMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }
}
