package com.soloask.android.search.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchMoreView extends BaseView {
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
     * 显示搜索到的用户列表
     *
     * @param list
     */
    void showSearchPersons(List<User> list);

    /**
     * 显示搜索到的问题列表
     *
     * @param list
     */
    void showSearchQuestions(List<Question> list);
}
