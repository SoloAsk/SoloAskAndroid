package com.soloask.android.common.network;

import com.soloask.android.common.network.request.BaseRequest;
import com.soloask.android.common.network.request.main.HotRequest;
import com.soloask.android.common.network.response.BaseResponse;
import com.soloask.android.common.network.response.main.HotResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lebron on 16-10-27.
 */

public interface ApiService {
    @POST("api/user/info")
    Observable<BaseResponse> getUserInfo(@Body BaseRequest request);

    @POST("api/question/hot")
    Observable<HotResponse> getHotQues(@Body HotRequest request);
}
