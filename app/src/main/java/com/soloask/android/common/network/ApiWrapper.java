package com.soloask.android.common.network;

import com.soloask.android.common.network.request.main.HotRequest;
import com.soloask.android.common.network.response.main.HotResponse;

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

    public Observable<HotResponse> getHotQues(HotRequest request) {
        return applySchedulers(mService.getHotQues(request));
    }
}
