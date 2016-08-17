package com.soloask.android.account.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-8.
 */
public interface MyCommonInteractor extends BaseInteractor {
    void getMyQuestionsData(User user, int type, MyCommonResponseListener listener);

    interface MyCommonResponseListener extends BaseInteractor.BaseResponseListener {
        void OnQuestionsResponseSuccess(List<Question> list);
    }
}
