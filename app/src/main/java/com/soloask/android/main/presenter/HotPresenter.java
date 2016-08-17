package com.soloask.android.main.presenter;

import com.soloask.android.common.base.BasePresenter;

/**
 * Created by lebron on 16-8-4.
 */
public interface HotPresenter extends BasePresenter {

    void getQuestionList();

    void resetSkipNum();
}
