package com.soloask.android.util;

import android.util.Log;


import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Lebron on 2016/7/12.
 */
public class AnswerManager {

    private OnUpLoadAnswerListener mListener;

    public void setOnUpLoadAnswerListener(OnUpLoadAnswerListener listener) {
        mListener = listener;
    }

    public void upLoadAnswer(String filePath) {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null && bmobFile.getFileUrl() != null) {
                    Log.i("AnswerManager", bmobFile.getFileUrl());
                    mListener.onSuccess(bmobFile.getFileUrl());
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    public interface OnUpLoadAnswerListener {
        void onSuccess(String url);

        void onFailed();
    }
}
