package com.soloask.android.question.presenter;

import com.soloask.android.data.model.Question;

/**
 * Created by lebron on 16-8-10.
 */
public interface AnswerPresenter {
    void getQuestionDetail(String questionId);

    void uploadAnswer(Question question, String filePath);
}
