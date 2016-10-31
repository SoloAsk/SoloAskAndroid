package com.soloask.android.main.interactor.impl;

import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.main.DiscoverRequest;
import com.soloask.android.common.network.response.main.UserListResponse;
import com.soloask.android.main.interactor.PersonInteractor;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-5.
 */
public class PersonInteractorImpl implements PersonInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private DiscoverRequest mRequest;

    @Inject
    public PersonInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void getPopularPersonData(final PersonResponseListener listener) {
        mRequest = new DiscoverRequest();
        mRequest.setOffset(mSkipNum);
        mRequest.setSize(10);

        ApiResponseHandler.CustomHandler<UserListResponse> handler = new ApiResponseHandler.CustomHandler<UserListResponse>() {
            @Override
            public void success(UserListResponse discoverResponse) {
                if (discoverResponse.getCode() == 1 && discoverResponse.getUserList() != null) {
                    listener.onResponseSuccess(discoverResponse.getUserList());
                } else {
                    listener.onResponseFailed();
                }
            }

            @Override
            public boolean operationError(UserListResponse discoverResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.getUserList(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void setSkipNum(int skipNum) {
        mSkipNum += skipNum;
    }

    @Override
    public int getSkipNum() {
        return mSkipNum;
    }

    @Override
    public void resetSkipNum() {
        mSkipNum = 0;
    }
}
