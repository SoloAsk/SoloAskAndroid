package com.soloask.android.search.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.search.interactor.SearchMoreInteractor;
import com.soloask.android.search.interactor.impl.SearchMoreInteractorImpl;
import com.soloask.android.search.presenter.SearchMorePresenter;
import com.soloask.android.search.presenter.impl.SearchMorePresenterImpl;
import com.soloask.android.search.view.SearchMoreView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-9.
 */
@Module
public class SearchMoreModule extends BaseModule<SearchMoreView> {
    public SearchMoreModule(SearchMoreView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    SearchMoreView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    SearchMoreInteractor provideInteractor(SearchMoreInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    SearchMorePresenter providePresenter(SearchMorePresenterImpl presenter) {
        return presenter;
    }
}
