package com.soloask.android.question.presenter;

import com.soloask.android.common.base.BasePresenter;

/**
 * Created by lebron on 16-8-10.
 */
public interface QuestionDetailPresenter extends BasePresenter {

    void checkUserHeard(String question, String user);

    void getQuestionDetail(String questionId);

    void setHeardUser(String question, String user);
}
