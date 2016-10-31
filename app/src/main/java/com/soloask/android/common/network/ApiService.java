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

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lebron on 16-10-27.
 */

public interface ApiService {
    @POST("api/user/login")
    Observable<UserResponse> userLogin(@Body LoginRequest request);

    @POST("api/user/info")
    Observable<UserResponse> getUserInfo(@Body UserRequest request);

    @POST("api/user/update")
    Observable<UserResponse> updateUser(@Body UpdateUserRequest request);

    @POST("api/user/list")
    Observable<UserListResponse> getUserList(@Body DiscoverRequest request);

    @POST("api/question/hot")
    Observable<QuesListResponse> getHotQues(@Body HotRequest request);

    @POST("api/question/related")
    Observable<QuesListResponse> getRelatedQues(@Body MineQuesRequest request);

    @POST("api/heard/list")
    Observable<QuesListResponse> getListenedQues(@Body MineQuesRequest request);

    @POST("api/withdraw/create")
    Observable<WithDrawResponse> createWithDraw(@Body WithDrawRequest request);
}
