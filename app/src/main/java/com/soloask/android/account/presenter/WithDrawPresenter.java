package com.soloask.android.account.presenter;

import com.soloask.android.data.model.User;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface WithDrawPresenter {
    void withDrawRequest(User user, String paypalAccount);
}
