package com.soloask.android.data.bmob;

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

    private void sign(String id, String name, String icon) {
        final User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setUserIcon(icon);
        user.setUserPrice(0.99);
        user.setUserIntroduce("上知天文地理，下晓鸡毛蒜皮");
        user.setUserTitle("神秘人士");
        user.setUserIncome(0.0);
        user.setUserEarned(0.0);
        user.setAnswerQuesNum(0);
        user.setAskQuesNum(0);
        user.setHeardQuesNum(0);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    user.setObjectId(s);
                    mListener.onSuccess(user);
                } else {
                    e.printStackTrace();
                    mListener.onFailed();
                }
            }
        });
    }

    public void signOrLogin(final String id, final String name, final String icon) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", id);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mListener.onSuccess(list.get(0));
                    } else {
                        Log.i("UserManager", " signin");
                        sign(id, name, icon);
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
                    Log.i("UserManager", e.toString());
                    mInfoListener.onFailed();
                }
            }
        });
    }

    public void updateUserInfo(final User user) {
        if (user == null) {
            mInfoListener.onFailed();
            return;
        }
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
        void onSuccess(User user);

        void onFailed();
    }

    public interface UserInfoListener {
        void onSuccess(User user);

        void onFailed();
    }
}
