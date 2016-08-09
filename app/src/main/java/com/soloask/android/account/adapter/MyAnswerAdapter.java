package com.soloask.android.account.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyAnswerAdapter extends BaseQuickAdapter<Question> {

    public MyAnswerAdapter(List list) {
        super(R.layout.item_my_question_view, list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Question question) {
        ImageView imageView = baseViewHolder.getView(R.id.img_questioner);
        TextView statusView = baseViewHolder.getView(R.id.tv_status);
        TextView listenersView = baseViewHolder.getView(R.id.tv_listeners_info);
        baseViewHolder.setText(R.id.tv_questioner_name, question.getAskUser().getUserName());
        baseViewHolder.setText(R.id.tv_question, question.getQuesContent());
        baseViewHolder.setText(R.id.tv_answered_time, RelativeDateFormat.format(question.getAskTime(), mContext));
        baseViewHolder.setText(R.id.tv_cost, String.format(mContext.getString(R.string.format_dollar), question.getQuesPrice()));
        Glide.with(mContext)
                .load(question.getAskUser().getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        statusView.setVisibility(View.VISIBLE);
        statusView.setText(Constant.ARRAY_STATUS[question.getState().intValue()]);

        if (question.getState().intValue() == Constant.STATUS_ANSWERED) {
            listenersView.setVisibility(View.VISIBLE);
            listenersView.setText(String.format(mContext.getString(R.string.format_listerers), question.getListenerNum()));
        } else {
            listenersView.setVisibility(View.GONE);
        }
    }

}
