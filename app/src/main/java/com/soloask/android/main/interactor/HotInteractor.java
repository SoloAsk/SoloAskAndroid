package com.soloask.android.main.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;

import java.util.List;

/**
 * Created by lebron on 16-8-4.
 */
public interface HotInteractor extends BaseInteractor {

    void getHotQuestionsData(HotQuestionResponseListener listener);

    interface HotQuestionResponseListener extends BaseResponseListener {
        void onResponseSuccess(List<Question> list);
    }
}
