package com.soloask.android.main.interactor;

import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by lebron on 16-8-4.
 */
public interface HotInteractor {
    boolean isLoading();

    void setIsLoading(boolean isLoading);

    void setSkipNum(int skipNum);

    int getSkipNum();

    void resetSkipNum();

    void getHotQuestionsData(HotQuestionResponseListener listener);

    interface HotQuestionResponseListener {
        void onResponseSuccess(List<Question> list);

        void onResponseFailed();
    }
}
