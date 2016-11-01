package com.soloask.android.account.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;

/**
 * Created by lebron on 16-8-5.
 */
public interface EditUserInteractor {
    void setUserInfo(UserModel user, EditUserInfoResponseListener listener);

    interface EditUserInfoResponseListener extends BaseInteractor.BaseResponseListener{
        void OnResponseSuccess();
    }
}
