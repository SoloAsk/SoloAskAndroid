package com.soloask.android.question.model;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.account.model.UserModel;

import java.io.Serializable;

/**
 * Created by lebron on 16-10-27.
 */

public class QuestionModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("voice_url")
    private String voiceURL;
    @SerializedName("voice_length")
    private Integer voiceTime;
    @SerializedName("listener_num")
    private Integer listenerNum;
    @SerializedName("price")
    private Double price;
    @SerializedName("create_time")
    private String askTime;
    @SerializedName("public")
    private int isPublic;
    @SerializedName("state")
    private Integer state;
    @SerializedName("ask_user")
    private UserModel askUser;
    @SerializedName("answer_user")
    private UserModel answerUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoiceURL() {
        return voiceURL;
    }

    public void setVoiceURL(String voiceURL) {
        this.voiceURL = voiceURL;
    }

    public Integer getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(Integer voiceTime) {
        this.voiceTime = voiceTime;
    }

    public Integer getListenerNum() {
        return listenerNum;
    }

    public void setListenerNum(Integer listenerNum) {
        this.listenerNum = listenerNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public Boolean getPublic() {
        return isPublic == 0 ? false : true;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = (aPublic == true ? 1 : 0);
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public UserModel getAskUser() {
        return askUser;
    }

    public void setAskUser(UserModel askUser) {
        this.askUser = askUser;
    }

    public UserModel getAnswerUser() {
        return answerUser;
    }

    public void setAnswerUser(UserModel answerUser) {
        this.answerUser = answerUser;
    }
}
