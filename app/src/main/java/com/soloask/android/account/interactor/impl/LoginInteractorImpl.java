package com.soloask.android.account.interactor.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.soloask.android.account.interactor.LoginInteractor;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.User;

import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by LeBron on 2016/8/6.
 */
public class LoginInteractorImpl implements LoginInteractor {
    private UserManager mManager;

    @Inject
    LoginInteractorImpl() {
    }

    @Override
    public void doLogin(Context context, CallbackManager manager, final LoginResponseListener listener) {
        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getLoginInfo(listener, loginResult.getAccessToken());
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
    public void getLoginInfo(final LoginResponseListener listener, AccessToken accessToken) {
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

                    if (mManager == null) {
                        mManager = new UserManager();
                    }
                    mManager.setUserLoginListener(new UserManager.UserLoginListener() {
                        @Override
                        public void onSuccess(User user) {
                            listener.OnLoginResponseSuccess(user);
                        }

                        @Override
                        public void onFailed() {
                            LoginManager.getInstance().logOut();
                            listener.onResponseFailed();
                        }
                    });
                    mManager.signOrLogin(id, name, photo);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,email,picture.width(608).height(608)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
