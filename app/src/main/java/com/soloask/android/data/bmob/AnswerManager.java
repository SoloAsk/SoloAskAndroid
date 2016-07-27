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
                if (e == null && bmobFile.getFileUrl() != null) {
                    Log.i("AnswerManager", bmobFile.getFileUrl() + voiceLength);
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
        user.increment("earning", question.getQuesPrice());
        user.increment("income", question.getQuesPrice());
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("AnswerManager", "I answered ++ and You have earned some money");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnUpLoadAnswerListener {
        void onSuccess();

        void onFailed();
    }
}
