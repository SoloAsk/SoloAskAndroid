package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.UserInteractor;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.UserRequest;
import com.soloask.android.common.network.response.account.UserResponse;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-5.
 */
public class UserInteractorImpl implements UserInteractor {
    private UserRequest mRequest;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;

    @Inject
    public UserInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void getUserInfo(String id, final UserInfoResponseListener listener) {
        if (mRequest == null) {
            mRequest = new UserRequest();
        }
        mRequest.setId(id);
        ApiResponseHandler.CustomHandler<UserResponse> handler = new ApiResponseHandler.CustomHandler<UserResponse>() {
            @Override
            public void success(UserResponse userResponse) {
                if (userResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && userResponse.getUserModel() != null) {
                    listener.onResponseSuccess(userResponse.getUserModel());
                } else {
                    listener.onResponseFailed();
                }
            }

            @Override
            public boolean operationError(UserResponse userResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.getUserInfo(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
