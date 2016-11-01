package com.soloask.android.question.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.question.interactor.AskInteractor;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.question.presenter.AskPresenter;
import com.soloask.android.question.view.AskView;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-11.
 */
public class AskPresenterImpl implements AskPresenter, AskInteractor.AskResponseListener {
    private AskView mView;
    private AskInteractor mInteractor;

    @Inject
    public AskPresenterImpl(AskView view, AskInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getRespondentInfo(String userId) {
        if (mInteractor != null) {
            mInteractor.getRespondentInfo(userId, this);
        }
    }

    @Override
    public void getRespondentRelatedQuestions(String userId) {
        if (mView == null || mInteractor == null || mInteractor.isLoading()) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            if (mInteractor.getSkipNum() <= 0) {
                mView.showNetworkError(true);
            }
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showNetworkError(false);
            mInteractor.setIsLoading(true);
            mInteractor.getRespondentRelatedQuestions(userId, this);
        }
    }

    @Override
    public void askQuestion(QuestionModel question) {
        if (mInteractor != null) {
            mInteractor.askQuestion(question, this);
        }
    }

    @Override
    public void resetSkipNum() {
        if (mInteractor != null) {
            mInteractor.resetSkipNum();
        }
    }

    @Override
    public void onGetRespondentInfoSucc(UserModel user) {
        if (mView != null && user != null) {
            mView.showCurrentRespondentInfo(user);
        }
    }

    @Override
    public void onGetRelatedQuestionsSucc(List<QuestionModel> questions) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (questions.size() > 0) {
            mView.showRelatedQuestions(questions);
            mInteractor.setSkipNum(questions.size());
        } else {
            if (mInteractor.getSkipNum() > 0) {
                mView.showToast(R.string.toast_no_more);
            }
        }
        mView.showNetworkError(false);
        mInteractor.setIsLoading(false);
    }

    @Override
    public void onAskQuestionSucc(String questionId) {
        if (mView != null && questionId != null) {
            mView.askSuccess(questionId);
        }
    }

    @Override
    public void onResponseFailed() {
        if (mView == null) {
            return;
        }
        if (mInteractor.getSkipNum() <= 0) {
            mView.showNetworkError(true);
        }
        mInteractor.setIsLoading(false);
    }
}
