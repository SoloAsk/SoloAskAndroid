package com.soloask.android.search.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchInteractor {
    void getPersonsData(String keyword, int size, SearchResponseListener listener);

    void getQuestionsData(String keyword, int size, SearchResponseListener listener);

    interface SearchResponseListener extends BaseInteractor.BaseResponseListener {
        void OnSearchPersonSuccess(List<UserModel> list);

        void OnSearchQuestionSuccess(List<QuestionModel> list);
    }
}
