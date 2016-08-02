package com.soloask.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.data.bmob.QuestionDetailManager;
import com.soloask.android.data.bmob.UserManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.FileManager;
import com.soloask.android.util.MediaManager;
import com.soloask.android.util.RelativeDateFormat;
import com.soloask.android.util.SharedPreferencesHelper;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.util.billing.Inventory;
import com.soloask.android.util.billing.Purchase;
import com.soloask.android.view.MaterialProgressBar;
import com.soloask.android.view.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;


/**
 * Created by Lebron on 2016/6/21.
 */
public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mUserLayout, mNetworkLayout;
    private LinearLayout mDetailLayout;
    private RelativeLayout mVoiceLayout, mVoiceContainer, mBottomView;
    private ImageView mRespondentImg, mQuestionImg, mRespondentImg2;
    private ImageView mAnimImg, mPlayImg;
    private TextView mTimeLengthView, mQuestionerName, mPriceView, mContent, mQuestionPriceView;
    private TextView mTimeView, mListenersView;
    private TextView mRespondentView, mTitleView;
    private MaterialProgressBar mProgressBar;
    private AnimationDrawable mAnimationDrawable;
    private IabHelper mHelper;
    private List mSKULists;
    private Question mQuestion;
    private User mCurrentUser;
    private boolean isIABHelperOK;
    private boolean isPayed = true, isPaused;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initView();
        getCurrentUser();
        //initIabHelper();
    }

    private void initData() {
        String objectId = getIntent().getStringExtra(Constant.KEY_QUESTION_ID);
        QuestionDetailManager manager = new QuestionDetailManager();
        manager.setOnQuestionDetailListener(new QuestionDetailManager.OnQuestionDetailListener() {
            @Override
            public void onSuccess(Question question) {
                if (question != null) {
                    mQuestion = question;
                    //是否有回答
                    if (question.getQuesVoiceURL() != null) {
                        mVoiceContainer.setVisibility(View.VISIBLE);
                        mListenersView.setVisibility(View.VISIBLE);
                        mListenersView.setText(String.format(getResources().getString(R.string.format_listerers), question.getListenerNum()));
                    }

                    isPayed = true;
                    mPriceView.setText(R.string.detail_click_to_play);

                    Glide.with(QuestionDetailActivity.this)
                            .load(question.getAskUser().getUserIcon())
                            //.placeholder(R.drawable.ic_me_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mQuestionImg);
                    Glide.with(QuestionDetailActivity.this)
                            .load(question.getAnswerUser().getUserIcon())
                            //.placeholder(R.drawable.ic_me_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mRespondentImg);
                    Glide.with(QuestionDetailActivity.this)
                            .load(question.getAnswerUser().getUserIcon())
                            //.placeholder(R.drawable.ic_me_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mRespondentImg2);
                    mQuestionerName.setText(question.getAskUser().getUserName());
                    mQuestionPriceView.setText(String.format(getString(R.string.format_dollar), question.getQuesPrice()));
                    mTimeView.setText(RelativeDateFormat.format(question.getAskTime(), QuestionDetailActivity.this));
                    mContent.setText(question.getQuesContent());
                    mRespondentView.setText(question.getAnswerUser().getUserName());
                    mTitleView.setText(question.getAnswerUser().getUserIntroduce());
                    mTimeLengthView.setText(String.format(getString(R.string.format_second), question.getVoiceTime()));
                    mProgressBar.setVisibility(View.GONE);
                    mDetailLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed() {
                mNetworkLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mDetailLayout.setVisibility(View.GONE);
                findViewById(R.id.tv_retry).setOnClickListener(QuestionDetailActivity.this);
                Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
            }
        });
        manager.getQuestionDetail(objectId);
    }

    private void initIabHelper() {
        try {
            mHelper = new IabHelper(this, Constant.BASE64_ENCODED_PUBLIC_KEY);
            mHelper.enableDebugLogging(true);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (result.isFailure()) {
                        Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isIABHelperOK = true;
                    //doQuery();
                }
            });
        } catch (Exception e) {
        }

    }

    private void initView() {
        mUserLayout = (RelativeLayout) findViewById(R.id.rl_user_profile);
        mNetworkLayout = (RelativeLayout) findViewById(R.id.network_layout);
        mVoiceLayout = (RelativeLayout) findViewById(R.id.rl_voice_container);
        mDetailLayout = (LinearLayout) findViewById(R.id.ll_question_detail);
        mContent = (TextView) findViewById(R.id.tv_question);
        mTimeLengthView = (TextView) findViewById(R.id.tv_time_length);
        mRespondentImg = (ImageView) findViewById(R.id.img_respondent);
        mRespondentImg2 = (ImageView) findViewById(R.id.img_respondent2);
        mQuestionerName = (TextView) findViewById(R.id.tv_questioner_name);
        mPriceView = (TextView) findViewById(R.id.tv_voice_price);
        mQuestionPriceView = (TextView) findViewById(R.id.tv_cost);
        mQuestionImg = (ImageView) findViewById(R.id.img_questioner);
        mAnimImg = (ImageView) findViewById(R.id.img_playing_voice_anim);
        mPlayImg = (ImageView) findViewById(R.id.img_playing_voice);
        mVoiceContainer = (RelativeLayout) findViewById(R.id.rl_answer_container);
        mBottomView = (RelativeLayout) findViewById(R.id.rl_bottom_common_view);
        mTimeView = (TextView) findViewById(R.id.tv_answered_time);
        mListenersView = (TextView) findViewById(R.id.tv_listeners_info);
        mRespondentView = (TextView) findViewById(R.id.tv_respondent_name);
        mTitleView = (TextView) findViewById(R.id.tv_respondent_describe);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.progressbar_loading);
        mAnimImg.setVisibility(View.VISIBLE);
        mPlayImg.setVisibility(View.GONE);

        mUserLayout.setOnClickListener(this);
        mVoiceLayout.setOnClickListener(this);
        mRespondentImg.setOnClickListener(this);
        mQuestionerName.setOnClickListener(this);
        mQuestionImg.setOnClickListener(this);
    }

    private void getCurrentUser() {
        UserManager userManager = new UserManager();
        userManager.setUserInfoListener(new UserManager.UserInfoListener() {
            @Override
            public void onSuccess(User user) {
                mCurrentUser = user;
                initData();
                Log.i("QuestionDetailActivity", "getCurrentUser successfully" + mCurrentUser.getUserId());
            }

            @Override
            public void onFailed() {

            }
        });
        userManager.getUserInfo(SharedPreferencesHelper.getPreferenceString(this, Constant.KEY_LOGINED_OBJECT_ID, null));
    }

    private void checkUserHeard(User user) {
        QuestionDetailManager questionDetailManager = new QuestionDetailManager();
        questionDetailManager.setOnCheckUserHeardListener(new QuestionDetailManager.OnCheckUserHeardListener() {
            @Override
            public void onSuccess(boolean userHeard) {
                isPayed = userHeard;
                if (isPayed) {
                    mPriceView.setText(R.string.detail_click_to_play);
                }
            }

            @Override
            public void onFailed() {

            }
        });
        questionDetailManager.checkUserHeard(mQuestion, user);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ShareDialog shareDialog = new ShareDialog(QuestionDetailActivity.this);
            shareDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkFileExist();
            } else {
                Toast.makeText(QuestionDetailActivity.this, R.string.notice_permission_denied, Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_questioner:
            case R.id.tv_questioner_name:
                Intent intent = new Intent(QuestionDetailActivity.this, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", mQuestion.getAskUser());
                intent.putExtras(bundle);
                QuestionDetailActivity.this.startActivity(intent);
                break;
            case R.id.rl_user_profile:
            case R.id.img_respondent:
                Intent intent1 = new Intent(QuestionDetailActivity.this, UserProfileActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", mQuestion.getAnswerUser());
                intent1.putExtras(bundle1);
                QuestionDetailActivity.this.startActivity(intent1);
                break;
            case R.id.rl_voice_container:
                if (isPayed) {
                    if (MediaManager.isPlaying()) {//播放暂停
                        MediaManager.pause();
                        mPriceView.setText(R.string.detail_click_to_play);
                        mAnimationDrawable.stop();
                        isPaused = true;
                    } else if (isPaused) {//恢复播放
                        MediaManager.resume();
                        mPriceView.setText(R.string.recording_playing);
                        mAnimationDrawable.start();
                        isPaused = false;
                    } else {//正常播放
                        checkNeededPermission();
                    }
                } else {
                    doPurchase();
                }
                break;
            case R.id.tv_retry:
                mProgressBar.setVisibility(View.VISIBLE);
                mNetworkLayout.setVisibility(View.GONE);
                initData();
                break;
        }
    }

    private void checkNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            checkFileExist();
        }
    }

    private void downloadAudio(String fileUrl) {
        Log.i("QuestionDetail", fileUrl);
        BmobFile bmobFile = new BmobFile(mQuestion.getObjectId() + ".aac", "", fileUrl);
        File saveFile = new File(FileManager.getFilePath(mQuestion.getObjectId() + ".aac"));
        bmobFile.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    playAudio();
                    //纪录偷听用户
                    new QuestionDetailManager().setHeardUser(mQuestion, mCurrentUser);
                } else {
                    e.printStackTrace();
                    mPriceView.setText(R.string.detail_click_to_play);
                    Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("Lebron", value + "/" + newworkSpeed);
            }

        });
    }

    private void playAudio() {
        mPriceView.setText(R.string.recording_playing);
        mAnimationDrawable = (AnimationDrawable) mAnimImg.getDrawable();
        mAnimationDrawable.start();
        MediaManager.playSound(FileManager.getFilePath(mQuestion.getObjectId() + ".aac"), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mAnimationDrawable.stop();
                mAnimImg.setImageResource(R.drawable.anim_play_audio);
                mPriceView.setText(R.string.detail_click_to_play);
            }
        }, new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mAnimationDrawable.stop();
                mAnimImg.setImageResource(R.drawable.anim_play_audio);
                mPriceView.setText(R.string.detail_click_to_play);
                Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void checkFileExist() {
        if (!FileManager.isFileExits(mQuestion.getObjectId() + ".aac")) {
            mPriceView.setText(R.string.detail_downloading);
            downloadAudio(mQuestion.getQuesVoiceURL());
        } else {
            playAudio();
        }
    }

    private void doPurchase() {
        Log.i("Lebron", (mHelper != null) + " " + isIABHelperOK + " vs " + !mHelper.isAsyncInProgress());
        if (mHelper != null && isIABHelperOK && !mHelper.isAsyncInProgress()) {
            mHelper.launchPurchaseFlow(QuestionDetailActivity.this, Constant.OVERHEAR_PRICE_ID, 10003, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    } else if (info.getSku().equals(Constant.OVERHEAR_PRICE_ID)) {
                        doConsume(info, false);
                    }
                }
            });
        } else {
            Toast.makeText(QuestionDetailActivity.this, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void doConsume(Purchase purchase, final boolean fromQuery) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fromQuery) {
                    Toast.makeText(QuestionDetailActivity.this, "Now you can hear", Toast.LENGTH_LONG).show();
                    mPriceView.setText(R.string.detail_click_to_play);
                    isPayed = true;
                    //纪录偷听用户
                    new QuestionDetailManager().setHeardUser(mQuestion, mCurrentUser);
                } else {
                    Log.i("Lebron", "consume successfully");
                }
            }
        });
    }

    private void doQuery() {
        mSKULists = new ArrayList();
        mSKULists.add(Constant.OVERHEAR_PRICE_ID);
        if (mHelper != null && isIABHelperOK) {
            mHelper.queryInventoryAsync(true, mSKULists, new IabHelper.QueryInventoryFinishedListener() {

                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean hasPurchase = inv.hasPurchase(Constant.OVERHEAR_PRICE_ID);
                    if (hasPurchase) {
                        try {
                            doConsume(inv.getPurchase(Constant.OVERHEAR_PRICE_ID), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Lebron", hasPurchase + " " + inv.getSkuDetails(Constant.OVERHEAR_PRICE_ID).toString());
                    String priceResult = inv.getSkuDetails(Constant.OVERHEAR_PRICE_ID).getPrice();
                    if (isPayed) {
                        mPriceView.setText(R.string.detail_click_to_play);
                    } else {
                        mPriceView.setText(String.format(getResources().getString(R.string.format_price), priceResult));
                    }
                }
            });
        } else {
            Toast.makeText(QuestionDetailActivity.this, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (Exception e) {
            }
            mHelper = null;
        }
        try {
            FileManager.deleteFolder(FileManager.getFilePath(""));
        } catch (Exception e) {
        }
        MediaManager.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MediaManager.isPlaying()) {//播放暂停
            MediaManager.pause();
            mPriceView.setText(R.string.detail_click_to_play);
            mAnimationDrawable.stop();
            isPaused = true;
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
