package com.soloask.android.common.network.request.search;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-11-1.
 */

public class SearchRequest extends BaseRequest {
    @SerializedName("keyword")
    private String keyword;
    @SerializedName("offset")
    private int offset;
    @SerializedName("size")
    private int size;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
