package com.soloask.android.main.view;

import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by lebron on 16-8-4.
 */
public interface HotView extends BaseView {

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
     * 显示PopularLives数据列表
     *
     * @param questionList
     */
    void showHotQuestions(List<Question> questionList);

    /**
     * 获取数据个数
     */
    int getDataSize();

}
