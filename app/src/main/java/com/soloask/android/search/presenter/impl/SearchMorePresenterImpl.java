package com.soloask.android.search.presenter.impl;

import android.util.Log;

import com.soloask.android.R;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.search.interactor.SearchMoreInteractor;
import com.soloask.android.search.presenter.SearchMorePresenter;
import com.soloask.android.search.view.SearchMoreView;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-9.
 */
public class SearchMorePresenterImpl implements SearchMorePresenter, SearchMoreInteractor.SearchResultResponseListener {
    private SearchMoreView mView;
    private SearchMoreInteractor mInteractor;

    @Inject
    public SearchMorePresenterImpl(SearchMoreView view, SearchMoreInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getSearchResults(boolean isQuestion, String keyword) {
        if (mView == null || mInteractor == null || mInteractor.isLoading()) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            if (mInteractor.getSkipNum() <= 0) {
                mView.showNetworkError(true);
            }
            mView.showProgress(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showNetworkError(false);
            mInteractor.setIsLoading(true);
            mInteractor.getSearchResult(isQuestion, keyword, this);
            if (mInteractor.getSkipNum() <= 0) {
                mView.showProgress(true);
            }
        }
    }

    @Override
    public void resetSkipNum() {
        if (mInteractor != null) {
            mInteractor.resetSkipNum();
        }
    }

    @Override
    public void OnSearchQuestionSuccess(List<QuestionModel> list) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (list.size() > 0) {
            mView.showSearchQuestions(list);
            mInteractor.setSkipNum(list.size());
        } else {
            if (mInteractor.getSkipNum() > 0) {
                mView.showToast(R.string.toast_no_more);
            }
        }
        mView.showProgress(false);
        mView.showNetworkError(false);
        mInteractor.setIsLoading(false);
    }

    @Override
    public void OnSearchPersonSuccess(List<UserModel> list) {
        if (mView == null || mInteractor == null) {
            return;
        }
        Log.i("Lebron", "size " + list.size());
        if (list.size() > 0) {
            mView.showSearchPersons(list);
            mInteractor.setSkipNum(list.size());
        } else {
            if (mInteractor.getSkipNum() > 0) {
                mView.showToast(R.string.toast_no_more);
            }
        }
        mView.showProgress(false);
        mView.showNetworkError(false);
        mInteractor.setIsLoading(false);
    }

    @Override
    public void onResponseFailed() {
        if (mView == null) {
            return;
        }
        if (mInteractor.getSkipNum() <= 0) {
            mView.showNetworkError(true);
        }
        mInteractor.setIsLoading(false);
        mView.showProgress(false);
    }
}
