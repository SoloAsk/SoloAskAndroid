package com.soloask.android.data.bmob;

import android.text.TextUtils;
import android.util.Log;

import com.soloask.android.data.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Lebron on 2016/7/11.
 */
public class UserManager {
    private UserLoginListener mListener;
    private UserInfoListener mInfoListener;

    public void setUserLoginListener(UserLoginListener listener) {
        mListener = listener;
    }

    public void setUserInfoListener(UserInfoListener listener) {
        mInfoListener = listener;
    }

    private void sign(String id, String name, String icon, String deviceToken) {
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setUserIcon(icon);
        user.setUserPrice(0.99);
        user.setUserIntroduce("Something");
        user.setUserTitle("Something");
        user.setUserIncome(0.0);
        user.setUserEarned(0.0);
        user.setAnswerQuesNum(0);
        user.setAskQuesNum(0);
        user.setHeardQuesNum(0);
        user.setDeviceToken(deviceToken);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mListener.onSuccess(s);
                } else {
                    e.printStackTrace();
                    mListener.onFailed();
                }
            }
        });
    }

    public void signOrLogin(final String id, final String name, final String icon, final String deviceToken) {
        BmobQuery<User> query = new BmobQuery<>();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(icon)) {
            mListener.onFailed();
            return;
        }
        query.addWhereEqualTo("userId", id);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mListener.onSuccess(list.get(0).getObjectId());
                    } else {
                        Log.i("UserManager", " signin");
                        sign(id, name, icon, deviceToken);
                    }
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    public void getUserInfo(String id) {
        BmobQuery<User> query = new BmobQuery<>();
        if (id == null) {
            mInfoListener.onFailed();
            return;
        }
        query.getObject(id, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mInfoListener.onSuccess(user);
                } else {
                    mInfoListener.onFailed();
                }
            }
        });
    }

    public void updateUserInfo(final User user, String title, String describe, double askPrice) {
        if (user == null) {
            mInfoListener.onFailed();
            return;
        }
        user.setUserTitle(title);
        user.setUserIntroduce(describe);
        user.setUserPrice(askPrice);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mInfoListener.onSuccess(user);
                } else {
                    mInfoListener.onFailed();
                }
            }
        });
    }

    public interface UserLoginListener {
        void onSuccess(String objectId);

        void onFailed();
    }

    public interface UserInfoListener {
        void onSuccess(User user);

        void onFailed();
    }
}
