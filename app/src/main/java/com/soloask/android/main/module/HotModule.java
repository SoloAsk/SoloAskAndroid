package com.soloask.android.main.module;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.main.interactor.HotInteractor;
import com.soloask.android.main.interactor.impl.HotInteractorImpl;
import com.soloask.android.main.presenter.HotPresenter;
import com.soloask.android.main.presenter.impl.HotPresenterImpl;
import com.soloask.android.main.view.HotView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-4.
 */
@Module
public class HotModule extends BaseModule<HotView> {
    public HotModule(HotView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    HotView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    HotInteractor provideHotInteractor(HotInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    HotPresenter provideHotPresenter(HotPresenterImpl presenter) {
        return presenter;
    }
}
