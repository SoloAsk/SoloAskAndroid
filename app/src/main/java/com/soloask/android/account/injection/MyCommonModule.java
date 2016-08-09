package com.soloask.android.account.injection;

import com.soloask.android.account.interactor.MyCommonInteractor;
import com.soloask.android.account.interactor.impl.MyCommonInteractorImpl;
import com.soloask.android.account.presenter.MyCommonPresenter;
import com.soloask.android.account.presenter.impl.MyCommonPresenterImpl;
import com.soloask.android.account.view.MyCommonView;
import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-8.
 */
@Module
public class MyCommonModule extends BaseModule<MyCommonView> {
    public MyCommonModule(MyCommonView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    MyCommonView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    MyCommonInteractor provideUserInteractor(MyCommonInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    MyCommonPresenter provideUserPresenter(MyCommonPresenterImpl presenter) {
        return presenter;
    }
}
