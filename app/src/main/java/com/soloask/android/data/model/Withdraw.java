package com.soloask.android.data.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lebron on 2016/7/14.
 */
public class Withdraw extends BmobObject {
    private User user;
    private Boolean dealed;
    private String applyTime;
    private String paypalAccount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getDealed() {
        return dealed;
    }

    public void setDealed(Boolean dealed) {
        this.dealed = dealed;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }
}
