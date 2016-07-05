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
    private boolean mShouldMark;
    private Drawable mBoundDrawable;

    public BoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMarkedDrawable();
    }

    public BoundImageView(Context context) {
        this(context, null);
    }

    private void initMarkedDrawable() {
        mBoundDrawable = getResources().getDrawable(R.drawable.ic_verified);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShouldMark) {
            mBoundDrawable.setBounds(getWidth() * 2 / 3, getHeight() * 2 / 3, getWidth(), getHeight());
            mBoundDrawable.draw(canvas);
        }
    }

    public void showMark(boolean isMark) {
        mShouldMark = isMark;
        invalidate();
    }
}
