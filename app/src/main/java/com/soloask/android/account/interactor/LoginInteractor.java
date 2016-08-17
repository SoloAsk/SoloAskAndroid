package com.soloask.android.account.interactor;

import android.content.Context;

import com.facebook.CallbackManager;
import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.User;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface LoginInteractor {
    void doLogin(Context context, CallbackManager manager, LoginResponseListener listener);

    void doQQLogin(Context context, JSONObject jsonObject, Tencent tencent, LoginResponseListener listener);

    interface LoginResponseListener extends BaseInteractor.BaseResponseListener {
        void OnLoginResponseSuccess(User user);

        void OnLoginResponseCancel();
    }
}
