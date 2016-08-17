package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-5.
 */
public interface EditUserInteractor {
    void setUserInfo(User user, EditUserInfoResponseListener listener);

    interface EditUserInfoResponseListener extends BaseInteractor.BaseResponseListener {
        void OnResponseSuccess();
    }
}
