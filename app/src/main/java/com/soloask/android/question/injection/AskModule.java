package com.soloask.android.question.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.question.interactor.AskInteractor;
import com.soloask.android.question.interactor.impl.AskInteractorImpl;
import com.soloask.android.question.presenter.AskPresenter;
import com.soloask.android.question.presenter.impl.AskPresenterImpl;
import com.soloask.android.question.view.AskView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-11.
 */
@Module
public class AskModule extends BaseModule<AskView> {
    public AskModule(AskView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    AskView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    AskInteractor provideInteractor(AskInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    AskPresenter providePresenter(AskPresenterImpl presenter) {
        return presenter;
    }
}
