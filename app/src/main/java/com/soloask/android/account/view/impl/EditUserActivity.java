package com.soloask.android.account.view.impl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.adapter.PriceAdapter;
import com.soloask.android.account.injection.EditUserModule;
import com.soloask.android.account.presenter.EditUserPresenter;
import com.soloask.android.account.view.EditUserView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.squareup.otto.Bus;
import com.umeng.message.UmengRegistrar;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 16-8-5.
 */
public class EditUserActivity extends BaseActivity implements EditUserView {
    @Inject
    EditUserPresenter mPresenter;
    @Inject
    Bus mBus;

    @BindView(R.id.tv_choose_price)
    TextView mPriceView;
    @BindView(R.id.progressbar_loading_layout)
    RelativeLayout mSavingLayout;
    @BindView(R.id.tv_price_1)
    TextView mPriceView1;
    @BindView(R.id.tv_price_5)
    TextView mPriceView5;
    @BindView(R.id.tv_price_10)
    TextView mPriceView10;
    @BindView(R.id.tv_price_more)
    TextView mPriceViewMore;
    @BindView(R.id.text_count)
    TextView mTextCountView;
    @BindView(R.id.tv_edit_count)
    TextView mTitleCountView;
    @BindView(R.id.edit_user_describe)
    EditText mDescribeView;
    @BindView(R.id.edit_user_title)
    EditText mTitleView;

    @OnClick(R.id.tv_price_1)
    public void setPrice1() {
        mPriceView.setText("$0.99");
        mAskPrice = 0.99;
    }

    @OnClick(R.id.tv_price_5)
    public void setPrice5() {
        mPriceView.setText("$4.99");
        mAskPrice = 4.99;
    }

    @OnClick(R.id.tv_price_10)
    public void setPrice10() {
        mPriceView.setText("$11.99");
        mAskPrice = 11.99;
    }

    @OnClick(R.id.tv_price_more)
    public void showDialog() {
        showChoosePriceDialog();
    }


    private User mUser;
    private String mTitle, mIntroduce;
    private double mAskPrice;
    private String mDeviceToken;
    private PriceAdapter mPriceAdapter;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new EditUserModule(this))
                .inject(this);
        mBus.register(this);
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            mTitle = mTitleView.getText().toString();
            mIntroduce = mDescribeView.getText().toString();
            if (TextUtils.isEmpty(mTitle)) {
                mTitleView.setError(getViewContext().getString(R.string.notice_cannot_null));
            } else if (TextUtils.isEmpty(mIntroduce)) {
                mDescribeView.setError(getViewContext().getString(R.string.notice_cannot_null));
            } else {
                if (mUser != null && mPresenter != null) {
                    mUser.setUserTitle(mTitle);
                    mUser.setUserIntroduce(mIntroduce);
                    mUser.setUserPrice(mAskPrice);
                    if (mDeviceToken != null) {
                        mUser.setDeviceToken(mDeviceToken);
                    }
                    mPresenter.setUserInfo(mUser);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser == null) {
            return;
        }
        mTitleView.setText(mUser.getUserTitle());
        mDescribeView.setText(mUser.getUserIntroduce());
        mAskPrice = mUser.getUserPrice();
        mPriceView.setText(String.format(getString(R.string.format_dollar), mUser.getUserPrice()));
        mDeviceToken = UmengRegistrar.getRegistrationId(getViewContext());
    }

    @Override
    public void updateUserInfoSuccess() {
        mBus.post(Constant.BUS_EVENT_EDIT);
        finish();
    }

    @Override
    public void showLoadingLayout(boolean show) {
        mSavingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    private void addListener() {
        mDescribeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextCountView.setText(String.format(getString(R.string.format_write_describe), mDescribeView.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitleCountView.setText(String.format(getString(R.string.format_write_title), mTitleView.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showChoosePriceDialog() {
        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        View view = getLayoutInflater().inflate(R.layout.dialog_price_table, null);
        final RecyclerView priceView = (RecyclerView) view.findViewById(R.id.grid_price_view);
        String[] priceArray = getResources().getStringArray(R.array.array_price);
        final List<String> list = Arrays.asList(priceArray);
        priceView.setLayoutManager(new GridLayoutManager(this, 5));
        mPriceAdapter = new PriceAdapter(list);
        mPriceAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mAskPrice = Double.valueOf(list.get(i));
                mPriceView.setText(String.format(getString(R.string.format_dollar), mAskPrice));
                mBottomSheetDialog.dismiss();
            }
        });
        priceView.setAdapter(mPriceAdapter);


        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }
}
