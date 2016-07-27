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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.LoginActivity;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by Lebron on 2016/6/21.
 */
public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<Question> list;

    public HotAdapter(Context context, List datas) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        list = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_hot_view, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Question question = list.get(position);
            ((ItemViewHolder) holder).listenersView.setText(String.format(context.getResources().getString(R.string.format_listerers), question.getListenerNum()));
            ((ItemViewHolder) holder).questionView.setText(question.getQuesContent());
            ((ItemViewHolder) holder).respondentView.setText(question.getAnswerUser().getUserName() + " | " + question.getAnswerUser().getUserTitle());
            Glide.with(context)
                    .load(question.getAnswerUser().getUserIcon())
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPreferencesHelper.getPreferenceString(context, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
                        Intent intent = new Intent(context, QuestionDetailActivity.class);
                        intent.putExtra(Constant.KEY_QUESTION_ID, question.getObjectId());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        ((Activity) context).startActivityForResult(intent, Constant.CODE_REQUEST);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constant.TYPE_HEADER;
        } else if (position  == getItemCount() && getItemCount() >= 10) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout container, voiceLayout;
        TextView questionView;
        TextView respondentView;
        TextView priceView;
        TextView listenersView;
        TextView timeLengthView;

        public ItemViewHolder(View itemView, int viewType) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_respondent);
            questionView = (TextView) itemView.findViewById(R.id.tv_question);
            respondentView = (TextView) itemView.findViewById(R.id.tv_respondent_describe);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_amount);
            priceView = (TextView) itemView.findViewById(R.id.tv_voice_price);
            timeLengthView = (TextView) itemView.findViewById(R.id.tv_time_length);
            container = (RelativeLayout) itemView.findViewById(R.id.rl_hot_item);
            voiceLayout = (RelativeLayout) itemView.findViewById(R.id.rl_voice_container);

            timeLengthView.setVisibility(View.GONE);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
