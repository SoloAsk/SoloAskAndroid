package com.soloask.android.account.presenter;

import com.soloask.android.common.base.BasePresenter;

/**
 * Created by lebron on 16-8-8.
 */
public interface MyCommonPresenter extends BasePresenter {
    void getQuestionList(String user_id, int type);

    void resetSkipNum();
}
