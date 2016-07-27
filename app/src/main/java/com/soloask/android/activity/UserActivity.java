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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.soloask.android.R;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.view.CircleImageView;
import com.soloask.android.view.ShareDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Lebron on 2016/6/29.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    private TextView mLoginView;
    private LinearLayout mUserInfoView;
    private TextView mLogoutView;
    private TextView mUserNameView, mTitleView, mIntroduceView;
    private TextView mMyAnswerView, mMyQuestionView, mMyListenView, mAskNewView, mAnswerNewView;
    private TextView mUserPriceView, mUserIncomeView;
    private TextView mWithDrawView;
    private TextView mAboutView;
    private TextView mEditView;
    private CircleImageView mUserIcon;
    private Intent mIntent;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initData();
    }

    private void initView() {
        mIntent = new Intent();
        mLoginView = (TextView) findViewById(R.id.tv_user_login);
        mLogoutView = (TextView) findViewById(R.id.tv_logout);
        mUserNameView = (TextView) findViewById(R.id.tv_user_name);
        mTitleView = (TextView) findViewById(R.id.tv_user_title);
        mIntroduceView = (TextView) findViewById(R.id.tv_user_describe);
        mUserInfoView = (LinearLayout) findViewById(R.id.ll_user_info);
        mMyAnswerView = (TextView) findViewById(R.id.tv_my_answer);
        mMyQuestionView = (TextView) findViewById(R.id.tv_my_question);
        mAskNewView = (TextView) findViewById(R.id.tv_iask_new);
        mAnswerNewView = (TextView) findViewById(R.id.tv_ianswer_new);
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
        if (SharedPreferencesHelper.getPreferenceString(UserActivity.this, Constant.KEY_LOGINED_OBJECT_ID, null) != null) {
            mUserInfoView.setVisibility(View.VISIBLE);
            mEditView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
            mLogoutView.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        UserManager userManager = new UserManager();
        userManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
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

            }

            @Override
            public void onFailed() {
                Toast.makeText(UserActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
            }
        });
        userManager.getUserInfo(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_OBJECT_ID, null));
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
            initData();
        } else if (resultCode == Constant.CODE_RESULT_EDIT) {
            initData();
        } else if (resultCode == Constant.KEY_FROM_MY_QUESTION) {
            setResult(Constant.KEY_FROM_MY_QUESTION);
            finish();
        } else if (resultCode == Constant.KEY_FROM_MY_LISTEN) {
            setResult(Constant.KEY_FROM_MY_LISTEN);
            finish();
        } else if (resultCode == Constant.KEY_FROM_MY_ANSWER) {
            initData();
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
                break;
            case R.id.tv_my_answer:
                if (mUser != null) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_ANSWER);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, Constant.CODE_REQUEST);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_my_question:
                if (mUser != null) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_QUESTION);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, Constant.CODE_REQUEST);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_my_listen:
                if (mUser != null) {
                    mIntent.setClass(UserActivity.this, MyCommonActivity.class);
                    mIntent.putExtra(Constant.KEY_FROM_MINE, Constant.KEY_FROM_MY_LISTEN);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, Constant.CODE_REQUEST);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_edit_profile:
                if (mUser != null) {
                    mIntent.setClass(UserActivity.this, EditProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, Constant.CODE_REQUEST);
                } else {
                    mIntent.setClass(UserActivity.this, LoginActivity.class);
                    UserActivity.this.startActivityForResult(mIntent, Constant.CODE_REQUEST);
                }
                break;
            case R.id.tv_about:
                mIntent.setClass(UserActivity.this, AboutActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_withdraw:
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
