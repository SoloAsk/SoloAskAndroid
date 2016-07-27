package com.soloask.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyListenAdapter extends BaseAdapter {

    public MyListenAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_my_listen_view, parent, false), mContext);
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
            ((ItemViewHolder) holder).titleView.setText(question.getAnswerUser().getUserTitle());
            ((ItemViewHolder) holder).timeView.setText(RelativeDateFormat.format(question.getAskTime(),mContext));
            ((ItemViewHolder) holder).questionView.setText(question.getQuesContent());
            ((ItemViewHolder) holder).listenersView.setText(String.format(mContext.getResources().getString(R.string.format_listerers), question.getListenerNum()));
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
        TextView titleView;
        TextView questionView;
        TextView timeView;
        TextView listenersView;
        LinearLayout mContainer;


        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_respondent);
            nameView = (TextView) itemView.findViewById(R.id.tv_respondent_name);
            titleView = (TextView) itemView.findViewById(R.id.tv_respondent_describe);
            questionView = (TextView) itemView.findViewById(R.id.tv_question);
            timeView = (TextView) itemView.findViewById(R.id.tv_time);
            listenersView = (TextView) itemView.findViewById(R.id.tv_count);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_my_listen_item);

        }
    }
}
