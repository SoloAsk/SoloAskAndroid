package com.soloask.android.common.base;



/**
 * Created by lebron on 16-8-4.
 */
public abstract class BaseModule<T> {
    protected T mView;

    public BaseModule(T view) {
        mView = view;
    }
}
