package com.soloask.android.account.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;

/**
 * Created by lebron on 16-8-5.
 */
public interface UserInteractor {
    void getUserInfo(String id, UserInfoResponseListener listener);

    interface UserInfoResponseListener extends BaseInteractor.BaseResponseListener {
        void onResponseSuccess(UserModel user);
    }
}
