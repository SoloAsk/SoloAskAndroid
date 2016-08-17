package com.soloask.android.search.injection;

import com.soloask.android.common.base.ActivityScoped;
import com.soloask.android.search.view.impl.SearchMoreActivity;

import dagger.Subcomponent;

/**
 * Created by lebron on 16-8-9.
 */
@ActivityScoped
@Subcomponent(modules = SearchMoreModule.class)
public interface SearchMoreComponent {
    void inject(SearchMoreActivity activity);
}
