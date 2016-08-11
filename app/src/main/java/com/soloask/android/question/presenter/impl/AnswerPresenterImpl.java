package com.soloask.android.question.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.data.model.Question;
import com.soloask.android.question.interactor.AnswerInteractor;
import com.soloask.android.question.presenter.AnswerPresenter;
import com.soloask.android.question.view.AnswerView;
import com.soloask.android.util.NetworkManager;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-10.
 */
public class AnswerPresenterImpl implements AnswerPresenter, AnswerInteractor.AnswerResponseListener {
    private AnswerView mView;
    private AnswerInteractor mInteractor;

    @Inject
    public AnswerPresenterImpl(AnswerView view, AnswerInteractor interactor) {
        mView = view;
        mInteractor = interactor;
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
    public void uploadAnswer(Question question, String filePath) {
        if (mView == null || mInteractor == null) {
            return;
        }
        mView.showProgress(true);
        mView.showNetworkError(false);
        mInteractor.uploadAnswer(question, filePath, this);
    }

    @Override
    public void OnQuestionDetailSucess(Question question) {
        if (mView != null && question != null) {
            mView.showProgress(false);
            mView.showNetworkError(false);
            mView.showQuestionDetail(question);
        }
    }

    @Override
    public void OnUploadAnswerSuccess() {
        if (mView != null) {
            mView.showProgress(false);
            mView.showNetworkError(false);
            mView.showUploadAnswerSuccess();
        }
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showNetworkError(true);
            mView.showProgress(false);
        }
    }
}
