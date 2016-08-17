package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.EditUserInteractor;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.User;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class EditUserInteractorImpl implements EditUserInteractor {
    private UserManager mManager;

    @Inject
    EditUserInteractorImpl() {
    }

    @Override
    public void setUserInfo(User user, final EditUserInfoResponseListener listener) {
        if (mManager == null) {
            mManager = new UserManager();
        }
        mManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                listener.OnResponseSuccess();
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.updateUserInfo(user);
    }
}
