package com.soloask.android.question.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

/**
 * Created by lebron on 16-8-10.
 */
public interface AnswerInteractor {
    void getQuestionDetail(String questionId, AnswerResponseListener listener);

    void uploadAnswer(String questionId, String filepath, int length, AnswerResponseListener listener);

    interface AnswerResponseListener extends BaseInteractor.BaseResponseListener {
        void OnQuestionDetailSucess(QuestionModel question);

        void OnUploadAnswerSuccess();
    }
}
