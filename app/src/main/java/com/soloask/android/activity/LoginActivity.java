package com.soloask.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.soloask.android.R;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.util.Constant;
import com.soloask.android.util.QQManager;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by Lebron on 2016/6/22.
 */
public class LoginActivity extends BaseActivity {
    private Tencent mTencent;
    private TextView mFBLoginView;
    private TextView mQQLoginView;
    private String mDeviceToken = "device_token";
    private IUiListener mBaseUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            Log.i("LoginActivity", jsonObject.toString());
            getUserQQLoginInfo(jsonObject);
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, uiError.errorMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTencent = QQManager.getTencentInstance(LoginActivity.this);
        mTencent.logout(this);
        mFBLoginView = (TextView) findViewById(R.id.tv_facebook_login);
        mQQLoginView = (TextView) findViewById(R.id.tv_qq_login);
        mFBLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null || accessToken.isExpired()) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                }
            }
        });
        mQQLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTencent.isSessionValid()) {
                    mTencent.login(LoginActivity.this, "all", mBaseUiListener);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUiListener);
    }

    /**
     * 获取登录信息
     */

    private void doLogin(String id, final String name, final String photo) {
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
                LoginManager.getInstance().logOut();
                mTencent.logout(LoginActivity.this);
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
        userManager.signOrLogin(id, name, photo, mDeviceToken);
    }

    private void getUserQQLoginInfo(final JSONObject jsonObject) {
        try {
            mTencent.setOpenId(jsonObject.optString("openid"));
            mTencent.setAccessToken(jsonObject.optString("access_token"), String.valueOf(jsonObject.optLong("expires_in")));
            UserInfo userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject userInfo = (JSONObject) o;
                    String id = jsonObject.optString("openid");
                    String name = userInfo.optString("nickname");
                    String photo = userInfo.optString("figureurl_qq_2");
                    Log.i("LoginActivity", userInfo.toString());
                    if (TextUtils.isEmpty(name)) {
                        name = "无名氏";
                    }
                    if (TextUtils.isEmpty(photo)) {
                        photo = userInfo.optString("figureurl_qq_1");
                    }
                    doLogin(id, name, photo);
                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(LoginActivity.this, uiError.errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {

                }
            });
        } catch (Exception e) {
        }
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
