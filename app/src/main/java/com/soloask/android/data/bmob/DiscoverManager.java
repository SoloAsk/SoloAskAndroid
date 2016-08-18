package com.soloask.android.data.bmob;

import com.soloask.android.data.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Lebron on 2016/7/11.
 */
public class DiscoverManager {
    private OnGetDiscoverListener listener;

    public void setOnGetDiscoverListener(OnGetDiscoverListener listener) {
        this.listener = listener;
    }

    public void getUserList(int skip) {
        BmobQuery<User> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-answerQuesNum,-askQuesNum,-createdAt");
        query.setSkip(skip);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    listener.onSuccess(list);
                } else {
                    e.printStackTrace();
                    listener.onFailed();
                }
            }
        });
    }

    public interface OnGetDiscoverListener {
        void onSuccess(List<User> list);

        void onFailed();
    }
}
