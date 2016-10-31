package com.soloask.android.common.network.request;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.MainApplication;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lebron on 16-10-27.
 */

public abstract class BaseRequest implements Serializable {
    @SerializedName("token")
    protected String token = SharedPreferencesHelper.getPreferenceString(MainApplication.getInstance(), Constant.KEY_TOKEN, "invalid token");

    public String getToken() {
        return SharedPreferencesHelper.getPreferenceString(MainApplication.getInstance(), Constant.KEY_TOKEN, "invalid token");
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected abstract JSONObject toJsonObject();
}
