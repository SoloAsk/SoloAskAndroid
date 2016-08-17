package com.soloask.android;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.soloask.android.common.base.AppComponent;
import com.soloask.android.common.base.AppModule;
import com.soloask.android.common.base.DaggerAppComponent;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Lebron on 2016/6/22.
 */
public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Kh1BVmKuwtAB9mwpRxXHXKumR";
    private static final String TWITTER_SECRET = "PhHsC0kSrhXPnHjqBy0rtQIAN9wpSACYFJ0QZqdsGd7h6NpOO4";
    private static MainApplication mInstance;
    private AppComponent mAppComponent;

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //Fabric
/*        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));*/
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //Umeng
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
