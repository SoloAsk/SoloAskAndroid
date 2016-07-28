package com.soloask.android;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Lebron on 2016/6/22.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //Umeng
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
