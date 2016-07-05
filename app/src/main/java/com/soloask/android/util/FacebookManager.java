package com.soloask.android.util;

import android.app.Activity;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Lebron on 2016/6/25.
 */
public class FacebookManager {
    public static void facebookShare(final Activity activity) {
        try {
            ShareDialog mShareDialog = new ShareDialog(activity);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("SoloAsk")
                        .setContentDescription(
                                "I'm talking with LBJ in SoloAsk")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=home.solo.launcher.free&hl=zh-CN"))
                        .build();
                mShareDialog.show(linkContent);
            }
        } catch (Exception e) {
        }

    }
}
