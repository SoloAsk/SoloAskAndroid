package com.soloask.android.account.presenter.impl;

import com.facebook.CallbackManager;
import com.soloask.android.R;
import com.soloask.android.account.interactor.LoginInteractor;
import com.soloask.android.account.presenter.LoginPresenter;
import com.soloask.android.account.view.LoginView;
import com.soloask.android.data.model.User;
import com.soloask.android.util.NetworkManager;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by LeBron on 2016/8/6.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.LoginResponseListener {
    private LoginView mView;
    private LoginInteractor mInteractor;

    @Inject
    public LoginPresenterImpl(LoginView loginView, LoginInteractor interactor) {
        mView = loginView;
        mInteractor = interactor;
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showToast(R.string.login_failed);
            mView.showLoadingProgress(false);
        }
    }

    @Override
    public void doLogin(CallbackManager callbackManager) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showLoadingProgress(true);
            mInteractor.doLogin(mView.getViewContext(), callbackManager, this);
        }
    }

    @Override
    public void doQQLogin(Tencent tencent, JSONObject jsonObject) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showLoadingProgress(true);
            mInteractor.doQQLogin(mView.getViewContext(), jsonObject, tencent, this);
        }
    }

    @Override
    public void OnLoginResponseSuccess(User user) {
        if (user != null) {
            mView.loginSuccess(user);
        } else {
            mView.showToast(R.string.login_failed);
        }
        mView.showLoadingProgress(false);
    }

    @Override
    public void OnLoginResponseCancel() {
        if (mView != null) {
            mView.showToast(R.string.login_cancel);
            mView.showLoadingProgress(false);
        }
    }
}
