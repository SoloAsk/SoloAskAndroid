package com.soloask.android.util;

import android.app.Activity;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.soloask.android.R;

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
                        .setContentDescription(activity.getString(R.string.share_content))
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.soloask.android"))
                        .build();
                mShareDialog.show(linkContent);
            }
        } catch (Exception e) {
        }

    }
}
