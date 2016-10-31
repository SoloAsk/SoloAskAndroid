package com.soloask.android.common.network.request.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-31.
 */

public class WithDrawRequest extends BaseRequest {
    @SerializedName("paypal")
    private String paypal;
    @SerializedName("user_id")
    private String user_id;

    public String getPaypal() {
        return paypal;
    }

    public void setPaypal(String paypal) {
        this.paypal = paypal;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
