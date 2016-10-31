package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.WithDrawInteractor;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.WithDrawRequest;
import com.soloask.android.common.network.response.WithDrawResponse;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LeBron on 2016/8/6.
 */
public class WithDrawInteractorImpl implements WithDrawInteractor {

    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private WithDrawRequest mRequest;

    @Inject
    public WithDrawInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void requestWithDraw(String user, String paypalAccount, final WithDrawResponseListener listener) {
        if (mRequest == null) {
            mRequest = new WithDrawRequest();
        }
        mRequest.setUser_id(user);
        mRequest.setPaypal(paypalAccount);
        ApiResponseHandler.CustomHandler<WithDrawResponse> handler = new ApiResponseHandler.CustomHandler<WithDrawResponse>() {
            @Override
            public void success(WithDrawResponse baseResponse) {
                if (baseResponse.getCode() == ApiConstant.STATUS_CODE_SUCC) {
                    listener.OnResponseSuccess();
                }
            }

            @Override
            public boolean operationError(WithDrawResponse baseResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.createWithDraw(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
