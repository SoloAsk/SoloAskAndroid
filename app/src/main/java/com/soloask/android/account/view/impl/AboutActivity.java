package com.soloask.android.account.view.impl;


import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.util.Constant;

import butterknife.BindView;

/**
 * Created by Lebron on 2016/7/6.
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_version_name)
    TextView textView;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViewsAndData() {
        textView.setText(Constant.getVersion(this));
    }

}
