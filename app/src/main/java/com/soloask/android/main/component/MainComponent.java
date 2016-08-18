package com.soloask.android.main.component;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.main.module.MainModule;
import com.soloask.android.main.view.impl.MainActivity;
import com.squareup.otto.Bus;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-18.
 */
@ActivityScoped
@Subcomponent(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);

    Bus provideBus();
}
