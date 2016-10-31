package com.soloask.android.main.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-5.
 */
public class PersonAdapter extends BaseQuickAdapter<UserModel> {
    public PersonAdapter(List<UserModel> data) {
        super(R.layout.item_person_view, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserModel user) {
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.img_respondent);
        Glide.with(mContext)
                .load(user.getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        baseViewHolder.setText(R.id.tv_respondent_name, user.getUserName())
                .setText(R.id.tv_respondent_describe, user.getUserIntroduce())
                .setText(R.id.tv_respondent_followers, user.getUserTitle());
    }
}
