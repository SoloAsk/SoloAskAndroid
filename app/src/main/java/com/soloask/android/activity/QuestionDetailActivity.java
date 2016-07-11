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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.soloask.android.R;
import com.soloask.android.data.rest.ServiceGenerator;
import com.soloask.android.data.rest.DownloadServiceApi;
import com.soloask.android.util.Constant;
import com.soloask.android.util.FileManager;
import com.soloask.android.util.MediaManager;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.util.billing.Inventory;
import com.soloask.android.util.billing.Purchase;
import com.soloask.android.view.ShareDialog;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lebron on 2016/6/21.
 */
public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mUserLayout;
    private RelativeLayout mVoiceLayout, mVoiceContainer;
    private FrameLayout mDislikeLayout;
    private ImageView mRespondentImg, mQuestionImg;
    private ImageView mAnimImg, mPlayImg;
    private TextView mTimeLengthView, mQuestionerName, mPriceView, mDislikesView;
    private TextView mTimeView, mListenersView;
    private AnimationDrawable mAnimationDrawable;
    private IabHelper mHelper;
    private List mSKULists;
    private boolean isIABHelperOK;
    private boolean isPayed, isPaused;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initView();
        initIabHelper();
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
                    doQuery();
                }
            });
        } catch (Exception e) {
        }

    }

    private void initView() {
        mUserLayout = (RelativeLayout) findViewById(R.id.rl_user_profile);
        mVoiceLayout = (RelativeLayout) findViewById(R.id.rl_voice_container);
        mDislikeLayout = (FrameLayout) findViewById(R.id.fl_vote_negative);
        mTimeLengthView = (TextView) findViewById(R.id.tv_time_length);
        mRespondentImg = (ImageView) findViewById(R.id.img_respondent);
        mQuestionerName = (TextView) findViewById(R.id.tv_questioner_name);
        mPriceView = (TextView) findViewById(R.id.tv_voice_price);
        mQuestionImg = (ImageView) findViewById(R.id.img_questioner);
        mAnimImg = (ImageView) findViewById(R.id.img_playing_voice_anim);
        mPlayImg = (ImageView) findViewById(R.id.img_playing_voice);
        mVoiceContainer = (RelativeLayout) findViewById(R.id.rl_answer_container);
        mDislikesView = (TextView) findViewById(R.id.tv_listeners_info);
        mTimeView = (TextView) findViewById(R.id.tv_answered_time);
        mListenersView = (TextView) findViewById(R.id.tv_listeners_info);
        mAnimImg.setVisibility(View.VISIBLE);
        mPlayImg.setVisibility(View.GONE);

        if (getIntent().getBooleanExtra(Constant.KEY_FROM_MYASK, false)) {
            mVoiceContainer.setVisibility(View.GONE);
            mDislikesView.setText(R.string.status_unanswered);
        }

        mTimeView.setText(getResources().getQuantityString(R.plurals.dealed_time_hour, 1, 2));
        mListenersView.setText(String.format(getResources().getString(R.string.format_listerers), 123));
        mUserLayout.setOnClickListener(this);
        mVoiceLayout.setOnClickListener(this);
        mRespondentImg.setOnClickListener(this);
        mQuestionerName.setOnClickListener(this);
        mQuestionImg.setOnClickListener(this);
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
                Toast.makeText(QuestionDetailActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_profile:
            case R.id.img_respondent:
            case R.id.img_questioner:
            case R.id.tv_questioner_name:
                Intent intent = new Intent(QuestionDetailActivity.this, UserProfileActivity.class);
                QuestionDetailActivity.this.startActivity(intent);
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
/*        DownloadServiceApi downloadServiceApi = ServiceGenerator.createService(DownloadServiceApi.class);
        Call<ResponseBody> call = downloadServiceApi.getSmallSizeFile(fileUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Lebron", "server contacted and has file");
                    boolean writtenToDisk = FileManager.writeResponseBodyToDisk(response.body());
                    Log.d("Lebron", "file download was a success? " + writtenToDisk);
                    if (writtenToDisk) {
                        playAudio();
                    }
                } else {
                    mPriceView.setText(R.string.detail_click_to_play);
                    Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mPriceView.setText(R.string.detail_click_to_play);
                Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
            }
        });*/
        FileDownloader.getImpl().create(fileUrl)
                .setPath(FileManager.getFilePath("test.apk"))
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i("Lebron", soFarBytes + "/" + totalBytes);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        playAudio();
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        mPriceView.setText(R.string.detail_click_to_play);
                        Toast.makeText(QuestionDetailActivity.this, R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

    private void playAudio() {
        mPriceView.setText(R.string.recording_playing);
        mAnimationDrawable = (AnimationDrawable) mAnimImg.getDrawable();
        mAnimationDrawable.start();
        MediaManager.playSound(FileManager.getFilePath("answer_temp.aac"), new MediaPlayer.OnCompletionListener() {
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
        if (!FileManager.isFileExits("test.apk")) {
            mPriceView.setText(R.string.detail_downloading);
            downloadAudio("https://storage.evozi.com/apk/dl/15/02/25/com.devuni.flashlight.apk?h=HAV1MiF3krF0ryZnYyTaNA&t=1467891185");
            return;
        } else {
            playAudio();
        }
    }

    private void doPurchase() {
        Log.i("Lebron", (mHelper != null) + " " + isIABHelperOK + " vs " + !mHelper.isAsyncInProgress());
        if (mHelper != null && isIABHelperOK && !mHelper.isAsyncInProgress()) {
            mHelper.launchPurchaseFlow(QuestionDetailActivity.this, "payment_for_listen", 10003, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    } else if (info.getSku().equals("payment_for_listen")) {
                        doConsume(info);
                    }
                }
            });
        } else {
            Toast.makeText(QuestionDetailActivity.this, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void doConsume(Purchase purchase) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(QuestionDetailActivity.this, "Now you can hear", Toast.LENGTH_LONG).show();
                mPriceView.setText(R.string.detail_click_to_play);
                isPayed = true;
            }
        });
    }

    private void doQuery() {
        mSKULists = new ArrayList();
        mSKULists.add("payment_for_listen");
        if (mHelper != null && isIABHelperOK) {
            mHelper.queryInventoryAsync(true, mSKULists, new IabHelper.QueryInventoryFinishedListener() {

                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        Toast.makeText(QuestionDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    /*boolean hasPurchase = inv.hasPurchase("payment_for_listen");
                    if (hasPurchase) {
                        try {
                            doConsume(inv.getPurchase("payment_for_listen"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Lebron", "test" + hasPurchase);*/
                    Log.i("Lebron", inv.getSkuDetails("payment_for_listen").toString());
                    String priceResult = inv.getSkuDetails("payment_for_listen").getPrice();
                    String price = priceResult.substring(priceResult.indexOf("$"), priceResult.length());
                    mPriceView.setText(String.format(getResources().getString(R.string.format_price), price));
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
        MediaManager.release();
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
