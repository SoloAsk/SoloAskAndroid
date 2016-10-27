package com.soloask.android.common.network.request;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lebron on 16-10-27.
 */

public abstract class BaseRequest implements Serializable {
    @SerializedName("token")
    protected String token;

    public String getToken() {
        return "token";
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected abstract JSONObject toJsonObject();
}
