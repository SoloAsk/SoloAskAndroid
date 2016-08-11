package com.soloask.android.question.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.question.interactor.AnswerInteractor;
import com.soloask.android.question.interactor.impl.AnswerInteractorImpl;
import com.soloask.android.question.presenter.AnswerPresenter;
import com.soloask.android.question.presenter.impl.AnswerPresenterImpl;
import com.soloask.android.question.view.AnswerView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-10.
 */
@Module
public class AnswerModule extends BaseModule<AnswerView> {

    public AnswerModule(AnswerView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    AnswerView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    AnswerInteractor provideInteractor(AnswerInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    AnswerPresenter providePresenter(AnswerPresenterImpl presenter) {
        return presenter;
    }
}
