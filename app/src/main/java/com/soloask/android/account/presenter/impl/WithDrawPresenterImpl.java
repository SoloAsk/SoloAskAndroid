package com.soloask.android.account.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.account.interactor.WithDrawInteractor;
import com.soloask.android.account.presenter.WithDrawPresenter;
import com.soloask.android.account.view.WithDrawView;
import com.soloask.android.data.model.User;
import com.soloask.android.util.NetworkManager;

import javax.inject.Inject;

/**
 * Created by LeBron on 2016/8/6.
 */
public class WithDrawPresenterImpl implements WithDrawPresenter, WithDrawInteractor.WithDrawResponseListener {
    private WithDrawView mView;
    private WithDrawInteractor mInteractor;

    @Inject
    public WithDrawPresenterImpl(WithDrawView view, WithDrawInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void withDrawRequest(String user, String paypalAccount) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showLoadingLayout(true);
            mInteractor.requestWithDraw(user, paypalAccount, this);
        }
    }

    @Override
    public void OnResponseSuccess() {
        if (mView != null) {
            mView.showLoadingLayout(false);
            mView.requestSuccess();
            mView.showToast(R.string.toast_withdraw_submited);
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
