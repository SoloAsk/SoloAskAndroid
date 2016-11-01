package com.soloask.android.search.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.question.model.QuestionModel;

import java.util.List;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchMoreInteractor extends BaseInteractor {
    void getSearchResult(boolean isQuestion, String keyword, SearchResultResponseListener listener);

    interface SearchResultResponseListener extends BaseResponseListener {
        void OnSearchQuestionSuccess(List<QuestionModel> list);

        void OnSearchPersonSuccess(List<UserModel> list);
    }
}
