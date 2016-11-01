package com.soloask.android.common.network.response.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.response.BaseResponse;
import com.soloask.android.question.model.QuestionModel;

/**
 * Created by lebron on 16-10-31.
 */

public class QuestionResponse extends BaseResponse {
    @SerializedName("data")
    private QuestionModel mQuestionModel;

    public QuestionModel getQuestionModel() {
        return mQuestionModel;
    }
}
