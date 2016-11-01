package com.soloask.android.search.interactor.impl;

import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.search.SearchRequest;
import com.soloask.android.common.network.response.main.QuesListResponse;
import com.soloask.android.common.network.response.main.UserListResponse;
import com.soloask.android.search.interactor.SearchMoreInteractor;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-9.
 */
public class SearchMoreInteractorImpl implements SearchMoreInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private SearchRequest mRequest;

    @Inject
    public SearchMoreInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void getSearchResult(boolean isQuestion, String keyword, final SearchResultResponseListener listener) {
        if (mRequest == null) {
            mRequest = new SearchRequest();
        }
        mRequest.setKeyword(keyword);
        mRequest.setOffset(mSkipNum);
        mRequest.setSize(10);
        if (isQuestion) {
            ApiResponseHandler.CustomHandler<QuesListResponse> handler = new ApiResponseHandler.CustomHandler<QuesListResponse>() {
                @Override
                public void success(QuesListResponse response) {
                    if (response.getCode() == ApiConstant.STATUS_CODE_SUCC && response.getQuestionList() != null) {
                        listener.OnSearchQuestionSuccess(response.getQuestionList());
                    }
                }

                @Override
                public boolean operationError(QuesListResponse response, int status, String message) {
                    listener.onResponseFailed();
                    return false;
                }

                @Override
                public boolean error(Throwable e) {
                    listener.onResponseFailed();
                    return false;
                }
            };
            Subscription subscription = mApiWrapper.searchQuestions(mRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ApiSubscriber<>(handler));
            mSub.add(subscription);
        } else {
            ApiResponseHandler.CustomHandler<UserListResponse> handler = new ApiResponseHandler.CustomHandler<UserListResponse>() {
                @Override
                public void success(UserListResponse userListResponse) {
                    if (userListResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && userListResponse.getUserList() != null) {
                        listener.OnSearchPersonSuccess(userListResponse.getUserList());
                    }
                }

                @Override
                public boolean operationError(UserListResponse userListResponse, int status, String message) {
                    listener.onResponseFailed();
                    return false;
                }

                @Override
                public boolean error(Throwable e) {
                    listener.onResponseFailed();
                    return false;
                }
            };
            Subscription subscription = mApiWrapper.searchUsers(mRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ApiSubscriber<>(handler));
            mSub.add(subscription);
        }

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
    public int getSkipNum() {
        return mSkipNum;
    }

    @Override
    public void setSkipNum(int skipNum) {
        mSkipNum += skipNum;
    }

    @Override
    public void resetSkipNum() {
        mSkipNum = 0;
    }
}
