package com.soloask.android.account.presenter.impl;


import com.soloask.android.R;
import com.soloask.android.account.interactor.EditUserInteractor;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.account.presenter.EditUserPresenter;
import com.soloask.android.account.view.EditUserView;
import com.soloask.android.util.NetworkManager;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class EditUserPresenterImpl implements EditUserPresenter, EditUserInteractor.EditUserInfoResponseListener {
    private EditUserView mView;
    private EditUserInteractor mInteractor;

    @Inject
    public EditUserPresenterImpl(EditUserView view, EditUserInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void setUserInfo(UserModel user) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showLoadingLayout(true);
            mInteractor.setUserInfo(user, this);
        }
    }

    @Override
    public void OnResponseSuccess() {
        if (mView != null) {
            mView.updateUserInfoSuccess();
            mView.showLoadingLayout(false);
        }
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showLoadingLayout(false);
            mView.showToast(R.string.failed_to_load_data);
        }
    }
}
