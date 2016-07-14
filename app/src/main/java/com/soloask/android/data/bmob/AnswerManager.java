package com.soloask.android.data.bmob;

import android.util.Log;

import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Lebron on 2016/7/12.
 */
public class AnswerManager {

    private OnUpLoadAnswerListener mListener;

    public void setOnUpLoadAnswerListener(OnUpLoadAnswerListener listener) {
        mListener = listener;
    }

    public void upLoadAnswer(final Question question, String filePath, final int voiceLength) {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("Lebron", bmobFile.getFileUrl());
                    updateQuestion(question, bmobFile.getFileUrl(), voiceLength);
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    private void updateQuestion(final Question question, String voiceUrl, int voiceLength) {
        question.setQuesVoiceURL(voiceUrl);
        question.setVoiceTime(voiceLength);
        question.setState(Constant.STATUS_ANSWERED);
        question.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateUserAnswerNum(question);
                    updateUserEarning(question);
                    mListener.onSuccess();
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    private void updateUserAnswerNum(Question question) {
        User user = question.getAnswerUser();
        user.increment("answerQuesNum");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("Lebron", "I answered ++");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUserEarning(Question question) {
        User user = question.getAnswerUser();
        user.increment("earning", question.getQuesPrice());
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("AnswerManager", "You have earned some money");
                }
            }
        });
    }

    public interface OnUpLoadAnswerListener {
        void onSuccess();

        void onFailed();
    }
}
