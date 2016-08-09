package com.soloask.android.account.interactor.impl;

import com.soloask.android.account.interactor.MyCommonInteractor;
import com.soloask.android.data.bmob.MineManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lebron on 16-8-8.
 */
public class MyCommonInteractorImpl implements MyCommonInteractor {
    private boolean isLoading;
    private int mSkipNum = 0;
    private MineManager mManager;

    @Inject
    public MyCommonInteractorImpl() {
    }

    @Override
    public void getMyQuestionsData(User user, int type, final MyCommonResponseListener listener) {
        if (mManager == null) {
            mManager = new MineManager(user);
        }
        mManager.setOnGetQuestionListener(new MineManager.OnGetQuestionListener() {
            @Override
            public void onSuccess(List<Question> list) {
                listener.OnQuestionsResponseSuccess(list);
            }

            @Override
            public void onFailed() {
                listener.onResponseFailed();
            }
        });
        mManager.getQuestions(mSkipNum, type);
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
        mSkipNum = skipNum;
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
