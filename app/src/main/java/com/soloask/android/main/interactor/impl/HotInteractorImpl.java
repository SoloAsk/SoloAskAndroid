package com.soloask.android.main.interactor.impl;

import com.soloask.android.data.bmob.HotManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.main.interactor.HotInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-4.
 */
public class HotInteractorImpl implements HotInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private HotManager mHotManager;

    @Inject
    public HotInteractorImpl() {
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

    @Override
    public void getHotQuestionsData(final HotQuestionResponseListener listener) {
        if (mHotManager == null) {
            mHotManager = new HotManager();
        }
        mHotManager.setOnGetHotListener(new HotManager.OnGetHotListener() {
            @Override
            public void onSuccess(List<Question> list) {
                listener.onResponseSuccess(list);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mHotManager.getHotList(mSkipNum);
    }
}
