package com.soloask.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;

import java.util.List;

/**
 * Created by Lebron on 2016/6/30.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<Question> mDatas;

    public void clearDataList() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void addDataList(List<Question> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public BaseAdapter(Context context, List<Question> list) {
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
