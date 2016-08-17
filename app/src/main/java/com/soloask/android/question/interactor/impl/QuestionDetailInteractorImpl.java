package com.soloask.android.question.interactor.impl;

import com.soloask.android.data.bmob.QuestionDetailManager;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.question.interactor.QuestionDetailInteractor;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-10.
 */
public class QuestionDetailInteractorImpl implements QuestionDetailInteractor {
    private UserManager mUserManager;
    private QuestionDetailManager mQuestionManager;

    @Inject
    public QuestionDetailInteractorImpl() {
    }

    @Override
    public void getCurrentUser(String userId, final QuestionDetailResponseListener listener) {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
        mUserManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                listener.OnGetCurrentUserSuccess(user);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mUserManager.getUserInfo(userId);
    }

    @Override
    public void checkUserHeard(Question question, User user, final QuestionDetailResponseListener listener) {
        if (mQuestionManager == null) {
            mQuestionManager = new QuestionDetailManager();
        }
        mQuestionManager.setOnCheckUserHeardListener(new QuestionDetailManager.OnCheckUserHeardListener() {
            @Override
            public void onSuccess(boolean userHeard) {
                listener.OnCheckUserHeardSuccess(userHeard);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mQuestionManager.checkUserHeard(question, user);
    }

    @Override
    public void getQuestionDetail(String questionId, final QuestionDetailResponseListener listener) {
        if (mQuestionManager == null) {
            mQuestionManager = new QuestionDetailManager();
        }
        mQuestionManager.setOnQuestionDetailListener(new QuestionDetailManager.OnQuestionDetailListener() {
            @Override
            public void onSuccess(Question question) {
                listener.OnGetDetailSuccess(question);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mQuestionManager.getQuestionDetail(questionId);
    }
}
