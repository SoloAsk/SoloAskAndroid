package com.soloask.android.account.interactor.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.soloask.android.account.interactor.LoginInteractor;
import com.soloask.android.common.network.ApiConstant;
import com.soloask.android.common.network.ApiResponseHandler;
import com.soloask.android.common.network.ApiSubscriber;
import com.soloask.android.common.network.ApiWrapper;
import com.soloask.android.common.network.request.account.LoginRequest;
import com.soloask.android.common.network.response.account.UserResponse;
import com.umeng.message.UmengRegistrar;

import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LeBron on 2016/8/6.
 */
public class LoginInteractorImpl implements LoginInteractor {
    private LoginRequest mRequest;
    private ApiWrapper mApiWrapper;
    private CompositeSubscription mSub;

    @Inject
    LoginInteractorImpl(ApiWrapper apiWrapper, CompositeSubscription subscription) {
        mApiWrapper = apiWrapper;
        mSub = subscription;
    }

    @Override
    public void doLogin(final Context context, CallbackManager manager, final LoginResponseListener listener) {
        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String device_token = UmengRegistrar.getRegistrationId(context);
                Log.i("LoginInteractor", device_token);
                getLoginInfo(listener, loginResult.getAccessToken(), device_token);
            }

            @Override
            public void onCancel() {
                listener.OnLoginResponseCancel();
            }

            @Override
            public void onError(FacebookException e) {
                listener.onResponseFailed();
            }
        });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
        }
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(final LoginResponseListener listener, AccessToken accessToken, final String deviceToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");
                    final String name = object.optString("name");
                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    final String photo = object_data.optString("url");
                    if (mRequest == null) {
                        mRequest = new LoginRequest();
                    }
                    mRequest.setThirdId(id);
                    mRequest.setIcon(photo);
                    mRequest.setUserName(name);
                    mRequest.setDeviceToken(deviceToken);
                    ApiResponseHandler.CustomHandler<UserResponse> handler = new ApiResponseHandler.CustomHandler<UserResponse>() {
                        @Override
                        public void success(UserResponse userResponse) {
                            if (userResponse.getCode() == ApiConstant.STATUS_CODE_SUCC && userResponse.getUserModel() != null) {
                                listener.OnLoginResponseSuccess(userResponse.getUserModel());
                            } else {
                                LoginManager.getInstance().logOut();
                                listener.onResponseFailed();
                            }
                        }

                        @Override
                        public boolean operationError(UserResponse userResponse, int status, String message) {
                            LoginManager.getInstance().logOut();
                            listener.onResponseFailed();
                            return false;
                        }

                        @Override
                        public boolean error(Throwable e) {
                            LoginManager.getInstance().logOut();
                            listener.onResponseFailed();
                            return false;
                        }
                    };
                    Subscription subscription = mApiWrapper.userLogin(mRequest)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new ApiSubscriber<>(handler));
                    mSub.add(subscription);

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,email,picture.width(608).height(608)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
