package com.soloask.android.common.network;

import rx.Subscriber;

/**
 * Created by lebron on 16-10-27.
 */

public class ApiSubscriber<T> extends Subscriber<T> {
    private ApiResponseHandler mApiResponseHandler;

    public ApiSubscriber(ApiResponseHandler.CustomHandler<T> handler) {
        mApiResponseHandler = new ApiResponseHandler(handler);
    }

    @Override
    public void onCompleted() {
        mApiResponseHandler.release();
    }

    @Override
    public void onError(Throwable e) {
        mApiResponseHandler.onError(e);
    }

    @Override
    public void onNext(T t) {
        mApiResponseHandler.onNext(t);
    }
}
