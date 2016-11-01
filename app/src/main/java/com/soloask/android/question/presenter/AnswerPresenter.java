package com.soloask.android.question.presenter;

/**
 * Created by lebron on 16-8-10.
 */
public interface AnswerPresenter {
    void getQuestionDetail(String questionId);

    void uploadAnswer(String questionId, String filepath, int length);
}
