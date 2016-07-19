package com.soloask.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.util.Constant;

import java.util.List;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchQuestionAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Question> mDatas;

    public SearchQuestionAdapter(Context context, List list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_search_question_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Question question = mDatas.get(position);
        ((ItemViewHolder) holder).questionView.setText(question.getQuesContent());
        ((ItemViewHolder) holder).repondentInfo.setText(question.getQuesPrice() + "");
        ((ItemViewHolder) holder).mQuestionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra(Constant.KEY_QUESTION_ID, question.getObjectId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView questionView;
        TextView repondentInfo;
        LinearLayout mQuestionContainer;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mQuestionContainer = (LinearLayout) itemView.findViewById(R.id.ll_search_question);
            questionView = (TextView) itemView.findViewById(R.id.tv_search_question_content);
            repondentInfo = (TextView) itemView.findViewById(R.id.tv_search_question_user_info);
        }
    }
}
