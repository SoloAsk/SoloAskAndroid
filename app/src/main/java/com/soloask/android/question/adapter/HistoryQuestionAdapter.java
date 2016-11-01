package com.soloask.android.question.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.util.RelativeDateFormat;
import com.soloask.android.view.BoundImageView;

import java.util.List;


/**
 * Created by Lebron on 2016/6/21.
 */
public class HistoryQuestionAdapter extends BaseQuickAdapter<QuestionModel> {

    public HistoryQuestionAdapter(List<QuestionModel> list) {
        super(R.layout.item_user_voice_history, list);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, QuestionModel question) {
        BoundImageView questionerIcon = baseViewHolder.getView(R.id.img_questioner);
        BoundImageView respondentIcon = baseViewHolder.getView(R.id.img_respondent);
        TextView listenersView = baseViewHolder.getView(R.id.tv_listeners_info);
        Glide.with(mContext)
                .load(question.getAskUser().getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(questionerIcon);
        Glide.with(mContext)
                .load(question.getAnswerUser().getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(respondentIcon);
        baseViewHolder.setText(R.id.tv_questioner_question, question.getContent());
        baseViewHolder.setText(R.id.tv_question_price, String.format(mContext.getString(R.string.format_dollar), question.getPrice()));
        baseViewHolder.setText(R.id.tv_answered_time, (RelativeDateFormat.format(question.getAskTime(), mContext)));
        baseViewHolder.setText(R.id.tv_time_length, String.format(mContext.getString(R.string.format_second), question.getVoiceTime()));
        listenersView.setVisibility(View.VISIBLE);
        listenersView.setText(String.format(mContext.getString(R.string.format_listerers), question.getListenerNum()));
    }

}
