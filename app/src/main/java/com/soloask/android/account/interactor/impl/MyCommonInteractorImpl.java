package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.MyCommonInteractor;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.MineQuesRequest;
import com.soloask.android.common.network.response.main.QuesListResponse;
import com.soloask.android.util.Constant;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-8.
 */
public class MyCommonInteractorImpl implements MyCommonInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private MineQuesRequest mRequest;

    @Inject
    public MyCommonInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void getMyQuestionsData(String user_id, int type, final MyCommonResponseListener listener) {
        String requestType;
        if (type == Constant.KEY_FROM_MY_ANSWER) {
            requestType = "answer";
        } else if (type == Constant.KEY_FROM_MY_QUESTION) {
            requestType = "ask";
        }else{
            requestType = "listen";
        }
        if (mRequest == null) {
            mRequest = new MineQuesRequest();
        }
        mRequest.setType(requestType);
        mRequest.setUser_id(user_id);
        mRequest.setOffset(mSkipNum);
        mRequest.setSize(10);
        ApiResponseHandler.CustomHandler<QuesListResponse> handler = new ApiResponseHandler.CustomHandler<QuesListResponse>() {
            @Override
            public void success(QuesListResponse quesListResponse) {
                if (quesListResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && quesListResponse.getQuestionList() != null) {
                    listener.OnQuestionsResponseSuccess(quesListResponse.getQuestionList());
                }
            }

            @Override
            public boolean operationError(QuesListResponse quesListResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Observable<QuesListResponse> observable;
        if (type == Constant.KEY_FROM_MY_LISTEN) {
            observable = mApiWrapper.getListenedQuesList(mRequest);
        } else {
            observable = mApiWrapper.getRelatedQuesList(mRequest);
        }
        Subscription subscription = observable
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
