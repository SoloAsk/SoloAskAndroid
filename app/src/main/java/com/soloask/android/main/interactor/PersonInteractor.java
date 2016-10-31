package com.soloask.android.main.interactor;

import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.base.BaseInteractor;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-5.
 */
public interface PersonInteractor extends BaseInteractor {
    void getPopularPersonData(PersonResponseListener listener);

    interface PersonResponseListener extends BaseResponseListener {
        void onResponseSuccess(List<UserModel> list);
    }
}
