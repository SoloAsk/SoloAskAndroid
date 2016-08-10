package com.soloask.android.search.interactor.impl;

import com.soloask.android.data.bmob.SearchManager;
import com.soloask.android.search.interactor.SearchInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-9.
 */
public class SearchInteractorImpl implements SearchInteractor {
    private SearchManager mManager;
    private int mSkipNum = 0;

    @Inject
    public SearchInteractorImpl() {
    }

    @Override
    public void getPersonsData(String keyword, int size, final SearchResponseListener listener) {
        if (mManager == null) {
            mManager = new SearchManager();
        }
        mManager.setOnSearchUserListener(new SearchManager.OnSearchUserListener() {
            @Override
            public void onSuccess(List users) {
                listener.OnSearchPersonSuccess(users);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.getSearchUsers(keyword, 3, mSkipNum);
    }

    @Override
    public void getQuestionsData(String keyword, int size, final SearchResponseListener listener) {
        if (mManager == null) {
            mManager = new SearchManager();
        }
        mManager.setOnSearchQuestionListener(new SearchManager.OnSearchQuestionListener() {
            @Override
            public void onSuccess(List questions) {
                listener.OnSearchQuestionSuccess(questions);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.getSearchQuestions(keyword, 3, mSkipNum);
    }
}
