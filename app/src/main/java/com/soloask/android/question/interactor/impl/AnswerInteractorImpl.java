package com.soloask.android.question.interactor.impl;

import com.soloask.android.data.bmob.AnswerManager;
import com.soloask.android.data.bmob.QuestionDetailManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.question.interactor.AnswerInteractor;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-10.
 */
public class AnswerInteractorImpl implements AnswerInteractor {
    private QuestionDetailManager mQuestionDetailManager;
    private AnswerManager mManager;

    @Inject
    public AnswerInteractorImpl() {
    }

    @Override
    public void getQuestionDetail(String questionId, final AnswerResponseListener listener) {
        if (mQuestionDetailManager == null) {
            mQuestionDetailManager = new QuestionDetailManager();
        }
        mQuestionDetailManager.setOnQuestionDetailListener(new QuestionDetailManager.OnQuestionDetailListener() {
            @Override
            public void onSuccess(Question question) {
                listener.OnQuestionDetailSucess(question);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mQuestionDetailManager.getQuestionDetail(questionId);
    }

    @Override
    public void uploadAnswer(Question question, String filepath, final AnswerResponseListener listener) {
        if (mManager == null) {
            mManager = new AnswerManager();
        }
        mManager.setOnUpLoadAnswerListener(new AnswerManager.OnUpLoadAnswerListener() {
            @Override
            public void onSuccess() {
                listener.OnUploadAnswerSuccess();
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.upLoadAnswer(question, filepath);
    }
}
