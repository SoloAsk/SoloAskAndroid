package com.soloask.android.account.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.account.interactor.MyCommonInteractor;
import com.soloask.android.account.presenter.MyCommonPresenter;
import com.soloask.android.account.view.MyCommonView;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-8.
 */
public class MyCommonPresenterImpl implements MyCommonPresenter, MyCommonInteractor.MyCommonResponseListener {
    private MyCommonView mView;
    private MyCommonInteractor mInteractor;

    @Inject
    public MyCommonPresenterImpl(MyCommonView view, MyCommonInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getQuestionList(User user, int type) {
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
            mInteractor.getMyQuestionsData(user,type, this);
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
        //getQuestionList();
    }

    @Override
    public void stop() {
        if (mView != null) {
            mInteractor.setIsLoading(false);
            mView.showProgress(false);
        }
    }

    @Override
    public void OnQuestionsResponseSuccess(List<Question> list) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (list.size() > 0) {
            mView.showMyQuestions(list);
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
