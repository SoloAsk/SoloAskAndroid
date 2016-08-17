package com.soloask.android.search.interactor.impl;

import com.soloask.android.data.bmob.SearchManager;
import com.soloask.android.search.interactor.SearchMoreInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-9.
 */
public class SearchMoreInteractorImpl implements SearchMoreInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private SearchManager mManager;

    @Inject
    public SearchMoreInteractorImpl() {
    }

    @Override
    public void getSearchResult(boolean isQuestion, String keyword, final SearchResultResponseListener listener) {
        if (mManager == null) {
            mManager = new SearchManager();
        }
        if (isQuestion) {
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
            mManager.getSearchQuestions(keyword, 10, mSkipNum);
        } else {
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
            mManager.getSearchUsers(keyword, 10, mSkipNum);
        }

    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public int getSkipNum() {
        return mSkipNum;
    }

    @Override
    public void setSkipNum(int skipNum) {
        mSkipNum += skipNum;
    }

    @Override
    public void resetSkipNum() {
        mSkipNum = 0;
    }
}
