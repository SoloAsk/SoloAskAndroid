package com.soloask.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.soloask.android.R;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.CircleImageView;
import com.soloask.android.view.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.net.URL;

/**
 * Created by Lebron on 2016/6/29.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    private TextView mLoginView;
    private LinearLayout mUserInfoView;
    private TextView mLogoutView;
    private TextView mUserNameView;
    private TextView mMyAnswerView, mMyQuestionView, mMyListenView;
    private TextView mUserPriceView, mUserIncomeView;
    private TextView mWithDrawView;
    private TextView mAboutView;
    private TextView mEditView;
    private CircleImageView mUserIcon;
    private Intent mIntent;

    URL url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        mIntent = new Intent();
        mLoginView = (TextView) findViewById(R.id.tv_user_login);
        mLogoutView = (TextView) findViewById(R.id.tv_logout);
        mUserNameView = (TextView) findViewById(R.id.tv_user_name);
        mUserInfoView = (LinearLayout) findViewById(R.id.ll_user_info);
        mMyAnswerView = (TextView) findViewById(R.id.tv_my_answer);
        mMyQuestionView = (TextView) findViewById(R.id.tv_my_question);
        mMyListenView = (TextView) findViewById(R.id.tv_my_listen);
        mEditView = (TextView) findViewById(R.id.tv_edit_profile);
        mWithDrawView = (TextView) findViewById(R.id.tv_withdraw);
        mAboutView = (TextView) findViewById(R.id.tv_about);
        mUserPriceView = (TextView) findViewById(R.id.tv_user_price);
        mUserIncomeView = (TextView) findViewById(R.id.tv_user_income);
        mUserIcon = (CircleImageView) findViewById(R.id.img_user_icon);
        mLoginView.setOnClickListener(this);
        mLogoutView.setOnClickListener(this);
        mMyAnswerView.setOnClickListener(this);
        mMyQuestionView.setOnClickListener(this);
        mMyListenView.setOnClickListener(this);
        mEditView.setOnClickListener(this);
        mWithDrawView.setOnClickListener(this);
        mAboutView.setOnClickListener(this);
        if (SharedPreferencesHelper.getPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false)) {
            mUserInfoView.setVisibility(View.VISIBLE);
            mEditView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ShareDialog shareDialog = new ShareDialog(UserActivity.this);
            shareDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.CODE_RESULT_LOGIN) {
            SharedPreferencesHelper.setPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, true);
            mUserInfoView.setVisibility(View.VISIBLE);
            mEditView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
            mUserNameView.setText(data.getStringExtra("user_name"));
            Glide.with(UserActivity.this)
                    .load(data.getStringExtra("user_icon_url"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mUserIcon);

        } else if (resultCode == Constant.CODE_RESULT_EDIT) {
            mUserPriceView.setText("$9.99");
        } else {
            Log.i("Lebron", "  do nothing");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_login:
                mIntent.setClass(UserActivity.this, LoginActivity.class);
                UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                break;
            case R.id.tv_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setMessage(R.string.sure_to_log_out);
                builder.setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            LoginManager.getInstance().logOut();
                            SharedPreferencesHelper.setPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mUserInfoView.setVisibility(View.GONE);
                        mEditView.setVisibility(View.GONE);
                        mLoginView.setVisibility(View.VISIBLE);
                        mLogoutView.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.tv_my_answer:
                if (SharedPreferencesHelper.getPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false)) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_ANSWER);
                    startActivity(mIntent);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_my_question:
                if (SharedPreferencesHelper.getPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false)) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_QUESTION);
                    startActivity(mIntent);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_my_listen:
                if (SharedPreferencesHelper.getPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false)) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_LISTEN);
                    startActivity(mIntent);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_edit_profile:
                mIntent.setClass(UserActivity.this, EditProfileActivity.class);
                startActivityForResult(mIntent, Constant.CODE_REQUEST);
                break;
            case R.id.tv_about:
                mIntent.setClass(UserActivity.this, LauncherActivity.class);
                mIntent.putExtra(Constant.KEY_FROM_ABOUT, true);
                startActivity(mIntent);
                break;
            case R.id.tv_withdraw:
                if (SharedPreferencesHelper.getPreferenceBoolean(UserActivity.this, Constant.KEY_IS_LOGINED, false)) {
                    mIntent.setClass(UserActivity.this, WithDrawActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
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
