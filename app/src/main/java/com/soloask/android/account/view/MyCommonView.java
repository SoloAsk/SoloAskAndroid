package com.soloask.android.account.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-8.
 */
public interface MyCommonView extends BaseView {
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
     * 显示我的数据列表
     *
     * @param questionList
     */
    void showMyQuestions(List<QuestionModel> questionList);

    /**
     * 获取数据个数
     */
    int getDataSize();
}
