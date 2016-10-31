package com.soloask.android.account.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lebron on 16-10-27.
 */

public class UserModel implements Serializable {
    @SerializedName("id")
    private String userId;
    @SerializedName("username")
    private String userName;
    @SerializedName("icon")
    private String userIcon;
    @SerializedName("title")
    private String userTitle;
    @SerializedName("introduction")
    private String userIntroduce;
    @SerializedName("price")
    private Double price;
    @SerializedName("earning")
    private Double earning;
    @SerializedName("income")
    private Double income;
    @SerializedName("answer_num")
    private Integer answerQuesNum;
    @SerializedName("ask_num")
    private Integer askQuesNum;
    @SerializedName("heard_num")
    private Integer heardQuesNum;
    @SerializedName("device_token")
    private String deviceToken;
    @SerializedName("third_party_uuid")
    private String thirdPartyId;
    @SerializedName("token")
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserIntroduce() {
        return userIntroduce;
    }

    public void setUserIntroduce(String userIntroduce) {
        this.userIntroduce = userIntroduce;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getEarning() {
        return earning;
    }

    public void setEarning(Double earning) {
        this.earning = earning;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Integer getAnswerQuesNum() {
        return answerQuesNum;
    }

    public void setAnswerQuesNum(Integer answerQuesNum) {
        this.answerQuesNum = answerQuesNum;
    }

    public Integer getAskQuesNum() {
        return askQuesNum;
    }

    public void setAskQuesNum(Integer askQuesNum) {
        this.askQuesNum = askQuesNum;
    }

    public Integer getHeardQuesNum() {
        return heardQuesNum;
    }

    public void setHeardQuesNum(Integer heardQuesNum) {
        this.heardQuesNum = heardQuesNum;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
