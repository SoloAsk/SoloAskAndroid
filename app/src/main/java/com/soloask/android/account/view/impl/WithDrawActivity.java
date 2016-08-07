package com.soloask.android.account.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.injection.WithDrawModule;
import com.soloask.android.account.presenter.WithDrawPresenter;
import com.soloask.android.account.view.WithDrawView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.BoundImageView;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Lebron on 2016/6/26.
 */
public class WithDrawActivity extends BaseActivity implements WithDrawView {
    @BindView(R.id.rl_progressbar)
    RelativeLayout mProgressBar;
    @BindView(R.id.img_user_icon)
    BoundImageView mUserIcon;
    @BindView(R.id.tv_user_name)
    TextView mNameView;
    @BindView(R.id.tv_user_money)
    TextView mIncomeView;
    @BindView(R.id.edit_cash_account)
    EditText mPaypalView;
    @BindView(R.id.edit_cash_account_again)
    EditText mPaypalView2;
    @BindView(R.id.box_remember)
    CheckBox mRememberBox;

    @Inject
    WithDrawPresenter mPresenter;
    private User mUser;
    private boolean isRemember;
    private String paypalAccount;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initViewsAndData() {
        mRememberBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
            }
        });
        initData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new WithDrawModule(this))
                .inject(this);
    }

    private void initData() {
        isRemember = SharedPreferencesHelper.getPreferenceBoolean(this, Constant.KEY_REMEMBER_PAYPAL, false);
        if (isRemember) {
            mRememberBox.setChecked(true);
            mPaypalView.setText(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_PAYPAL_ACCOUNT, ""));
        }
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser == null) {
            return;
        }
        Glide.with(this)
                .load(mUser.getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mUserIcon);
        mNameView.setText(mUser.getUserName());
        mIncomeView.setText(String.format(getString(R.string.format_current_income), mUser.getUserEarned()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_withdraw, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_withdraw) {
            paypalAccount = mPaypalView.getText().toString();
            if (TextUtils.isEmpty(paypalAccount)) {
                mPaypalView.setError(WithDrawActivity.this.getString(R.string.notice_cannot_null));
            } else if (TextUtils.isEmpty(mPaypalView2.getText())) {
                mPaypalView2.setError(WithDrawActivity.this.getString(R.string.notice_cannot_null));
            } else if (!paypalAccount.equals(mPaypalView2.getText().toString())) {
                mPaypalView2.setError(WithDrawActivity.this.getString(R.string.notice_donot_match));
            } else {
                if (mPresenter != null) {
                    mPresenter.withDrawRequest(mUser, paypalAccount);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestSuccess() {
        if (isRemember) {
            SharedPreferencesHelper.setPreferenceString(WithDrawActivity.this, Constant.KEY_PAYPAL_ACCOUNT, paypalAccount);
        }
        SharedPreferencesHelper.setPreferenceBoolean(WithDrawActivity.this, Constant.KEY_REMEMBER_PAYPAL, isRemember);
        finish();
    }

    @Override
    public void showLoadingLayout(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
