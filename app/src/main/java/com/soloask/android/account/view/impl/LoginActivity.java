package com.soloask.android.account.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.injection.LoginModule;
import com.soloask.android.account.model.UserModel;
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
    @BindView(R.id.progressbar_loading_layout)
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
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndData() {

    }

    @Override
    public void loginSuccess(UserModel user) {
        Intent intent = new Intent();
        intent.putExtra("user_name", user.getUserName());
        intent.putExtra("user_icon_url", user.getUserIcon());
        intent.putExtra("user_id", user.getUserId());
        intent.putExtra("token", user.getToken());
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
