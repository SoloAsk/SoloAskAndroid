package com.soloask.android.question.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.question.adapter.HistoryQuestionAdapter;
import com.soloask.android.question.injection.AskModule;
import com.soloask.android.question.presenter.AskPresenter;
import com.soloask.android.question.view.AskView;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.util.billing.Inventory;
import com.soloask.android.util.billing.Purchase;
import com.soloask.android.view.BoundImageView;
import com.soloask.android.view.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Lebron on 2016/6/21.
 */
public class UserProfileActivity extends BaseActivity implements AskView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener
        , BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.refresh_layout_user)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_user_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetWorkLayout;
    private BoundImageView mIconView;
    private TextView mNameView;
    private TextView mTitleView;
    private TextView mIntroduceView;
    private TextView mPriceView;
    private TextView mFinishView;
    private TextView mEditCountView;
    private TextView mSummaryView;
    private EditText mQuestionView;
    private CheckBox mPubBox;

    @Inject
    AskPresenter mPresenter;

    private HistoryQuestionAdapter mAdapter;
    private IabHelper mHelper;
    private List<Question> mDatas = new ArrayList();
    private String mContent;
    private boolean isPub = true;
    private boolean isIABHelperOK;
    private double mPrice = 0.0;
    private String mPaymentId;
    private User mRespondent, mQuestioner;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void initViewsAndData() {
        mRespondent = (User) getIntent().getSerializableExtra("user");

        mAdapter = new HistoryQuestionAdapter(mDatas);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.openLoadMore(10, true);
        mAdapter.setOnRecyclerViewItemClickListener(this);
        mAdapter.addHeaderView(getHeaderView());
        mAdapter.setLoadingView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_footer, null));
        mAdapter.setEmptyView(LayoutInflater.from(getViewContext()).inflate(R.layout.layout_common_empty, (ViewGroup) mRecyclerView.getParent(), false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        setRespondentInfo();
        addListener();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new AskModule(this))
                .inject(this);
        initIabHelper();
        getCurrentUser();
        getUserInfo();
        getUserRelatedQuestions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ShareDialog shareDialog = new ShareDialog(UserProfileActivity.this);
            shareDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCurrentUser() {
        if (mPresenter != null) {
            mPresenter.getCurrentUser(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_OBJECT_ID, null));
        }
    }

    private void getUserInfo() {
        if (mPresenter != null) {
            mPresenter.getRespondentInfo(mRespondent.getObjectId());
        }
    }

    private void getUserRelatedQuestions() {
        if (mPresenter != null) {
            mPresenter.getRespondentRelatedQuestions(mRespondent);
        }
    }

    private void askQuestions(Question question) {
        if (mPresenter != null) {
            mPresenter.askQuestion(question);
        }
    }

    private View getHeaderView() {
        View view = LayoutInflater.from(getViewContext()).inflate(R.layout.headview_user_profile, (ViewGroup) mRecyclerView.getParent(), false);
        mIconView = (BoundImageView) view.findViewById(R.id.img_user_icon);
        mNameView = (TextView) view.findViewById(R.id.tv_user_name);
        mTitleView = (TextView) view.findViewById(R.id.tv_user_title);
        mIntroduceView = (TextView) view.findViewById(R.id.tv_user_describe);
        mPriceView = (TextView) view.findViewById(R.id.tv_question_price);
        mFinishView = (TextView) view.findViewById(R.id.btn_submit_question);
        mEditCountView = (TextView) view.findViewById(R.id.tv_edit_count);
        mSummaryView = (TextView) view.findViewById(R.id.tv_user_history);
        mQuestionView = (EditText) view.findViewById(R.id.edit_ask_question);
        mPubBox = (CheckBox) view.findViewById(R.id.checkbox_question_nameless);
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestioner == null) {
                    showToast(R.string.login_account_disable);
                    return;
                }
                mContent = mQuestionView.getText().toString();
                if (TextUtils.isEmpty(mContent)) {
                    mQuestionView.setError(getString(R.string.notice_cannot_null));
                } else if (mQuestioner.getObjectId().equals(mRespondent.getObjectId())) {
                    showToast(R.string.notice_ask_yourself);
                } else {
                    doPurchase();
                }
            }
        });
        return view;
    }

    private void addListener() {
        mQuestionView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditCountView.setText(String.format(getString(R.string.format_write_question), mQuestionView.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPubBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPub = isChecked;
            }
        });
    }

    private void setRespondentInfo() {
        mPaymentId = "payment_" + String.format("%.2f", mRespondent.getUserPrice());
        String summary = String.format(getResources().getString(R.string.format_answered), mRespondent.getAnswerQuesNum())
                + " , " + String.format(getResources().getString(R.string.format_earned), mRespondent.getUserIncome());
        mSummaryView.setText(summary);
        mNameView.setText(mRespondent.getUserName());
        mTitleView.setText(mRespondent.getUserTitle());
        mIntroduceView.setText(mRespondent.getUserIntroduce());
        mPriceView.setText(String.format(getString(R.string.format_dollar), mRespondent.getUserPrice()));
        mPrice = mRespondent.getUserPrice();
        Glide.with(this)
                .load(mRespondent.getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIconView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (Exception e) {
            }
            mHelper = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.i("Lebron", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
        if (stringId == R.string.toast_no_more) {
            mAdapter.notifyDataChangedAfterLoadMore(false);
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showNetworkError(boolean show) {
        mNoNetWorkLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void getCurrentUser(User user) {
        mQuestioner = user;
    }

    @Override
    public void showCurrentRespondentInfo(User user) {
        mRespondent = user;
        setRespondentInfo();
    }

    @Override
    public void showRelatedQuestions(List<Question> questions) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataChangedAfterLoadMore(questions, true);
    }

    @Override
    public void askSuccess(String questionId) {
        Intent intent = new Intent(getViewContext(), QuestionDetailActivity.class);
        intent.putExtra(Constant.KEY_QUESTION_ID, questionId);
        startActivity(intent);
        mQuestionView.setText("");
        showToast(R.string.notice_success);
    }

    @Override
    public void onItemClick(View view, int i) {
        Intent intent = new Intent(getViewContext(), QuestionDetailActivity.class);
        intent.putExtra(Constant.KEY_QUESTION_ID, mDatas.get(i).getObjectId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mDatas.clear();
            mPresenter.resetSkipNum();
            getUserInfo();
            getUserRelatedQuestions();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        getUserRelatedQuestions();
    }

    private void initIabHelper() {
        try {
            if (mHelper == null) {
                mHelper = new IabHelper(this, Constant.BASE64_ENCODED_PUBLIC_KEY);
            }
            mHelper.enableDebugLogging(true);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        Toast.makeText(UserProfileActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isIABHelperOK = true;
                    doQuery();
                }
            });
        } catch (Exception e) {
        }

    }

    private void doPurchase() {
        if (mHelper != null && isIABHelperOK && !mHelper.isAsyncInProgress()) {
            mHelper.launchPurchaseFlow(this, mPaymentId, 10002, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Toast.makeText(getViewContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (info.getSku().equals(mPaymentId)) {
                            doConsume(info, false);
                        }
                    }
                }
            });
        } else {
            showToast(R.string.google_pay_is_unavailable);
        }
    }

    private void doConsume(Purchase purchase, final boolean fromQuery) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(getViewContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fromQuery) {
                    if (mRespondent != null && mQuestioner != null) {
                        Question question = new Question();
                        question.setAskUser(mQuestioner);
                        question.setAnswerUser(mRespondent);
                        question.setQuesContent(mContent);
                        question.setPub(isPub);
                        question.setQuesPrice(mPrice);
                        askQuestions(question);
                    }
                } else {
                    Log.i("UserProfileActivity", "consume successfully");
                }

            }
        });
    }

    private void doQuery() {
        List mSKULists = new ArrayList();
        if (mPaymentId == null) {
            mPaymentId = Constant.OVERHEAR_PRICE_ID;
        }
        Log.i("UserProfileActivity", mPaymentId);
        mSKULists.add(mPaymentId);
        if (mHelper != null && isIABHelperOK) {
            mHelper.queryInventoryAsync(true, mSKULists, new IabHelper.QueryInventoryFinishedListener() {

                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        Toast.makeText(getViewContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean hasPurchase = inv.hasPurchase(mPaymentId);
                    if (hasPurchase) {
                        try {
                            doConsume(inv.getPurchase(mPaymentId), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            showToast(R.string.google_pay_is_unavailable);
        }
    }
}
