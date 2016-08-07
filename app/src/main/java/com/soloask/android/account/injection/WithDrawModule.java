package com.soloask.android.account.injection;

import com.soloask.android.account.interactor.WithDrawInteractor;
import com.soloask.android.account.interactor.impl.WithDrawInteractorImpl;
import com.soloask.android.account.presenter.WithDrawPresenter;
import com.soloask.android.account.presenter.impl.WithDrawPresenterImpl;
import com.soloask.android.account.view.WithDrawView;
import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by LeBron on 2016/8/6.
 */
@Module
public class WithDrawModule extends BaseModule<WithDrawView> {
    public WithDrawModule(WithDrawView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    WithDrawView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    WithDrawInteractor provideUserInteractor(WithDrawInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    WithDrawPresenter provideUserPresenter(WithDrawPresenterImpl presenter) {
        return presenter;
    }
}
