package com.soloask.android.main.presenter;

import com.soloask.android.common.base.BasePresenter;

/**
 * Created by lebron on 16-8-5.
 */
public interface PersonPresenter extends BasePresenter {
    void getPersonList();

    void resetSkipNum();
}
