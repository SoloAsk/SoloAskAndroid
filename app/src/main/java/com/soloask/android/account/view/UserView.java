package com.soloask.android.account.view;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseView;

/**
 * Created by lebron on 16-8-5.
 */
public interface UserView extends BaseView<UserModel> {

    void showUserInfo(UserModel user);
}
