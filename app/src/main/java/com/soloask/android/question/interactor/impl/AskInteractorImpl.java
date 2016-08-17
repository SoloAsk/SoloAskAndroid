package com.soloask.android.question.interactor.impl;

import com.soloask.android.data.bmob.AskManager;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.question.interactor.AskInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-11.
 */
public class AskInteractorImpl implements AskInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private UserManager mQuestionerManager;
    private UserManager mRespondentManager;
    private AskManager mAskManager;
    private AskManager mQuestionsManager;

    @Inject
    public AskInteractorImpl() {
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public int getSkipNum() {
        return mSkipNum;
    }

    @Override
    public void setSkipNum(int skipNum) {
        mSkipNum += skipNum;
    }

    @Override
    public void resetSkipNum() {
        mSkipNum = 0;
    }

    @Override
    public void getCurrentUser(String userId, final AskResponseListener listener) {
        if (mQuestionerManager == null) {
            mQuestionerManager = new UserManager();
        }
        mQuestionerManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                listener.onGetCurrentUserSuccess(user);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mQuestionerManager.getUserInfo(userId);
    }

    @Override
    public void getRespondentInfo(String userId, final AskResponseListener listener) {
        if (mRespondentManager == null) {
            mRespondentManager = new UserManager();
        }
        mRespondentManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                listener.onGetRespondentInfoSucc(user);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mRespondentManager.getUserInfo(userId);
    }

    @Override
    public void getRespondentRelatedQuestions(User user, final AskResponseListener listener) {
        if (mQuestionsManager == null) {
            mQuestionsManager = new AskManager();
        }
        mQuestionsManager.setOnRespondentQuestionListener(new AskManager.OnRespondentQuestionListener() {
            @Override
            public void onSuccess(List<Question> list) {
                listener.onGetRelatedQuestionsSucc(list);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mQuestionsManager.getHistoryQuestions(mSkipNum, user);
    }

    @Override
    public void askQuestion(Question question, final AskResponseListener listener) {
        if (mAskManager == null) {
            mAskManager = new AskManager();
        }
        mAskManager.setOnAskQuestionListener(new AskManager.OnAskQuestionListener() {
            @Override
            public void onSuccess(String objectId) {
                listener.onAskQuestionSucc(objectId);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mAskManager.askQuestion(question);
    }
}
