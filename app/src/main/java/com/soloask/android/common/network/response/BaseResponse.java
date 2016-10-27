package com.soloask.android.common.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lebron on 16-10-27.
 */

public abstract class BaseResponse implements Serializable {
    @SerializedName("code")
    protected int code;
    @SerializedName("message")
    protected String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
