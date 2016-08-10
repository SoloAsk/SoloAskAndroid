package com.soloask.android.search.interactor;

import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchMoreInteractor extends BaseInteractor {
    void getSearchResult(boolean isQuestion, String keyword, SearchResultResponseListener listener);

    interface SearchResultResponseListener extends BaseResponseListener {
        void OnSearchQuestionSuccess(List<Question> list);

        void OnSearchPersonSuccess(List<User> list);
    }
}
