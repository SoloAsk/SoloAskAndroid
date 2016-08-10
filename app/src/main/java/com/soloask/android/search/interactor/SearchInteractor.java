package com.soloask.android.search.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchInteractor {
    void getPersonsData(String keyword, int size, SearchResponseListener listener);

    void getQuestionsData(String keyword, int size, SearchResponseListener listener);

    interface SearchResponseListener extends BaseInteractor.BaseResponseListener {
        void OnSearchPersonSuccess(List<User> list);

        void OnSearchQuestionSuccess(List<Question> list);
    }
}
