package com.soloask.android.question.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-10.
 */
public interface QuestionDetailInteractor {
    void getCurrentUser(String userId, QuestionDetailResponseListener listener);

    void checkUserHeard(Question question, User user, QuestionDetailResponseListener listener);

    void getQuestionDetail(String questionId, QuestionDetailResponseListener listener);

    interface QuestionDetailResponseListener extends BaseInteractor.BaseResponseListener {
        void OnGetCurrentUserSuccess(User user);

        void OnCheckUserHeardSuccess(boolean heard);

        void OnGetDetailSuccess(Question question);
    }
}
