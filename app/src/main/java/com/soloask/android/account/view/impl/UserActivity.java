package com.soloask.android.account.view.impl;

import android.content.Context;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.injection.UserModule;
import com.soloask.android.account.presenter.UserPresenter;
import com.soloask.android.account.view.UserView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.QQManager;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.CircleImageView;
import com.soloask.android.view.ShareDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Lebron on 2016/6/29.
 */
public class UserActivity extends BaseActivity implements UserView {
    @BindView(R.id.tv_user_login)
    TextView mLoginView;
    @BindView(R.id.ll_user_info)
    LinearLayout mUserInfoView;
    @BindView(R.id.tv_logout)
    TextView mLogoutView;
    @BindView(R.id.tv_user_name)
    TextView mUserNameView;
    @BindView(R.id.tv_user_title)
    TextView mTitleView;
    @BindView(R.id.tv_user_describe)
    TextView mIntroduceView;
    @BindView(R.id.tv_my_answer)
    TextView mMyAnswerView;
    @BindView(R.id.tv_my_question)
    TextView mMyQuestionView;
    @BindView(R.id.tv_my_listen)
    TextView mMyListenView;
    @BindView(R.id.tv_iask_new)
    TextView mAskNewView;
    @BindView(R.id.tv_ianswer_new)
    TextView mAnswerNewView;
    @BindView(R.id.tv_user_price)
    TextView mUserPriceView;
    @BindView(R.id.tv_user_income)
    TextView mUserIncomeView;
    @BindView(R.id.tv_withdraw)
    TextView mWithDrawView;
    @BindView(R.id.tv_about)
    TextView mAboutView;
    @BindView(R.id.tv_edit_profile)
    TextView mEditView;
    @BindView(R.id.img_user_icon)
    CircleImageView mUserIcon;

    @Inject
    UserPresenter mPresenter;

    private Intent mIntent;
    private User mUser;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_user;
    }

    @Override
    protected void initViewsAndData() {
        mIntent = new Intent();
        if (SharedPreferencesHelper.getPreferenceString(UserActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
            mUserInfoView.setVisibility(View.VISIBLE);
            mEditView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this)
                .getAppComponent()
                .plus(new UserModule(this))
                .inject(this);
        getUserInfo();
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
            SharedPreferencesHelper.setPreferenceString(UserActivity.this, Constant.KEY_LOGINED_OBJECT_ID, data.getStringExtra("user_object_id"));
            SharedPreferencesHelper.setPreferenceString(UserActivity.this, Constant.KEY_LOGINED_ICON_URL, data.getStringExtra("user_icon_url"));
            mUserInfoView.setVisibility(View.VISIBLE);
            mEditView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
            getUserInfo();
        } else if (resultCode == Constant.CODE_RESULT_EDIT || resultCode == Constant.KEY_FROM_MY_ANSWER) {
            getUserInfo();
        } else if (resultCode == Constant.KEY_FROM_MY_QUESTION) {
            setResult(Constant.KEY_FROM_MY_QUESTION);
            finish();
        } else if (resultCode == Constant.KEY_FROM_MY_LISTEN) {
            setResult(Constant.KEY_FROM_MY_LISTEN);
            finish();
        } else {
            Log.i("UserActivity", "  do nothing");
        }
    }


    @OnClick(R.id.tv_user_login)
    public void gotoLoginActivity() {
        mIntent.setClass(UserActivity.this, LoginActivity.class);
        UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
    }

    @OnClick(R.id.tv_logout)
    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setMessage(R.string.sure_to_log_out);
        builder.setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    //LoginManager.getInstance().logOut();
                    QQManager.getTencentInstance(getViewContext()).logout(UserActivity.this);
                    SharedPreferencesHelper.setPreferenceString(UserActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null);
                    SharedPreferencesHelper.setPreferenceString(UserActivity.this, Constant.KEY_LOGINED_ICON_URL, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mUserInfoView.setVisibility(View.GONE);
                mEditView.setVisibility(View.GONE);
                mUserIcon.setImageResource(R.drawable.ic_me_default);
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
    }

    @OnClick(R.id.tv_my_answer)
    public void gotoMyAnswerActivity() {
        toMyCommonActivity(Constant.KEY_FROM_MY_ANSWER);
    }

    @OnClick(R.id.tv_my_question)
    public void gotoMyQuestionActivity() {
        toMyCommonActivity(Constant.KEY_FROM_MY_QUESTION);
    }

    @OnClick(R.id.tv_my_listen)
    public void gotoMyListenActivity() {
        toMyCommonActivity(Constant.KEY_FROM_MY_LISTEN);
    }

    @OnClick(R.id.tv_edit_profile)
    public void gotoEditActivity() {
        if (mUser != null) {
            mIntent.setClass(UserActivity.this, EditUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mUser);
            mIntent.putExtras(bundle);
            startActivityForResult(mIntent, Constant.CODE_REQUEST);
        } else {
            mIntent.setClass(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
        }
    }

    @OnClick(R.id.tv_about)
    public void gotoAboutActivity() {
        mIntent.setClass(UserActivity.this, AboutActivity.class);
        startActivity(mIntent);
    }

    @OnClick(R.id.tv_withdraw)
    public void gotoWithDrawActivity() {
        if (mUser != null) {
            mIntent.setClass(UserActivity.this, WithDrawActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("user", mUser);
            mIntent.putExtras(bundle1);
            startActivity(mIntent);
        } else {
            mIntent.setClass(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
        }
    }

    private void toMyCommonActivity(int from) {
        if (mUser != null) {
            mIntent.setClass(UserActivity.this, MyCommonActivity.class);
            mIntent.putExtra(Constant.KEY_FROM_MINE, from);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mUser);
            mIntent.putExtras(bundle);
            startActivityForResult(mIntent, Constant.CODE_REQUEST);
        } else {
            mIntent.setClass(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
        }
    }

    private void getUserInfo() {
        if (mPresenter != null) {
            mPresenter.getUserInfo(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_OBJECT_ID, null));
        }
    }

    @Override
    public void showUserInfo(User user) {
        mUser = user;
        Glide.with(UserActivity.this)
                .load(user.getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mUserIcon);
        mUserNameView.setText(user.getUserName());
        mTitleView.setText(user.getUserTitle());
        mIntroduceView.setText(user.getUserIntroduce());
        mUserPriceView.setText(String.format(getString(R.string.format_dollar), user.getUserPrice()));
        mUserIncomeView.setText(String.format(getString(R.string.format_dollar), user.getUserIncome()));
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
