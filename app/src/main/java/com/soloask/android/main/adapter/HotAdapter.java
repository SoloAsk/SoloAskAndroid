package com.soloask.android.main.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by lebron on 16-8-5.
 */
public class HotAdapter extends BaseQuickAdapter<Question> {

    public HotAdapter(List<Question> data) {
        super(R.layout.item_hot_view, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Question question) {
        baseViewHolder.setText(R.id.tv_question, question.getQuesContent());
        baseViewHolder.setText(R.id.tv_respondent_describe, question.getAnswerUser().getUserName() + " | " + question.getAnswerUser().getUserTitle());
        baseViewHolder.setText(R.id.tv_listeners_amount, String.format(mContext.getResources().getString(R.string.format_listerers), question.getListenerNum()));

        ImageView imageView = baseViewHolder.getView(R.id.img_respondent);
        Glide.with(mContext)
                .load(question.getAnswerUser().getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
