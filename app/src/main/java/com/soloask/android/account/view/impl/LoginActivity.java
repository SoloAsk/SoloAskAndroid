package com.soloask.android.account.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.injection.LoginModule;
import com.soloask.android.account.presenter.LoginPresenter;
import com.soloask.android.account.view.LoginView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeBron on 2016/8/6.
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.tv_facebook_login)
    TextView mLoginView;
    @BindView(R.id.rl_progressbar)
    RelativeLayout mLoadingLayout;

    @Inject
    LoginPresenter mPresenter;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new LoginModule(this))
                .inject(this);
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_facebook_login)
    public void login() {
        if (mPresenter != null) {
            mPresenter.doLogin(mCallbackManager);
        }
        /*User user = new User();
        user.setObjectId("6c8d5c4865");
        user.setUserName("Heyong");
        user.setUserIcon("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xft1/v/t1.0-1/p720x720/11703164_1432811433714866_750303433030121790_n.jpg?oh=b72934c9efad3ade7c92fc8990c17dd7&oe=5821FB1E&__gda__=1475159383_f2d077770029b513159caaa8fe1e8925");
        loginSuccess(user);*/
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndData() {

    }

    @Override
    public void loginSuccess(User user) {
        Intent intent = new Intent();
        intent.putExtra("user_name", user.getUserName());
        intent.putExtra("user_icon_url", user.getUserIcon());
        intent.putExtra("user_object_id", user.getObjectId());
        LoginActivity.this.setResult(Constant.CODE_RESULT_LOGIN, intent);
        LoginActivity.this.finish();
    }

    @Override
    public void showLoadingProgress(boolean show) {
        mLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
