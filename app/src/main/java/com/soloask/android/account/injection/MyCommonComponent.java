package com.soloask.android.account.injection;

import com.soloask.android.account.view.impl.MyCommonActivity;
import com.soloask.android.common.base.ActivityScoped;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-8.
 */
@ActivityScoped
@Subcomponent(modules = MyCommonModule.class)
public interface MyCommonComponent {
    void inject(MyCommonActivity myCommonActivity);
}
