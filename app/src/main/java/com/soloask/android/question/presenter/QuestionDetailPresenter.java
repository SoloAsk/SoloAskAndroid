package com.soloask.android.question.presenter;

import com.soloask.android.common.base.BasePresenter;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-10.
 */
public interface QuestionDetailPresenter extends BasePresenter {
    void getCurrentUser(String userId);

    void checkUserHeard(Question question, User user);

    void getQuestionDetail(String questionId);
}
