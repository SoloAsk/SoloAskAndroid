package com.soloask.android.main.component;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.main.module.HotModule;
import com.soloask.android.main.view.impl.HotFragment;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-4.
 */
@ActivityScoped
@Subcomponent(modules = HotModule.class)
public interface HotComponent {
    void inject(HotFragment hotFragment);
}
