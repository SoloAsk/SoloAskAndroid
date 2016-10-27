package com.soloask.android.common.network.request.main;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-27.
 */

public class HotRequest extends BaseRequest {
    @SerializedName("offset")
    private int offset;

    @SerializedName("size")
    private int size;

    @Override
    protected JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("offset", offset);
            json.put("size", size);
            json.put("token", getToken());
            return json;
        } catch (Exception e) {
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
