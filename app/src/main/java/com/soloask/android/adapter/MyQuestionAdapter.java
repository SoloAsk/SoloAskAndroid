package com.soloask.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.bmob.MineManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyQuestionAdapter extends BaseAdapter {
    private int status;

    public MyQuestionAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_my_question_view, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Question question = mDatas.get(position);
            Glide.with(mContext)
                    .load(question.getAnswerUser().getUserIcon())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).nameView.setText(question.getAnswerUser().getUserName());
            ((ItemViewHolder) holder).timeView.setText(RelativeDateFormat.format(question.getAskTime(), mContext));
            ((ItemViewHolder) holder).priceView.setText(String.format(mContext.getString(R.string.format_dollar), question.getQuesPrice()));
            ((ItemViewHolder) holder).questionView.setText(question.getQuesContent());
            status = question.getState().intValue();
            if (status != Constant.STATUS_REFUND && RelativeDateFormat.isTimeOut(question.getAskTime())) {
                Log.i("MyQuestion", " time out");
                MineManager mineManager = new MineManager();
                mineManager.dealTimeOut(question, Constant.STATUS_REFUND);
                status = Constant.STATUS_REFUND;
            }
            ((ItemViewHolder) holder).statusView.setText(Constant.ARRAY_STATUS[status]);

            if (status == Constant.STATUS_ANSWERED) {
                ((ItemViewHolder) holder).listenersView.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).listenersView.setText(String.format(mContext.getString(R.string.format_listerers), question.getListenerNum()));
            } else {
                ((ItemViewHolder) holder).listenersView.setVisibility(View.GONE);
            }
            ((ItemViewHolder) holder).mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                    intent.putExtra(Constant.KEY_QUESTION_ID, question.getObjectId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView priceView;
        TextView statusView;
        TextView questionView;
        TextView timeView;
        TextView listenersView;
        LinearLayout mContainer;


        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_questioner);
            nameView = (TextView) itemView.findViewById(R.id.tv_questioner_name);
            priceView = (TextView) itemView.findViewById(R.id.tv_cost);
            statusView = (TextView) itemView.findViewById(R.id.tv_status);
            questionView = (TextView) itemView.findViewById(R.id.tv_question);
            timeView = (TextView) itemView.findViewById(R.id.tv_answered_time);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_info);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_my_question_item);
            statusView.setVisibility(View.VISIBLE);
        }
    }
}
