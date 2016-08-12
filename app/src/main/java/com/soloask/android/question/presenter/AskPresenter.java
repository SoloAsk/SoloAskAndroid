package com.soloask.android.question.presenter;

import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-11.
 */
public interface AskPresenter {
    void getCurrentUser(String userId);

    void getRespondentInfo(String userId);

    void getRespondentRelatedQuestions(User user);

    void askQuestion(Question question);

    void resetSkipNum();
}
