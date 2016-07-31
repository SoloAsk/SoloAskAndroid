package com.soloask.android.data.model;


import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lebron on 2016/7/11.
 */
public class User extends BmobObject implements Serializable {
    private String userId;
    private String userName;
    private String userIcon;
    private String userTitle;
    private String userIntroduce;
    private Double askPrice;
    private Double earning;
    private Double income;
    private String paypalAccount;
    private Integer answerQuesNum;
    private Integer askQuesNum;
    private Integer heardQuesNum;
    private String deviceToken;

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

    public Double getUserPrice() {
        return askPrice;
    }

    public void setUserPrice(Double userPrice) {
        askPrice = userPrice;
    }

    public Double getUserEarned() {
        return earning;
    }

    public void setUserEarned(Double userEarned) {
        this.earning = userEarned;
    }

    public Double getUserIncome() {
        return income;
    }

    public void setUserIncome(Double userIncome) {
        income = userIncome;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public Integer getHeardQuesNum() {
        return heardQuesNum;
    }

    public void setHeardQuesNum(Integer heardQuesNum) {
        this.heardQuesNum = heardQuesNum;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return this.userId.equals(u.userId);
        }
        return super.equals(o);
    }
}
