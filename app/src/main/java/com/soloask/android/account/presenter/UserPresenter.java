package com.soloask.android.account.presenter;

import com.soloask.android.common.base.BasePresenter;

/**
 * Created by lebron on 16-8-5.
 */
public interface UserPresenter extends BasePresenter {
    void getUserInfo(String userId);
}
