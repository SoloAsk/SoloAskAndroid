package com.soloask.android.question.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-11.
 */
public interface AskView extends BaseView {
    /**
     * 是否显示无网络界面
     *
     * @param show
     */
    void showNetworkError(boolean show);

    /**
     * 得到当前用户
     *
     * @param user
     */
    void getCurrentUser(User user);

    /**
     * 显示当前回答用户
     *
     * @param user
     */
    void showCurrentRespondentInfo(User user);

    /**
     * 得到当前回答用户历史问题列表
     *
     * @param questions
     */
    void showRelatedQuestions(List<Question> questions);

    /**
     * 提问成功
     *
     * @param questionId
     */
    void askSuccess(String questionId);


}
