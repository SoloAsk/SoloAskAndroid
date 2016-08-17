package com.soloask.android.question.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.question.interactor.QuestionDetailInteractor;
import com.soloask.android.question.interactor.impl.QuestionDetailInteractorImpl;
import com.soloask.android.question.presenter.QuestionDetailPresenter;
import com.soloask.android.question.presenter.impl.QuestionDetailPresenterImpl;
import com.soloask.android.question.view.QuestionDetailView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-10.
 */
@Module
public class QuestionDetailModule extends BaseModule<QuestionDetailView> {
    public QuestionDetailModule(QuestionDetailView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    QuestionDetailView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    QuestionDetailInteractor provideInteractor(QuestionDetailInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    QuestionDetailPresenter providePresenter(QuestionDetailPresenterImpl presenter) {
        return presenter;
    }
}
