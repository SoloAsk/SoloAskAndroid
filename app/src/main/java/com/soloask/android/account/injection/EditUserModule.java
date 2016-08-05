package com.soloask.android.account.injection;

import com.soloask.android.account.interactor.EditUserInteractor;
import com.soloask.android.account.interactor.impl.EditUserInteractorImpl;
import com.soloask.android.account.presenter.EditUserPresenter;
import com.soloask.android.account.presenter.impl.EditUserPresenterImpl;
import com.soloask.android.account.view.EditUserView;
import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.common.base.BaseModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-5.
 */
@Module
public class EditUserModule extends BaseModule<EditUserView> {
    public EditUserModule(EditUserView view) {
        super(view);
    }

    @Provides
    @ActivityScoped
    EditUserView provideView() {
        return mView;
    }

    @Provides
    @ActivityScoped
    EditUserInteractor provideUserInteractor(EditUserInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScoped
    EditUserPresenter provideUserPresenter(EditUserPresenterImpl presenter) {
        return presenter;
    }
}
