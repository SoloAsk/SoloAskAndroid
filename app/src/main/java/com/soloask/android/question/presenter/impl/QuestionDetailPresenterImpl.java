package com.soloask.android.question.presenter.impl;

import android.util.Log;

import com.soloask.android.R;
import com.soloask.android.question.interactor.QuestionDetailInteractor;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.question.presenter.QuestionDetailPresenter;
import com.soloask.android.question.view.QuestionDetailView;
import com.soloask.android.util.NetworkManager;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-10.
 */
public class QuestionDetailPresenterImpl implements QuestionDetailPresenter, QuestionDetailInteractor.QuestionDetailResponseListener {
    private QuestionDetailView mView;
    private QuestionDetailInteractor mInteractor;

    @Inject
    public QuestionDetailPresenterImpl(QuestionDetailView view, QuestionDetailInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void checkUserHeard(String question, String user) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showNetworkError(true);
            mView.showProgress(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showProgress(true);
            mView.showNetworkError(false);
            mInteractor.checkUserHeard(question, user, this);
        }
    }

    @Override
    public void getQuestionDetail(String questionId) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showNetworkError(true);
            mView.showProgress(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showProgress(true);
            mView.showNetworkError(false);
            mInteractor.getQuestionDetail(questionId, this);
        }
    }

    @Override
    public void setHeardUser(String question, String user) {
        if (mInteractor == null) {
            return;
        }
        mInteractor.setHeardUser(question, user, this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    @Override
    public void OnCheckUserHeardSuccess(boolean heard) {
        if (mView != null) {
            mView.showUserHeard(heard);
        }
    }

    @Override
    public void OnGetDetailSuccess(QuestionModel question) {
        if (mView != null && question != null) {
            mView.showQuestionDetail(question);
            mView.showProgress(false);
            mView.showNetworkError(false);
        }
    }

    @Override
    public void OnSetHeardUserSuccess() {
        Log.i("QuestionDetail", "listen question successfully");
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showNetworkError(true);
            mView.showProgress(false);
        }
    }
}
