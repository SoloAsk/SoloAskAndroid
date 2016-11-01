package com.soloask.android.question.presenter;

import com.soloask.android.question.model.QuestionModel;

/**
 * Created by lebron on 16-8-11.
 */
public interface AskPresenter {

    void getRespondentInfo(String userId);

    void getRespondentRelatedQuestions(String userId);

    void askQuestion(QuestionModel question);

    void resetSkipNum();
}
