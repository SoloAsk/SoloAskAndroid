package com.soloask.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.UserProfileActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.view.BoundImageView;

import java.util.List;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchPersonAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<User> mDatas;

    public SearchPersonAdapter(Context context, List list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_search_person_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User user = (User) mDatas.get(position);
        Glide.with(mContext)
                .load(user.getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ItemViewHolder) holder).mUserIcon);
        ((ItemViewHolder) holder).mUserNameView.setText(user.getUserName());
        ((ItemViewHolder) holder).mUserInfoView.setText(user.getUserTitle());
        ((ItemViewHolder) holder).mUserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mUserContainer;
        BoundImageView mUserIcon;
        TextView mUserNameView;
        TextView mUserInfoView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mUserContainer = (LinearLayout) itemView.findViewById(R.id.ll_user_profile);
            mUserIcon = (BoundImageView) itemView.findViewById(R.id.img_user);
            mUserNameView = (TextView) itemView.findViewById(R.id.tv_user_name);
            mUserInfoView = (TextView) itemView.findViewById(R.id.tv_user_describe);
        }
    }
}
