package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-5.
 */
public interface UserInteractor {
    void getUserInfo(String id, UserInfoResponseListener listener);

    interface UserInfoResponseListener extends BaseInteractor.BaseResponseListener {
        void onResponseSuccess(User user);
    }
}
