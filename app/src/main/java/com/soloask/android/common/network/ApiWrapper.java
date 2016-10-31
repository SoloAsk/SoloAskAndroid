package com.soloask.android.common.network;

import com.soloask.android.common.network.request.account.LoginRequest;
import com.soloask.android.common.network.request.account.MineQuesRequest;
import com.soloask.android.common.network.request.account.UpdateUserRequest;
import com.soloask.android.common.network.request.account.UserRequest;
import com.soloask.android.common.network.request.account.WithDrawRequest;
import com.soloask.android.common.network.request.main.DiscoverRequest;
import com.soloask.android.common.network.request.main.HotRequest;
import com.soloask.android.common.network.response.WithDrawResponse;
import com.soloask.android.common.network.response.account.UserResponse;
import com.soloask.android.common.network.response.main.UserListResponse;
import com.soloask.android.common.network.response.main.QuesListResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lebron on 16-10-27.
 */

public class ApiWrapper extends Api {
    private ApiService mService;

    @Inject
    public ApiWrapper(ApiService service) {
        mService = service;
    }

    public Observable<UserResponse> userLogin(LoginRequest request) {
        return applySchedulers(mService.userLogin(request));
    }

    public Observable<UserResponse> getUserInfo(UserRequest request) {
        return applySchedulers(mService.getUserInfo(request));
    }

    public Observable<UserResponse> updateUser(UpdateUserRequest request) {
        return applySchedulers(mService.updateUser(request));
    }

    public Observable<QuesListResponse> getHotQues(HotRequest request) {
        return applySchedulers(mService.getHotQues(request));
    }

    public Observable<UserListResponse> getUserList(DiscoverRequest request) {
        return applySchedulers(mService.getUserList(request));
    }

    public Observable<QuesListResponse> getRelatedQuesList(MineQuesRequest request) {
        return applySchedulers(mService.getRelatedQues(request));
    }

    public Observable<QuesListResponse> getListenedQuesList(MineQuesRequest request) {
        return applySchedulers(mService.getListenedQues(request));
    }

    public Observable<WithDrawResponse> createWithDraw(WithDrawRequest request) {
        return applySchedulers(mService.createWithDraw(request));
    }
}
