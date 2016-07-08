package com.soloask.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soloask.android.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initView();
    }

    private void initView() {
        mProgressBar = (RelativeLayout) findViewById(R.id.rl_progressbar);
        mUserIcon = (BoundImageView) findViewById(R.id.img_user_icon);
        mNameView = (TextView) findViewById(R.id.tv_user_name);
        mIncomeView = (TextView) findViewById(R.id.tv_user_money);
        mPaypalView = (EditText) findViewById(R.id.edit_cash_account);
        mPaypalView2 = (EditText) findViewById(R.id.edit_cash_account_again);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_withdraw, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_withdraw) {
            if (TextUtils.isEmpty(mPaypalView.getText())) {
                mPaypalView.setError(WithDrawActivity.this.getString(R.string.notice_cannot_null));
            } else if (TextUtils.isEmpty(mPaypalView2.getText())) {
                mPaypalView2.setError(WithDrawActivity.this.getString(R.string.notice_cannot_null));
            } else if (!mPaypalView.getText().equals(mPaypalView2.getText())) {
                mPaypalView2.setError(WithDrawActivity.this.getString(R.string.notice_donot_match));
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(WithDrawActivity.this, R.string.toast_withdraw_submited, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 2000L);
            }
        }
        return super.onOptionsItemSelected(item);
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
