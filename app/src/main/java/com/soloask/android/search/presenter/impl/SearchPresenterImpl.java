package com.soloask.android.search.presenter.impl;

import com.soloask.android.R;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.search.interactor.SearchInteractor;
import com.soloask.android.search.presenter.SearchPresenter;
import com.soloask.android.search.view.SearchView;
import com.soloask.android.util.NetworkManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-9.
 */
public class SearchPresenterImpl implements SearchPresenter, SearchInteractor.SearchResponseListener {
    private SearchView mView;
    private SearchInteractor mInteractor;
    private boolean isUserEmpty, isQuestionEmpty;

    @Inject
    public SearchPresenterImpl(SearchView view, SearchInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onResponseFailed() {
        if (mView != null) {
            mView.showProgress(false);
            mView.showToast(R.string.failed_to_load_data);
        }
    }

    @Override
    public void getPersonsData(String keyword, int size) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showNetworkError(true);
            mView.showProgress(false);
            mView.showEmptyLayout(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showProgress(true);
            mView.showNetworkError(false);
            mView.showEmptyLayout(false);
            mInteractor.getPersonsData(keyword, size, this);
        }
    }

    @Override
    public void getQuestionsData(String keyword, int size) {
        if (mView == null || mInteractor == null) {
            return;
        }
        if (!NetworkManager.isNetworkValid(mView.getViewContext())) {
            mView.showNetworkError(true);
            mView.showProgress(false);
            mView.showEmptyLayout(false);
            mView.showToast(R.string.failed_to_load_data);
        } else {
            mView.showProgress(true);
            mView.showNetworkError(false);
            mView.showEmptyLayout(false);
            mInteractor.getQuestionsData(keyword, size, this);
        }
    }

    @Override
    public void OnSearchPersonSuccess(List<User> list) {
        isUserEmpty = list.size() <= 0;
        if (mView != null) {
            mView.showPersons(list);
            mView.showProgress(false);
            if (isUserEmpty && isQuestionEmpty) {
                mView.showEmptyLayout(true);
            }
        }
    }

    @Override
    public void OnSearchQuestionSuccess(List<Question> list) {
        isQuestionEmpty = list.size() <= 0;
        if (mView != null) {
            mView.showQuestions(list);
            mView.showProgress(false);
            if (isUserEmpty && isQuestionEmpty) {
                mView.showEmptyLayout(true);
            }
        }
    }
}
