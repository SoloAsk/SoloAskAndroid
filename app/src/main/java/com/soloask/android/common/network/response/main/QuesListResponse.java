package com.soloask.android.common.network.response.main;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.response.BaseResponse;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-10-27.
 */

public class QuesListResponse extends BaseResponse {
    @SerializedName("data")
    private List<QuestionModel> questionList;


    public List<QuestionModel> getQuestionList() {
        return questionList;
    }
}
