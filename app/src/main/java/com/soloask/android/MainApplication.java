package com.soloask.android;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.soloask.android.common.base.ApiModule;
import com.soloask.android.common.base.AppComponent;
import com.soloask.android.common.base.AppModule;
import com.soloask.android.common.base.DaggerAppComponent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * Created by Lebron on 2016/6/22.
 */
public class MainApplication extends Application {

    private AppComponent mAppComponent;
    private static MainApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //Umeng
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule()).build();
    }

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public static MainApplication getInstance() {
        return mInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
