package com.soloask.android.question.interactor.impl;

import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.MineQuesRequest;
import com.soloask.android.common.network.request.account.UserRequest;
import com.soloask.android.common.network.request.question.AskRequest;
import com.soloask.android.common.network.response.account.UserResponse;
import com.soloask.android.common.network.response.main.QuesListResponse;
import com.soloask.android.common.network.response.question.QuestionResponse;
import com.soloask.android.question.interactor.AskInteractor;
import com.soloask.android.question.model.QuestionModel;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-8-11.
 */
public class AskInteractorImpl implements AskInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;
    private UserRequest mUserRequest;
    private MineQuesRequest mRelatedRequest;
    private AskRequest mAskRequest;

    @Inject
    public AskInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
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

    @Override
    public void getRespondentInfo(String userId, final AskResponseListener listener) {
        if (mUserRequest == null) {
            mUserRequest = new UserRequest();
        }
        mUserRequest.setId(userId);
        ApiResponseHandler.CustomHandler<UserResponse> handler = new ApiResponseHandler.CustomHandler<UserResponse>() {
            @Override
            public void success(UserResponse userResponse) {
                if (userResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && userResponse.getUserModel() != null) {
                    listener.onGetRespondentInfoSucc(userResponse.getUserModel());
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
        Subscription subscription = mApiWrapper.getUserInfo(mUserRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public void getRespondentRelatedQuestions(String userId, final AskResponseListener listener) {
        if (mRelatedRequest == null) {
            mRelatedRequest = new MineQuesRequest();
        }
        mRelatedRequest.setType("history");
        mRelatedRequest.setUser_id(userId);
        mRelatedRequest.setOffset(mSkipNum);
        mRelatedRequest.setSize(10);
        ApiResponseHandler.CustomHandler<QuesListResponse> handler = new ApiResponseHandler.CustomHandler<QuesListResponse>() {
            @Override
            public void success(QuesListResponse quesListResponse) {
                if (quesListResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && quesListResponse.getQuestionList() != null) {
                    listener.onGetRelatedQuestionsSucc(quesListResponse.getQuestionList());
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
        Subscription subscription = mApiWrapper.getRelatedQuesList(mRelatedRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }

    @Override
    public void askQuestion(final QuestionModel question, final AskResponseListener listener) {
        if (mAskRequest == null) {
            mAskRequest = new AskRequest();
        }
        mAskRequest.setContent(question.getContent());
        mAskRequest.setPrice(question.getPrice());
        mAskRequest.setIsPublic(question.getPublic() ? 1 : 0);
        mAskRequest.setAsk_uuid(question.getAskUser().getUserId());
        mAskRequest.setAnswer_uuid(question.getAnswerUser().getUserId());
        ApiResponseHandler.CustomHandler<QuestionResponse> handler = new ApiResponseHandler.CustomHandler<QuestionResponse>() {
            @Override
            public void success(QuestionResponse questionResponse) {
                if (questionResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && questionResponse.getQuestionModel() != null) {
                    listener.onAskQuestionSucc(String.valueOf(questionResponse.getQuestionModel().getId()));
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
        Subscription subscription = mApiWrapper.createQuestion(mAskRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSubscriber<>(handler));
        mSub.add(subscription);
    }
}
