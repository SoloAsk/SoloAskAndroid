package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.UserInteractor;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.User;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class UserInteractorImpl implements UserInteractor {
    private UserManager mManager;

    @Inject
    public UserInteractorImpl() {
    }

    @Override
    public void getUserInfo(String id, final UserInfoResponseListener listener) {
        if (mManager == null) {
            mManager = new UserManager();
        }
        mManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                listener.onResponseSuccess(user);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.getUserInfo(id);
    }
}
