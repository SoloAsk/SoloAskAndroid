package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.activity.LoginActivity;
import com.soloask.android.activity.UserProfileActivity;
import com.soloask.android.data.model.Movie;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by Lebron on 2016/6/22.
 */
public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<Movie> mDatas;

    public PersonAdapter(Context context, List<Movie> datas) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).describeView.setText(mDatas.get(position).getOriginalTitle());
            ((ItemViewHolder) holder).nameView.setText(mDatas.get(position).getTitle());
            ((ItemViewHolder) holder).followerView.setText(mDatas.get(position).getReleaseDate());
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
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPreferencesHelper.getPreferenceBoolean(context, Constant.KEY_IS_LOGINED, false)) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        ((Activity) context).startActivityForResult(intent, Constant.CODE_REQUEST);
                    }

                }
            });
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
