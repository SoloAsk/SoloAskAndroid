package com.soloask.android.account.injection;

import com.soloask.android.account.view.impl.UserActivity;
import com.soloask.android.common.base.ActivityScoped;
import com.squareup.otto.Bus;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-5.
 */
@ActivityScoped
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    void inject(UserActivity userActivity);
    Bus provideBus();
}
