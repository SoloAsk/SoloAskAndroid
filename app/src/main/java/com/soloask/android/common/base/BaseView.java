package com.soloask.android.common.base;

import android.content.Context;

/**
 * Created by lebron on 16-8-4.
 */
public interface BaseView<T> {
    /**
     * 提示Toast
     */
    void showToast(int stringId);

    Context getViewContext();

}
