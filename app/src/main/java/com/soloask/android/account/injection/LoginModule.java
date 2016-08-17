package com.soloask.android.account.injection;

import com.soloask.android.account.interactor.LoginInteractor;
import com.soloask.android.account.interactor.impl.LoginInteractorImpl;
import com.soloask.android.account.presenter.LoginPresenter;
import com.soloask.android.account.presenter.impl.LoginPresenterImpl;
import com.soloask.android.account.view.LoginView;
import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by LeBron on 2016/8/6.
 */
@Module
public class LoginModule extends BaseModule<LoginView> {
    public LoginModule(LoginView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    LoginView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    LoginInteractor provideInteractor(LoginInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    LoginPresenter providePresenter(LoginPresenterImpl presenter) {
        return presenter;
    }
}
