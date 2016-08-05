package com.soloask.android.common.base;


/**
 * Created by lebron on 16-8-5.
 */
public interface BaseInteractor {
    boolean isLoading();

    void setIsLoading(boolean isLoading);

    void setSkipNum(int skipNum);

    int getSkipNum();

    void resetSkipNum();

    interface BaseResponseListener {
        void onResponseFailed();
    }
}
