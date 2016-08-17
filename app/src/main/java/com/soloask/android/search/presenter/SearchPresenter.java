package com.soloask.android.search.presenter;

/**
 * Created by lebron on 16-8-9.
 */
public interface SearchPresenter {
    void getPersonsData(String keyword, int size);

    void getQuestionsData(String keyword, int size);
}
