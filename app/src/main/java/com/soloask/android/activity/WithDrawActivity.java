package com.soloask.android.activity;

import android.os.Bundle;
import android.os.Handler;
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
import com.soloask.android.R;
import com.soloask.android.data.bmob.WithDrawManager;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.BoundImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Lebron on 2016/6/26.
 */
public class WithDrawActivity extends BaseActivity {
    private RelativeLayout mProgressBar;
    private BoundImageView mUserIcon;
    private TextView mNameView, mIncomeView;
    private EditText mPaypalView, mPaypalView2;
    private CheckBox mRememberBox;
    private User mUser;
    private boolean isRemember;
    private String paypalAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initView();
        initData();
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
        mIncomeView.setText(String.format(getString(R.string.format_earned), mUser.getUserEarned()));
    }

    private void initView() {
        mProgressBar = (RelativeLayout) findViewById(R.id.rl_progressbar);
        mUserIcon = (BoundImageView) findViewById(R.id.img_user_icon);
        mNameView = (TextView) findViewById(R.id.tv_user_name);
        mIncomeView = (TextView) findViewById(R.id.tv_user_money);
        mPaypalView = (EditText) findViewById(R.id.edit_cash_account);
        mPaypalView2 = (EditText) findViewById(R.id.edit_cash_account_again);
        mRememberBox = (CheckBox) findViewById(R.id.box_remember);
        mRememberBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
            }
        });
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
                mProgressBar.setVisibility(View.VISIBLE);
                if (isRemember) {
                    SharedPreferencesHelper.setPreferenceString(WithDrawActivity.this, Constant.KEY_PAYPAL_ACCOUNT, paypalAccount);
                }
                SharedPreferencesHelper.setPreferenceBoolean(WithDrawActivity.this, Constant.KEY_REMEMBER_PAYPAL, isRemember);
                dealWithdrawApply();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void dealWithdrawApply() {
        WithDrawManager withDrawManager = new WithDrawManager();
        withDrawManager.setOnWithDrawListener(new WithDrawManager.OnWithDrawListener() {
            @Override
            public void onSuccess(String objectId) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(WithDrawActivity.this, R.string.toast_withdraw_submited, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed() {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(WithDrawActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
            }
        });
        withDrawManager.dealWithdraw(mUser, paypalAccount);
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
