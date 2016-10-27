package com.soloask.android.common.network.response.main;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.response.BaseResponse;
import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by lebron on 16-10-27.
 */

public class HotResponse extends BaseResponse {
    @SerializedName("data")
    private List<Question> questionList;


    public List<Question> getQuestionList() {
        return questionList;
    }
}
