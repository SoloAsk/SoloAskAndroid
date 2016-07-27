package com.soloask.android.data.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Lebron on 2016/7/11.
 */
public class Question extends BmobObject implements Serializable {
    private String quesContent;
    private String quesVoiceURL;
    private Integer voiceTime;
    private Integer listenerNum;
    private Double quesPrice;
    private String askTime;
    private Boolean isFree;
    private Boolean isPublic;
    private Integer state;
    private User askUser;
    private User answerUser;
    private BmobRelation heardUser;

    public String getQuesContent() {
        return quesContent;
    }

    public void setQuesContent(String quesContent) {
        this.quesContent = quesContent;
    }

    public BmobRelation getHeardUser() {
        return heardUser;
    }

    public void setHeardUser(BmobRelation heardUser) {
        this.heardUser = heardUser;
    }

    public User getAnswerUser() {
        return answerUser;
    }

    public void setAnswerUser(User answerUser) {
        this.answerUser = answerUser;
    }

    public User getAskUser() {
        return askUser;
    }

    public void setAskUser(User askUser) {
        this.askUser = askUser;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getPub() {
        return isPublic;
    }

    public void setPub(Boolean pub) {
        isPublic = pub;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public Double getQuesPrice() {
        return quesPrice;
    }

    public void setQuesPrice(Double quesPrice) {
        this.quesPrice = quesPrice;
    }

    public Integer getListenerNum() {
        return listenerNum;
    }

    public void setListenerNum(Integer listenerNum) {
        this.listenerNum = listenerNum;
    }

    public Integer getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(Integer voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getQuesVoiceURL() {
        return quesVoiceURL;
    }

    public void setQuesVoiceURL(String quesVoiceURL) {
        this.quesVoiceURL = quesVoiceURL;
    }
}
