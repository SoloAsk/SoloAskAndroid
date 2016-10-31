package com.soloask.android.common.base;

import com.soloask.android.MainApplication;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiService;
import com.soloask.android.common.network.ApiWrapper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 16-10-27.
 */

@Module
public class ApiModule {
    @Provides
    @Singleton
    ApiWrapper getApiWrapper(ApiService service) {
        return new ApiWrapper(service);
    }

    @Provides
    @Singleton
    CompositeSubscription getCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @Singleton
    ApiService getApiService(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    OkHttpClient getOkHttpClient(HttpLoggingInterceptor interceptor, Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
        return client;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor getHttpLoggingInterceptor() {
        // log拦截器  打印所有的log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    Cache getCache() {
        //设置 请求的缓存
        File cacheFile = new File(MainApplication.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb
        return cache;
    }
}
