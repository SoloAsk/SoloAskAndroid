package com.soloask.android.account.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-5.
 */
public interface EditUserView extends BaseView<User> {
    void updateUserInfoSuccess();

    void showLoadingLayout(boolean show);
}
