package com.soloask.android.question.interactor.impl;

import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.question.CheckHeardRequest;
import com.soloask.android.common.network.request.question.QuesDetailRequest;
import com.soloask.android.common.network.response.question.CheckHeardResponse;
import com.soloask.android.common.network.response.question.QuestionResponse;
import com.soloask.android.common.network.response.question.SetHeardResponse;
import com.soloask.android.question.interactor.QuestionDetailInteractor;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-10.
 */
public class QuestionDetailInteractorImpl implements QuestionDetailInteractor {
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private CheckHeardRequest mCheckRequest;
    private QuesDetailRequest mRequest;

    @Inject
    public QuestionDetailInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void checkUserHeard(String question, String user, final QuestionDetailResponseListener listener) {
        if (mCheckRequest == null) {
            mCheckRequest = new CheckHeardRequest();
        }
        mCheckRequest.setUser_id(user);
        mCheckRequest.setQuestion_id(question);
        ApiResponseHandler.CustomHandler<CheckHeardResponse> handler = new ApiResponseHandler.CustomHandler<CheckHeardResponse>() {
            @Override
            public void success(CheckHeardResponse checkHeardResponse) {
                if (checkHeardResponse.getCode() == ApiConstant.STATUS_CODE_SUCC) {
                    listener.OnCheckUserHeardSuccess(checkHeardResponse.isHeard());
                }
            }

            @Override
            public boolean operationError(CheckHeardResponse checkHeardResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.checkQuesHeard(mCheckRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public void getQuestionDetail(final String questionId, final QuestionDetailResponseListener listener) {
        if (mRequest == null) {
            mRequest = new QuesDetailRequest();
        }
        mRequest.setId(questionId);
        ApiResponseHandler.CustomHandler<QuestionResponse> handler = new ApiResponseHandler.CustomHandler<QuestionResponse>() {
            @Override
            public void success(QuestionResponse questionResponse) {
                if (questionResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && questionResponse.getQuestionModel() != null) {
                    listener.OnGetDetailSuccess(questionResponse.getQuestionModel());
                }
            }

            @Override
            public boolean operationError(QuestionResponse questionResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.getQuesDetail(mRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public void setHeardUser(String question, String user, final QuestionDetailResponseListener listener) {
        if (mCheckRequest == null) {
            mCheckRequest = new CheckHeardRequest();
        }
        mCheckRequest.setQuestion_id(question);
        mCheckRequest.setUser_id(user);
        ApiResponseHandler.CustomHandler<SetHeardResponse> handler = new ApiResponseHandler.CustomHandler<SetHeardResponse>() {
            @Override
            public void success(SetHeardResponse response) {
                if (response.getCode() == ApiConstant.STATUS_CODE_SUCC) {
                    listener.OnSetHeardUserSuccess();
                }
            }

            @Override
            public boolean operationError(SetHeardResponse setHeardResponse, int status, String message) {
                listener.onResponseFailed();
                return false;
            }

            @Override
            public boolean error(Throwable e) {
                listener.onResponseFailed();
                return false;
            }
        };
        Subscription subscription = mApiWrapper.setHeardUser(mCheckRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
