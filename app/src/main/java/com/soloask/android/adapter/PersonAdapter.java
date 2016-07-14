package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.util.List;

/**
 * Created by Lebron on 2016/6/22.
 */
public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<User> mDatas;

    public PersonAdapter(Context context, List<User> datas) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_person_view, parent, false), context);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Glide.with(context)
                    .load(mDatas.get(position).getUserIcon())
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).describeView.setText(mDatas.get(position).getUserIntroduce());
            ((ItemViewHolder) holder).nameView.setText(mDatas.get(position).getUserName());
            ((ItemViewHolder) holder).followerView.setText(mDatas.get(position).getUserTitle());
            ((ItemViewHolder) holder).mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPreferencesHelper.getPreferenceString(context, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mDatas.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        ((Activity) context).startActivityForResult(intent, Constant.CODE_REQUEST);
                    }

                }
            });
        }
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
            followerView.setText(context.getResources().getQuantityString(R.plurals.answer_and_earn, 1, 30));
            mContainer = (RelativeLayout) itemView.findViewById(R.id.rl_person_item);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
