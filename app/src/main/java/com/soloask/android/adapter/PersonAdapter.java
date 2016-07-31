package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.LoginActivity;
import com.soloask.android.activity.UserProfileActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.MaterialProgressBar;

import java.util.List;

/**
 * Created by Lebron on 2016/6/22.
 */
public class PersonAdapter extends BaseRecyclerAdapter<User> {

    public PersonAdapter(Context context, List<User> datas) {
        super(context, datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_person_view, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Glide.with(mContext)
                    .load(mData.get(position).getUserIcon())
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).describeView.setText(mData.get(position).getUserIntroduce());
            ((ItemViewHolder) holder).nameView.setText(mData.get(position).getUserName());
            ((ItemViewHolder) holder).followerView.setText(mData.get(position).getUserTitle());
            ((ItemViewHolder) holder).mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPreferencesHelper.getPreferenceString(mContext, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
                        Intent intent = new Intent(mContext, UserProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mData.get(position));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        ((Activity) mContext).startActivityForResult(intent, Constant.CODE_REQUEST);
                    }

                }
            });
        } else {
            ((FooterViewHolder) holder).progressBar.setVisibility(noMore ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView describeView;
        TextView followerView;
        RelativeLayout mContainer;


        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_respondent);
            nameView = (TextView) itemView.findViewById(R.id.tv_respondent_name);
            describeView = (TextView) itemView.findViewById(R.id.tv_respondent_describe);
            followerView = (TextView) itemView.findViewById(R.id.tv_respondent_followers);
            mContainer = (RelativeLayout) itemView.findViewById(R.id.rl_person_item);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        MaterialProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (MaterialProgressBar) itemView.findViewById(R.id.pgb_footer_more);
        }
    }
}
