package com.soloask.android.account.interactor;

import android.content.Context;

import com.facebook.CallbackManager;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface LoginInteractor {
    void doLogin(Context context, CallbackManager manager, LoginResponseListener listener);

    interface LoginResponseListener extends BaseInteractor.BaseResponseListener {
        void OnLoginResponseSuccess(UserModel user);

        void OnLoginResponseCancel();
    }
}
