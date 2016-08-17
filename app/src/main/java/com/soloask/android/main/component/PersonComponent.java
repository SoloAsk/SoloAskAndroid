package com.soloask.android.main.component;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.main.module.PersonModule;
import com.soloask.android.main.view.impl.PersonFragment;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-5.
 */
@ActivityScoped
@Subcomponent(modules = PersonModule.class)
public interface PersonComponent {
    void inject(PersonFragment personFragment);
}
