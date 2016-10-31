package com.soloask.android.main.interactor.impl;

import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.main.HotRequest;
import com.soloask.android.common.network.response.main.QuesListResponse;
import com.soloask.android.main.interactor.HotInteractor;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-4.
 */
public class HotInteractorImpl implements HotInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private HotRequest mRequest;

    @Inject
    public HotInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
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

    @Override
    public void getHotQuestionsData(final HotQuestionResponseListener listener) {
        mRequest = new HotRequest();
        mRequest.setOffset(mSkipNum);
        mRequest.setSize(10);

        ApiResponseHandler.CustomHandler<QuesListResponse> handler = new ApiResponseHandler.CustomHandler<QuesListResponse>() {
            @Override
            public void success(QuesListResponse hotResponse) {
                if (hotResponse.getCode() == 1 && hotResponse.getQuestionList() != null) {
                    listener.onResponseSuccess(hotResponse.getQuestionList());
                } else {
                    listener.onResponseFailed();
                }
            }

            @Override
            public boolean operationError(QuesListResponse hotResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.getHotQues(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
