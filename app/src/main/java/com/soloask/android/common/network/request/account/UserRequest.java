package com.soloask.android.common.network.request.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lebron on 16-10-28.
 */

public class UserRequest extends BaseRequest {
    @SerializedName("id")
    private String id;

    @Override
    protected JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("token", getToken());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
