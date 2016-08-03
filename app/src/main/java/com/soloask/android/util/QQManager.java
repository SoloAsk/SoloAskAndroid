package com.soloask.android.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;


/**
 * Created by lebron on 16-8-3.
 */
public class QQManager {
    private static Tencent mTencent;

    public static Tencent getTencentInstance(Context context) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constant.QQ_APP_ID, context.getApplicationContext());
        }
        return mTencent;
    }

    public static void qqShare(Context context) {
        try {
            Bundle bundle = new Bundle();
            //这条分享消息被好友点击后的跳转URL。
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://play.google.com/store/apps/details?id=com.soloask.android");
            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "你丑你提问，我美我回答");
            //分享的图片URL
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://lh3.googleusercontent.com/wpX5q993X_k-EQN-_mmYHIPXh8R75gJGsbuojUKvYc2JLkXL13Ci9EzJ1N2VYNZcgcuG=w300-rw");
            //分享的消息摘要，最长50个字
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我刚刚在弯弯回答了一个问题，快来围观吧");

            getTencentInstance(context).shareToQQ((Activity) context, bundle, null);
        } catch (Exception e) {

        }
    }
}
