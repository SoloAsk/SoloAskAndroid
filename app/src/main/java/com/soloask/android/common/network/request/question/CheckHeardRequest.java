package com.soloask.android.common.network.request.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-31.
 */

public class CheckHeardRequest extends BaseRequest {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("question_id")
    private String question_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
