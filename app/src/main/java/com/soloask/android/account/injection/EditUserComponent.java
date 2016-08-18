package com.soloask.android.account.injection;

import com.soloask.android.account.view.impl.EditUserActivity;
import com.soloask.android.common.base.ActivityScoped;
import com.squareup.otto.Bus;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-5.
 */
@ActivityScoped
@Subcomponent(modules = EditUserModule.class)
public interface EditUserComponent {
    void inject(EditUserActivity editUserActivity);
    Bus provideBus();
}
