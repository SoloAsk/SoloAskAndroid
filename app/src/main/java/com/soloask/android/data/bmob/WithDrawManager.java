package com.soloask.android.data.bmob;

import com.soloask.android.data.model.User;
import com.soloask.android.data.model.Withdraw;
import com.soloask.android.util.RelativeDateFormat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Lebron on 2016/7/14.
 */
public class WithDrawManager {
    private OnWithDrawListener mListener;

    public void setOnWithDrawListener(OnWithDrawListener listener) {
        mListener = listener;
    }

    public void dealWithdraw(User user, String paypalaccount) {
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setPaypalAccount(paypalaccount);
        withdraw.setDealed(false);
        withdraw.setApplyTime(RelativeDateFormat.getCurrentTime());
        withdraw.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mListener.onSuccess(s);
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    public interface OnWithDrawListener {
        void onSuccess(String objectId);

        void onFailed();
    }
}
