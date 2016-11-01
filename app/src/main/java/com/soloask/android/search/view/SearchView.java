package com.soloask.android.search.view;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseView;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchView extends BaseView {
    /**
     * 是否显示无网络界面
     *
     * @param show
     */
    void showNetworkError(boolean show);

    /**
     * 是否显示未搜索到内容界面
     *
     * @param show
     */
    void showEmptyLayout(boolean show);

    /**
     * 是否显示进度条
     *
     * @param show
     */
    void showProgress(boolean show);

    /**
     * 显示搜到的问题列表
     *
     * @param questionList
     */
    void showQuestions(List<QuestionModel> questionList);

    /**
     * 显示搜到的用户列表
     *
     * @param personList
     */
    void showPersons(List<UserModel> personList);
}
