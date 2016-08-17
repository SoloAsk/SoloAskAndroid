package com.soloask.android.question.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.question.view.impl.UserProfileActivity;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-11.
 */
@ActivityScoped
@Subcomponent(modules = AskModule.class)
public interface AskComponent {
    void inject(UserProfileActivity activity);
}
