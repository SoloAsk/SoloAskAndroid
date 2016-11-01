package com.soloask.android.question.view.impl;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.question.injection.AnswerModule;
import com.soloask.android.question.model.QuestionModel;
import com.soloask.android.question.presenter.AnswerPresenter;
import com.soloask.android.question.view.AnswerView;
import com.soloask.android.util.AudioManager;
import com.soloask.android.util.Constant;
import com.soloask.android.util.FileManager;
import com.soloask.android.util.MediaManager;
import com.soloask.android.util.RelativeDateFormat;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Lebron on 2016/6/22.
 */
public class AnswerActivity extends BaseActivity implements AnswerView {
    private static final int MAX_RECORDING_LENGTH = 60;
    @BindView(R.id.img_questioner)
    ImageView mQuestionerIcon;
    @BindView(R.id.tv_questioner_name)
    TextView mQuestionerName;
    @BindView(R.id.tv_cost)
    TextView mQuestionPrice;
    @BindView(R.id.tv_question)
    TextView mQuestionView;
    @BindView(R.id.tv_recording_status)
    TextView mStatusView;
    @BindView(R.id.tv_answer_send)
    TextView mSendView;
    @BindView(R.id.btn_refuse_answer)
    TextView mRefuseView;
    @BindView(R.id.tv_answer_restart)
    TextView mRestartView;
    @BindView(R.id.tv_recording_progress)
    TextView mTimeProgress;
    @BindView(R.id.tv_answered_time)
    TextView mDealedTime;
    @BindView(R.id.img_recording_voice)
    ImageView mRecordingView;
    @BindView(R.id.progressbar_loading_layout)
    RelativeLayout mSendingLayout;
    @BindView(R.id.network_layout)
    RelativeLayout mNoNetWorkLayout;

    @Inject
    AnswerPresenter mPresenter;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean isRecording;
    private boolean isRecordingFinished;
    private int mCurrentSecond = 0;
    private int mTotalSeconds = 0;
    private int mPlayProgress = 0;
    private QuestionModel mQuestion;
    private String mQuestionId;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_answer;
    }

    @Override
    protected void initViewsAndData() {
        mQuestionId = getIntent().getStringExtra(Constant.KEY_QUESTION_ID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new AnswerModule(this))
                .inject(this);
        getQuestionDetail();
    }

    private void getQuestionDetail() {
        if (mPresenter != null) {
            mPresenter.getQuestionDetail(mQuestionId);
        }
    }

    private void uploadAnswer() {
        if (mPresenter != null) {
            mPresenter.uploadAnswer(mQuestionId, FileManager.getFilePath(Constant.FILE_NAME_VOICE), --mTotalSeconds);
        }
    }


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mCurrentSecond++;
                    mTotalSeconds = mCurrentSecond;
                    if (mTotalSeconds > MAX_RECORDING_LENGTH) {
                        stopRecording();
                    } else {
                        mTimeProgress.setText(String.format(getResources().getString(R.string.format_second), mCurrentSecond));
                    }
                    break;
                case 2:
                    mPlayProgress--;
                    if (mPlayProgress < 0) {
                        mTimeProgress.setText(String.format(getResources().getString(R.string.format_second), 0));
                    } else {
                        mTimeProgress.setText(String.format(getResources().getString(R.string.format_second), mPlayProgress));
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void startTimer() {
        stopTimer();
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    if (!isRecording && isRecordingFinished) {
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void stopRecording() {
        AudioManager.getInstance().stopAudio();
        mRestartView.setEnabled(true);
        mSendView.setEnabled(true);
        mStatusView.setText(R.string.recording_play);
        mRecordingView.setBackgroundResource(R.drawable.ic_answer_play);
        isRecording = false;
        isRecordingFinished = true;
        stopTimer();
    }

    private void startRecording() {
        AudioManager.getInstance().setOnAudioStateChangeListener(new AudioManager.AudioStateChangeListener() {
            @Override
            public void wellPrepared() {
                isRecording = true;
                mStatusView.setText(R.string.recording_stop);
                mRecordingView.setBackgroundResource(R.drawable.ic_answer_stop);
                startTimer();
            }

            @Override
            public void Preparedfailed() {
                Toast.makeText(AnswerActivity.this, R.string.notice_cannot_record, Toast.LENGTH_SHORT).show();
            }
        });
        AudioManager.getInstance().prepareAudio();
    }

    private void playRecording() {
        mStatusView.setText(R.string.recording_playing);
        mRecordingView.setBackgroundResource(R.drawable.ic_answer_stop);
        startTimer();
        MediaManager.playSound(AudioManager.getInstance().getCurrentPath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mStatusView.setText(R.string.recording_play);
                mRecordingView.setBackgroundResource(R.drawable.ic_answer_play);
                mTimeProgress.setText(String.format(getResources().getString(R.string.format_second), mTotalSeconds));
                stopTimer();
            }
        }, new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    private void checkNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            doRecordingBtnMethod();
        }
    }

    private void doRecordingBtnMethod() {
        if (!isRecording && !isRecordingFinished) {
            startRecording();
        } else if (isRecording && !isRecordingFinished) {
            stopRecording();
        } else if (!isRecording && isRecordingFinished) {
            mPlayProgress = mTotalSeconds;
            playRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            stopTimer();
            MediaManager.release();
            AudioManager.getInstance().stopAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doRecordingBtnMethod();
            } else {
                showToast(R.string.notice_permission_denied);
            }
            return;
        }
    }

    @OnClick(R.id.tv_retry)
    public void retry() {
        showNetworkError(false);
        getQuestionDetail();
    }

    @OnClick(R.id.img_recording_voice)
    public void recording() {
        if (mQuestion.getState() == Constant.STATUS_UNANSWERED) {
            checkNeededPermission();
        } else {
            showToast(R.string.notice_question_expired);
        }
    }


    @OnClick(R.id.tv_answer_restart)
    public void restart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
        builder.setTitle(R.string.rerecording_title);
        builder.setMessage(R.string.rerecording_message);
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MediaManager.release();
                AudioManager.getInstance().reRecording();
                stopTimer();
                mTimeProgress.setText(String.format(getResources().getString(R.string.format_second), 0));
                mCurrentSecond = 0;
                mTotalSeconds = 0;
                mStatusView.setText(R.string.recording_start);
                mRecordingView.setBackgroundResource(R.drawable.ic_answer_record);
                mRestartView.setEnabled(false);
                mSendView.setEnabled(false);
                isRecordingFinished = false;
                isRecording = false;
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.tv_answer_send)
    public void send() {
        if (FileManager.isFileExits(Constant.FILE_NAME_VOICE)) {
            uploadAnswer();
        } else {
            showToast(R.string.failed_to_load_data);
        }
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
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
    public void showProgress(boolean show) {
        mSendingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showQuestionDetail(QuestionModel question) {
        mQuestion = question;
        Glide.with(AnswerActivity.this)
                .load(question.getAskUser().getUserIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mQuestionerIcon);
        mQuestionerName.setText(question.getAskUser().getUserName());
        mQuestionPrice.setText(String.format(getString(R.string.format_dollar), question.getPrice()));
        mQuestionView.setText(question.getContent());
        mDealedTime.setText(RelativeDateFormat.format(question.getAskTime(), AnswerActivity.this));
    }

    @Override
    public void showUploadAnswerSuccess() {
        showToast(R.string.notice_success);
        setResult(Constant.KEY_FROM_MY_ANSWER);
        finish();
    }
}
