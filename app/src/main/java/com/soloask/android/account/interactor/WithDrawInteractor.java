package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.User;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface WithDrawInteractor {
    void requestWithDraw(User user, String paypalAccount, WithDrawResponseListener listener);

    interface WithDrawResponseListener extends BaseInteractor.BaseResponseListener {
        void OnResponseSuccess(String id);
    }
}
