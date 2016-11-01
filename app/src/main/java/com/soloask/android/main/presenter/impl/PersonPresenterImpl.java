package com.soloask.android.main.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.main.interactor.PersonInteractor;
import com.soloask.android.main.presenter.PersonPresenter;
import com.soloask.android.main.view.PersonView;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class PersonPresenterImpl implements PersonPresenter, PersonInteractor.PersonResponseListener {
    private PersonView mView;
    private PersonInteractor mInteractor;

    @Inject
    PersonPresenterImpl(PersonView view, PersonInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getPersonList() {
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
            mInteractor.getPopularPersonData(this);
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
        getPersonList();
    }

    @Override
    public void stop() {
        mView.showProgress(false);
        mInteractor.setIsLoading(false);
    }

    @Override
    public void onResponseSuccess(List<UserModel> list) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (list.size() > 0) {
            mView.showPopularPersons(list);
            mInteractor.setSkipNum(list.size());
        } else {
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
        if (mView == null || mInteractor == null) {
            return;
        }
        if (mInteractor.getSkipNum() <= 0) {
            mView.showNetworkError(true);
        }
        mView.showProgress(false);
        mInteractor.setIsLoading(false);
    }
}
