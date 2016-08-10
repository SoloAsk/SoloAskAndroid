package com.soloask.android.search.presenter;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchMorePresenter {
    void getSearchResults(boolean isQuestion,String keyword);

    void resetSkipNum();
}
