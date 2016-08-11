package com.soloask.android.question.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.question.view.impl.AnswerActivity;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-10.
 */
@ActivityScoped
@Subcomponent(modules = AnswerModule.class)
public interface AnswerComponent {
    void inject(AnswerActivity activity);
}
