package com.soloask.android.account.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.account.interactor.UserInteractor;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.account.presenter.UserPresenter;
import com.soloask.android.account.view.UserView;
import com.soloask.android.data.model.User;
import com.soloask.android.util.NetworkManager;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class UserPresenterImpl implements UserPresenter, UserInteractor.UserInfoResponseListener {
    private UserView mView;
    private UserInteractor mInteractor;

    @Inject
    public UserPresenterImpl(UserView view, UserInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getUserInfo(String userId) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mInteractor.getUserInfo(userId, this);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onResponseSuccess(UserModel user) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (user != null) {
            mView.showUserInfo(user);
        } else {
            mView.showToast(R.string.failed_to_load_data);
        }
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showToast(R.string.failed_to_load_data);
        }
    }
}
