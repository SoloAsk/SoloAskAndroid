package com.soloask.android.search.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.account.model.UserModel;

import java.util.List;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchPersonAdapter extends BaseQuickAdapter<UserModel> {

    public SearchPersonAdapter(List list) {
        super(R.layout.item_search_person_view, list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserModel user) {
        ImageView mUserIcon = baseViewHolder.getView(R.id.img_user);
        baseViewHolder.setText(R.id.tv_user_name, user.getUserName());
        baseViewHolder.setText(R.id.tv_user_describe, user.getUserTitle());
        Glide.with(mContext)
                .load(user.getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mUserIcon);
    }
}
