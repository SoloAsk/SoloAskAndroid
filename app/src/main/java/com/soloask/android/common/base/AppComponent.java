package com.soloask.android.common.base;


import com.soloask.android.main.component.HotComponent;
import com.soloask.android.main.module.HotModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lebron on 16-8-4.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    HotComponent plus(HotModule module);
}
