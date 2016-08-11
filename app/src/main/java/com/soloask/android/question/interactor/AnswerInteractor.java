package com.soloask.android.question.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;

/**
 * Created by lebron on 16-8-10.
 */
public interface AnswerInteractor {
    void getQuestionDetail(String questionId, AnswerResponseListener listener);

    void uploadAnswer(Question question, String filepath, AnswerResponseListener listener);

    interface AnswerResponseListener extends BaseInteractor.BaseResponseListener {
        void OnQuestionDetailSucess(Question question);

        void OnUploadAnswerSuccess();
    }
}
