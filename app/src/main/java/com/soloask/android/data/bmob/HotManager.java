package com.soloask.android.data.bmob;

import android.util.Log;

import com.soloask.android.data.model.Question;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Lebron on 2016/7/11.
 */
public class HotManager {
    private OnGetHotListener listener;

    public void setOnGetHotListener(OnGetHotListener listener) {
        this.listener = listener;
    }

    public void getHotList(int skip) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(skip);
        query.addWhereExists("quesVoiceURL");
        query.addWhereEqualTo("isPublic", true);
        query.order("-createdAt");
        query.include("answerUser");
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    listener.onSuccess(list);
                } else {
                    e.printStackTrace();
                    listener.onFailed();
                }
            }
        });
    }

    public interface OnGetHotListener {
        void onSuccess(List<Question> list);

        void onFailed();
    }
}
