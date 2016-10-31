package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.EditUserInteractor;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.UpdateUserRequest;
import com.soloask.android.common.network.response.account.UserResponse;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-5.
 */
public class EditUserInteractorImpl implements EditUserInteractor {
    private ApiWrapper mApiWrapper;
    private UpdateUserRequest mRequest;
    private CompositeSubscription mSub;

    @Inject
    EditUserInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void setUserInfo(UserModel user, final EditUserInfoResponseListener listener) {
        if (mRequest == null) {
            mRequest = new UpdateUserRequest();
        }
        mRequest.setId(user.getUserId());
        mRequest.setPrice(user.getPrice());
        mRequest.setTitle(user.getUserTitle());
        mRequest.setIntroduction(user.getUserIntroduce());
        ApiResponseHandler.CustomHandler<UserResponse> handler = new ApiResponseHandler.CustomHandler<UserResponse>() {
            @Override
            public void success(UserResponse userResponse) {
                if (userResponse.getCode() == ApiConstant.STATUS_CODE_SUCC) {
                    listener.OnResponseSuccess();
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
        Subscription subscription = mApiWrapper.updateUser(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
