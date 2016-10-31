package com.soloask.android.account.view;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.User;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface LoginView extends BaseView {
    void loginSuccess(UserModel user);

    void showLoadingProgress(boolean show);
}
