package com.soloask.android.common.network.request.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-31.
 */

public class QuesDetailRequest extends BaseRequest {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
