package com.soloask.android.common.network.request.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.request.BaseRequest;

import org.json.JSONObject;

/**
 * Created by lebron on 16-11-1.
 */

public class AskRequest extends BaseRequest {
    @SerializedName("content")
    private String content;
    @SerializedName("price")
    private double price;
    @SerializedName("public")
    private int isPublic;
    @SerializedName("answer_uuid")
    private String answer_uuid;
    @SerializedName("ask_uuid")
    private String ask_uuid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getAnswer_uuid() {
        return answer_uuid;
    }

    public void setAnswer_uuid(String answer_uuid) {
        this.answer_uuid = answer_uuid;
    }

    public String getAsk_uuid() {
        return ask_uuid;
    }

    public void setAsk_uuid(String ask_uuid) {
        this.ask_uuid = ask_uuid;
    }

    @Override
    protected JSONObject toJsonObject() {
        return null;
    }
}
