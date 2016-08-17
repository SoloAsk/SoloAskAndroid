package com.soloask.android.account.injection;

import com.soloask.android.account.view.impl.WithDrawActivity;
import com.soloask.android.common.base.ActivityScoped;

import dagger.Subcomponent;

/**
 * Created by LeBron on 2016/8/6.
 */
@ActivityScoped
@Subcomponent(modules = WithDrawModule.class)
public interface WithDrawComponent {
    void inject(WithDrawActivity activity);
}
