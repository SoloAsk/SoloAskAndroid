package com.soloask.android.question.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-11.
 */
public interface AskInteractor extends BaseInteractor {
    void getCurrentUser(String userId, AskResponseListener listener);

    void getRespondentInfo(String userId, AskResponseListener listener);

    void getRespondentRelatedQuestions(User user, AskResponseListener listener);

    void askQuestion(Question question, AskResponseListener listener);

    interface AskResponseListener extends BaseResponseListener {
        void onGetCurrentUserSuccess(User user);

        void onGetRespondentInfoSucc(User user);

        void onGetRelatedQuestionsSucc(List<Question> questions);

        void onAskQuestionSucc(String questionId);
    }
}
