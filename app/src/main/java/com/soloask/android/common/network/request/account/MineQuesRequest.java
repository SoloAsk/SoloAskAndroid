package com.soloask.android.common.network.request.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-31.
 */

public class MineQuesRequest extends BaseRequest {
    @SerializedName("type")
    private String type;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("offset")
    private int offset;
    @SerializedName("size")
    private int size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
