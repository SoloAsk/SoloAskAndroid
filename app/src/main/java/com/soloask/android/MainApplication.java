package com.soloask.android;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.umeng.analytics.MobclickAgent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

/**
 * Created by Lebron on 2016/6/22.
 */
public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Kh1BVmKuwtAB9mwpRxXHXKumR";
    private static final String TWITTER_SECRET = "PhHsC0kSrhXPnHjqBy0rtQIAN9wpSACYFJ0QZqdsGd7h6NpOO4";

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //Umeng
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        //FileDownloader
        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not necessary
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(15_000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });
    }
}
