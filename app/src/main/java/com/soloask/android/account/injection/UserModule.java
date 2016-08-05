package com.soloask.android.account.injection;

import com.soloask.android.account.interactor.UserInteractor;
import com.soloask.android.account.interactor.impl.UserInteractorImpl;
import com.soloask.android.account.presenter.UserPresenter;
import com.soloask.android.account.presenter.impl.UserPresenterImpl;
import com.soloask.android.account.view.UserView;
import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-5.
 */
@Module
public class UserModule extends BaseModule<UserView> {
    public UserModule(UserView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    UserView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    UserInteractor provideUserInteractor(UserInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    UserPresenter provideUserPresenter(UserPresenterImpl presenter) {
        return presenter;
    }
}
