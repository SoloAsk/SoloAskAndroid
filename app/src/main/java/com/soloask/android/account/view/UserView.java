package com.soloask.android.account.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-5.
 */
public interface UserView extends BaseView<User> {

    void showUserInfo(User user);
}
