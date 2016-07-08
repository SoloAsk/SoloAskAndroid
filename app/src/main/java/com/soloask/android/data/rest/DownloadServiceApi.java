package com.soloask.android.data.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Lebron on 2016/7/7.
 */
public interface DownloadServiceApi {
    @GET
    Call<ResponseBody> getSmallSizeFile(@Url String fileUrl);
}
