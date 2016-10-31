package com.soloask.android.common.network.request.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-10-28.
 */

public class UpdateUserRequest extends BaseRequest {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("introduction")
    private String introduction;
    @SerializedName("price")
    private double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
