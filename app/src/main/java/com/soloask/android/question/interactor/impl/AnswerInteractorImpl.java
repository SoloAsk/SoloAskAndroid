package com.soloask.android.question.interactor.impl;

import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.question.AnswerRequest;
import com.soloask.android.common.network.request.question.QuesDetailRequest;
import com.soloask.android.common.network.response.question.QuestionResponse;
import com.soloask.android.util.AnswerManager;
import com.soloask.android.question.interactor.AnswerInteractor;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-10.
 */
public class AnswerInteractorImpl implements AnswerInteractor {
    private AnswerManager mManager;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private AnswerRequest mAnswerRequest;
    private QuesDetailRequest mDetailRequest;


    @Inject
    public AnswerInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void getQuestionDetail(final String questionId, final AnswerResponseListener listener) {
        if (mDetailRequest == null) {
            mDetailRequest = new QuesDetailRequest();
        }
        mDetailRequest.setId(questionId);
        ApiResponseHandler.CustomHandler<QuestionResponse> handler = new ApiResponseHandler.CustomHandler<QuestionResponse>() {
            @Override
            public void success(QuestionResponse questionResponse) {
                if (questionResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && questionResponse.getQuestionModel() != null) {
                    listener.OnQuestionDetailSucess(questionResponse.getQuestionModel());
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
        Subscription subscription = mApiWrapper.getQuesDetail(mDetailRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public void uploadAnswer(final String questionId, String filepath, final int length, final AnswerResponseListener listener) {
        if (mManager == null) {
            mManager = new AnswerManager();
        }
        mManager.setOnUpLoadAnswerListener(new AnswerManager.OnUpLoadAnswerListener() {
            @Override
            public void onSuccess(String url) {
                updateQuestion(questionId, url, length, listener);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.upLoadAnswer(filepath);
    }

    private void updateQuestion(final String questionId, String url, int length, final AnswerResponseListener listener) {
        if (mAnswerRequest == null) {
            mAnswerRequest = new AnswerRequest();
        }
        mAnswerRequest.setId(questionId);
        mAnswerRequest.setLength(length);
        mAnswerRequest.setUrl(url);
        ApiResponseHandler.CustomHandler<QuestionResponse> handler = new ApiResponseHandler.CustomHandler<QuestionResponse>() {
            @Override
            public void success(QuestionResponse questionResponse) {
                if (questionResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && questionResponse.getQuestionModel() != null) {
                    listener.OnQuestionDetailSucess(questionResponse.getQuestionModel());
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
        Subscription subscription = mApiWrapper.answerQuestion(mAnswerRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
