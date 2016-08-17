package com.soloask.android.account.presenter;

import com.facebook.CallbackManager;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface LoginPresenter {
    void doLogin(CallbackManager callbackManager);

    void doQQLogin(Tencent tencent, JSONObject jsonObject);
}
