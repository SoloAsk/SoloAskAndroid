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
}
