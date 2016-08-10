package com.soloask.android.question.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

/**
 * Created by lebron on 16-8-10.
 */
public interface QuestionDetailView extends BaseView {
    /**
     * 是否显示无网络界面
     *
     * @param show
     */
    void showNetworkError(boolean show);

    /**
     * 是否显示进度条
     *
     * @param show
     */
    void showProgress(boolean show);

    /**
     * 显示当前用户
     *
     * @param user
     */
    void showCurrentUser(User user);

    /**
     * 显示当前用户是否听过此问题
     *
     * @param heard
     */
    void showUserHeard(boolean heard);

    /**
     * 显示问题详情
     *
     * @param question
     */
    void showQuestionDetail(Question question);
}
