package com.soloask.android.account.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.User;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface LoginView extends BaseView {
    void loginSuccess(User user);

    void showLoadingProgress(boolean show);
}
