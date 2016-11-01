package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface WithDrawInteractor {
    void requestWithDraw(String userId, String paypalAccount, WithDrawResponseListener listener);

    interface WithDrawResponseListener extends BaseInteractor.BaseResponseListener {
        void OnResponseSuccess();
    }
}
