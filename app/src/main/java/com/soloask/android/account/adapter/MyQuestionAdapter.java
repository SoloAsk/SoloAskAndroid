package com.soloask.android.account.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.data.bmob.MineManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyQuestionAdapter extends BaseQuickAdapter<Question> {
    private int status;

    public MyQuestionAdapter(List list) {
        super(R.layout.item_my_question_view, list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Question question) {
        ImageView imageView = baseViewHolder.getView(R.id.img_questioner);
        TextView statusView = baseViewHolder.getView(R.id.tv_status);
        TextView listenersView = baseViewHolder.getView(R.id.tv_listeners_info);
        baseViewHolder.setText(R.id.tv_questioner_name, question.getAnswerUser().getUserName());
        baseViewHolder.setText(R.id.tv_question, question.getQuesContent());
        baseViewHolder.setText(R.id.tv_answered_time, RelativeDateFormat.format(question.getAskTime(), mContext));
        baseViewHolder.setText(R.id.tv_cost, String.format(mContext.getString(R.string.format_dollar), question.getQuesPrice()));
        Glide.with(mContext)
                .load(question.getAnswerUser().getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        statusView.setVisibility(View.VISIBLE);
        status = question.getState().intValue();
        if (status != Constant.STATUS_TIMEOUT && RelativeDateFormat.isTimeOut(question.getAskTime())) {
            Log.i("MyQuestion", " time out");
            MineManager mineManager = new MineManager();
            mineManager.dealTimeOut(question, Constant.STATUS_TIMEOUT);
            status = Constant.STATUS_TIMEOUT;
        }
        statusView.setText(Constant.ARRAY_STATUS[status]);
        if (status == Constant.STATUS_ANSWERED) {
            listenersView.setVisibility(View.VISIBLE);
            listenersView.setText(String.format(mContext.getString(R.string.format_listerers), question.getListenerNum()));
        } else {
            listenersView.setVisibility(View.GONE);
        }
    }
}
