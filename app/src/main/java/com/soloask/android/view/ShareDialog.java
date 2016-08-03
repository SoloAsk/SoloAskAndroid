package com.soloask.android.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.soloask.android.R;
import com.soloask.android.util.FacebookManager;
import com.soloask.android.util.QQManager;

/**
 * Created by Lebron on 2016/7/2.
 */
public class ShareDialog extends AlertDialog implements View.OnClickListener {
    private ImageView mFacebookView, mTwitterView, mQQView;
    private Context mContext;

    public ShareDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        mFacebookView = (ImageView) findViewById(R.id.img_share_facebook);
        mTwitterView = (ImageView) findViewById(R.id.img_share_twitter);
        mQQView = (ImageView) findViewById(R.id.img_share_qq);
        mFacebookView.setOnClickListener(this);
        mTwitterView.setOnClickListener(this);
        mQQView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share_facebook:
                FacebookManager.facebookShare((Activity) mContext);
                break;
            case R.id.img_share_twitter:
                //TwitterManager.twitterShare(mContext);
                break;
            case R.id.img_share_qq:
                QQManager.qqShare(mContext);
                break;
        }
    }
}
