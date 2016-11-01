package com.soloask.android.question.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

/**
 * Created by lebron on 16-8-10.
 */
public interface QuestionDetailInteractor {
    void checkUserHeard(String question, String user, QuestionDetailResponseListener listener);

    void getQuestionDetail(String questionId, QuestionDetailResponseListener listener);

    void setHeardUser(String question, String user, QuestionDetailResponseListener listener);

    interface QuestionDetailResponseListener extends BaseInteractor.BaseResponseListener {

        void OnCheckUserHeardSuccess(boolean heard);

        void OnGetDetailSuccess(QuestionModel question);

        void OnSetHeardUserSuccess();
    }
}
