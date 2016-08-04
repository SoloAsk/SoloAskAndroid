package com.soloask.android.common.base;

import android.content.res.Resources;

import com.soloask.android.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lebron on 16-8-4.
 */
@Module
public class AppModule {
    MainApplication mApplication;

    public AppModule(MainApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    protected MainApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    protected Resources provideResources() {
        return mApplication.getResources();
    }
}
