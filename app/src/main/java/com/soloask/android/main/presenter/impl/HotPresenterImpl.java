package com.soloask.android.main.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.data.model.Question;
import com.soloask.android.main.interactor.HotInteractor;
import com.soloask.android.main.presenter.HotPresenter;
import com.soloask.android.main.view.HotView;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-4.
 */
public class HotPresenterImpl implements HotPresenter, HotInteractor.HotQuestionResponseListener {
    private HotView mView;
    private HotInteractor mInteractor;

    @Inject
    public HotPresenterImpl(HotView view, HotInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getQuestionList() {
        if (mView == null || mInteractor == null || mInteractor.isLoading()) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            if (mInteractor.getSkipNum() <= 0) {
                mView.showNetworkError(true);
            }
            mView.showProgress(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showNetworkError(false);
            mInteractor.setIsLoading(true);
            mInteractor.getHotQuestionsData(this);
            if (mInteractor.getSkipNum() <= 0) {
                mView.showProgress(true);
            }
        }
    }

    @Override
    public void resetSkipNum() {
        if (mInteractor != null) {
            mInteractor.resetSkipNum();
        }
    }

    @Override
    public void start() {
        getQuestionList();
    }

    @Override
    public void stop() {
        if (mView != null) {
            mInteractor.setIsLoading(false);
            mView.showProgress(false);
        }
    }

    @Override
    public void onResponseSuccess(List<QuestionModel> list) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (list.size() > 0) {
            mView.showHotQuestions(list);
            mInteractor.setSkipNum(list.size());
        } else {
            //第一页就没数据
            if (mInteractor.getSkipNum() > 0) {
                mView.showToast(R.string.toast_no_more);
            }
        }
        mView.showProgress(false);
        mView.showNetworkError(false);
        mInteractor.setIsLoading(false);
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
        mView.showProgress(false);
    }
}
