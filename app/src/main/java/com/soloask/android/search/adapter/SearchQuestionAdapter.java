package com.soloask.android.search.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;
import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchQuestionAdapter extends BaseQuickAdapter<Question> {

    public SearchQuestionAdapter(List list) {
        super(R.layout.item_search_question_view, list);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, Question question) {
        baseViewHolder.setText(R.id.tv_search_question_content, question.getQuesContent());
        baseViewHolder.setText(R.id.tv_search_question_user_info, String.format(mContext.getString(R.string.format_respondent), question.getAnswerUser().getUserName()));
    }
}
