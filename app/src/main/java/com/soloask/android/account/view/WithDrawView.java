package com.soloask.android.account.view;

import com.soloask.android.common.base.BaseView;

/**
 * Created by LeBron on 2016/8/6.
 */
public interface WithDrawView extends BaseView {
    void requestSuccess();

    void showLoadingLayout(boolean show);
}
