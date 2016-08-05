package com.soloask.android.main.interactor.impl;

import com.soloask.android.data.bmob.DiscoverManager;
import com.soloask.android.data.model.User;
import com.soloask.android.main.interactor.PersonInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-5.
 */
public class PersonInteractorImpl implements PersonInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private DiscoverManager mManager;

    @Inject
    public PersonInteractorImpl() {
    }

    @Override
    public void getPopularPersonData(final PersonResponseListener listener) {
        if (mManager == null) {
            mManager = new DiscoverManager();
        }
        mManager.setOnGetDiscoverListener(new DiscoverManager.OnGetDiscoverListener() {
            @Override
            public void onSuccess(List<User> list) {
                listener.onResponseSuccess(list);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.getUserList(mSkipNum);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void setSkipNum(int skipNum) {
        mSkipNum += skipNum;
    }

    @Override
    public int getSkipNum() {
        return mSkipNum;
    }

    @Override
    public void resetSkipNum() {
        mSkipNum = 0;
    }
}
