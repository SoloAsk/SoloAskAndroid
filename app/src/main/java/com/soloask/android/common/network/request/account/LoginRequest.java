package com.soloask.android.common.network.request.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-28.
 */

public class LoginRequest extends BaseRequest {
    @SerializedName("username")
    private String userName;
    @SerializedName("third_id")
    private String thirdId;
    @SerializedName("icon")
    private String icon;
    @SerializedName("device_token")
    private String deviceToken;

    @Override
    protected JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("username", userName);
            json.put("third_id", thirdId);
            json.put("icon", icon);
            json.put("device_token", deviceToken);
            return json;
        } catch (Exception e) {
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
