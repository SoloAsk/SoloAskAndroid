package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.soloask.android.R;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Lebron on 2016/6/22.
 */
public class LoginActivity extends BaseActivity {
    private CallbackManager callbackManager;
    private TextView mLoginView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
        mLoginView = (TextView) findViewById(R.id.tv_facebook_login);
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null || accessToken.isExpired()) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(AccessToken accessToken) {
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

                    UserManager userManager = new UserManager();
                    userManager.setUserLoginListener(new UserManager.UserLoginListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            Intent intent = new Intent();
                            intent.putExtra("user_name", name);
                            intent.putExtra("user_icon_url", photo);
                            intent.putExtra("user_object_id", objectId);
                            LoginActivity.this.setResult(Constant.CODE_RESULT_LOGIN, intent);
                            LoginActivity.this.finish();
                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    userManager.signOrLogin(id, name, photo);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,email,picture.width(608).height(608)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
