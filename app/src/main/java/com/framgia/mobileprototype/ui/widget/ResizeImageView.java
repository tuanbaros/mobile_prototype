package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class ResizeImageView extends ImageView implements View.OnTouchListener {
    private int mBaseX, mBaseY, mBaseW, mBaseH, mMargl, mMargt;
    private RelativeLayout.LayoutParams mLayoutParams;
    private static final int MIN_WIDTH = 150;

    public ResizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        ElementView elementView = (ElementView) view.getParent();
        RelativeLayout parentView = (RelativeLayout) elementView.getParent().getParent();
        int size = (int) getResources().getDimension(R.dimen.dp_100);
        int j = (int) event.getRawX();
        int i = (int) event.getRawY();
        mLayoutParams = (RelativeLayout.LayoutParams) elementView.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                elementView.invalidate();
                mBaseX = j;
                mBaseY = i;
                mBaseW = elementView.getWidth();
                mBaseH = elementView.getHeight();
                int[] location = new int[2];
                elementView.getLocationOnScreen(location);
                mMargl = mLayoutParams.leftMargin;
                mMargt = mLayoutParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                int maxLeft = parentView.getWidth() - size;
                int maxTop = parentView.getHeight() - size;
                int minLeft = 0;
                int minTop = 0;
                float f2 = (float) Math.toDegrees(Math.atan2(i - mBaseY, j - mBaseX));
                float f1 = f2;
                if (f2 < 0.0F) {
                    f1 = f2 + 360.0F;
                }
                j -= mBaseX;
                int k = i - mBaseY;
                i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
                    - elementView.getRotation())));
                j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
                    - elementView.getRotation())));
                k = i * 2 + mBaseW;
                int m = j * 2 + mBaseH;
                if (k > MIN_WIDTH) {
                    mLayoutParams.width = k;
                    mLayoutParams.leftMargin = (mMargl - i);
                }
                if (m > MIN_WIDTH) {
                    mLayoutParams.height = m;
                    mLayoutParams.topMargin = (mMargt - j);
                }
                if (mLayoutParams.leftMargin < minLeft) mLayoutParams.leftMargin = minLeft;
                if (mLayoutParams.topMargin < minTop) mLayoutParams.topMargin = minTop;
                if (mLayoutParams.leftMargin > maxLeft) mLayoutParams.leftMargin = maxLeft;
                if (mLayoutParams.topMargin > maxTop) mLayoutParams.topMargin = maxTop;
                elementView.setLayoutParams(mLayoutParams);
                elementView.performLongClick();
                break;
        }
        return true;
    }
}
