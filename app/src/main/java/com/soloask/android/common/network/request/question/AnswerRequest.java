package com.soloask.android.common.network.request.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-11-1.
 */

public class AnswerRequest extends BaseRequest {
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("length")
    private int length;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
