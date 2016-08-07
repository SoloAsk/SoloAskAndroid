package com.soloask.android.account.injection;

import com.soloask.android.account.view.impl.LoginActivity;
import com.soloask.android.common.base.ActivityScoped;

import dagger.Subcomponent;

/**
 * Created by LeBron on 2016/8/6.
 */
@ActivityScoped
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
