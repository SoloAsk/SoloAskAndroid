package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.WithDrawInteractor;
import com.soloask.android.data.bmob.WithDrawManager;
import com.soloask.android.data.model.User;

import javax.inject.Inject;

/**
 * Created by LeBron on 2016/8/6.
 */
public class WithDrawInteractorImpl implements WithDrawInteractor {
    private WithDrawManager mManager;

    @Inject
    public WithDrawInteractorImpl() {
    }

    @Override
    public void requestWithDraw(User user, String paypalAccount, final WithDrawResponseListener listener) {
        if (mManager == null) {
            mManager = new WithDrawManager();
        }
        mManager.setOnWithDrawListener(new WithDrawManager.OnWithDrawListener() {
            @Override
            public void onSuccess(String objectId) {
                listener.OnResponseSuccess(objectId);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.dealWithdraw(user, paypalAccount);
    }
}
