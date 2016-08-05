package com.soloask.android.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.soloask.android.util.Constant;

import java.util.List;

/**
 * Created by lebron on 16-8-5.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mDatas;

    public void clearDataList() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void addDataList(List<T> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public BaseAdapter(Context context, List<T> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mDatas = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() && getItemCount() >= 10) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
