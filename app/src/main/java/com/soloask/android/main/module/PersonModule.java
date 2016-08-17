package com.soloask.android.main.module;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;
import com.soloask.android.main.interactor.PersonInteractor;
import com.soloask.android.main.interactor.impl.PersonInteractorImpl;
import com.soloask.android.main.presenter.PersonPresenter;
import com.soloask.android.main.presenter.impl.PersonPresenterImpl;
import com.soloask.android.main.view.PersonView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-5.
 */
@Module
public class PersonModule extends BaseModule<PersonView> {
    public PersonModule(PersonView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    PersonView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    PersonInteractor providePersonInteractor(PersonInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    PersonPresenter providePersonPresenter(PersonPresenterImpl presenter) {
        return presenter;
    }
}
