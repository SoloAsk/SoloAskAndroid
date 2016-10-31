package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-8.
 */
public interface MyCommonInteractor extends BaseInteractor {
    void getMyQuestionsData(String user_id, int type, MyCommonResponseListener listener);

    interface MyCommonResponseListener extends BaseInteractor.BaseResponseListener {
        void OnQuestionsResponseSuccess(List<QuestionModel> list);
    }
}
