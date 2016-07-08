package com.soloask.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.soloask.android.R;

/**
 * Created by Lebron on 2016/6/30.
 */
public class BoundImageView extends CircleImageView {
    private boolean mVerified;
    private boolean mMentioned;
    private Drawable mVerifiedDrawable;
    private Drawable mMentionedDrawable;

    public BoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMarkedDrawable();
        initMentionedDrawable();
    }

    public BoundImageView(Context context) {
        this(context, null);
    }

    private void initMarkedDrawable() {
        mVerifiedDrawable = getResources().getDrawable(R.drawable.ic_verified);
    }

    private void initMentionedDrawable() {
        mMentionedDrawable = getResources().getDrawable(R.drawable.bg_mentioned);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mVerified) {
            mVerifiedDrawable.setBounds(getWidth() * 2 / 3, getHeight() * 2 / 3, getWidth(), getHeight());
            mVerifiedDrawable.draw(canvas);
        }
        if (mMentioned) {
            mMentionedDrawable.setBounds(getWidth() * 3 / 4, 0, getWidth(), getHeight() / 4);
            mMentionedDrawable.draw(canvas);
        }
    }

    public void showVerify(boolean isVerified) {
        mVerified = isVerified;
        invalidate();
    }

    public void showMention(boolean isMentioned) {
        mMentioned = isMentioned;
        invalidate();
    }
}
