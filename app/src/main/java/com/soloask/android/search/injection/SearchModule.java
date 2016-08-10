package com.soloask.android.search.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.search.interactor.SearchInteractor;
import com.soloask.android.search.interactor.impl.SearchInteractorImpl;
import com.soloask.android.search.presenter.SearchPresenter;
import com.soloask.android.search.presenter.impl.SearchPresenterImpl;
import com.soloask.android.search.view.SearchView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-9.
 */
@Module
public class SearchModule extends BaseModule<SearchView> {
    public SearchModule(SearchView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    SearchView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    SearchInteractor provideUserInteractor(SearchInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    SearchPresenter provideUserPresenter(SearchPresenterImpl presenter) {
        return presenter;
    }
}
