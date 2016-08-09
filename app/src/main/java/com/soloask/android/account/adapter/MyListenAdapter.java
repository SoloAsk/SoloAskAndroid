package com.soloask.android.account.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyListenAdapter extends BaseQuickAdapter<Question> {

    public MyListenAdapter(List list) {
        super(R.layout.item_my_listen_view, list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Question question) {
        ImageView imageView = baseViewHolder.getView(R.id.img_respondent);
        baseViewHolder.setText(R.id.tv_respondent_name, question.getAnswerUser().getUserName());
        baseViewHolder.setText(R.id.tv_respondent_describe, question.getAnswerUser().getUserTitle());
        baseViewHolder.setText(R.id.tv_question, question.getQuesContent());
        baseViewHolder.setText(R.id.tv_time, RelativeDateFormat.format(question.getAskTime(), mContext));
        baseViewHolder.setText(R.id.tv_count, String.format(mContext.getResources().getString(R.string.format_listerers), question.getListenerNum()));
        Glide.with(mContext)
                .load(question.getAnswerUser().getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}
