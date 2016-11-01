package com.soloask.android.question.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-11.
 */
public interface AskInteractor extends BaseInteractor {

    void getRespondentInfo(String userId, AskResponseListener listener);

    void getRespondentRelatedQuestions(String userId, AskResponseListener listener);

    void askQuestion(QuestionModel question, AskResponseListener listener);

    interface AskResponseListener extends BaseResponseListener {

        void onGetRespondentInfoSucc(UserModel user);

        void onGetRelatedQuestionsSucc(List<QuestionModel> questions);

        void onAskQuestionSucc(String questionId);
    }
}
