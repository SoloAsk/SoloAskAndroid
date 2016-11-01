package com.soloask.android.account.view;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseView;

/**
 * Created by lebron on 16-8-5.
 */
public interface EditUserView extends BaseView<UserModel> {
    void updateUserInfoSuccess();

    void showLoadingLayout(boolean show);
}
