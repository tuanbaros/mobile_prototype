package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.mockdetail.MockDetailContract;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class ResizeImageView extends ImageView implements View.OnTouchListener {
    private int mBaseX, mBaseY, mBaseW, mBaseH, mMargl, mMargt;
    private static final int MIN_WIDTH = 150;
    private MockDetailContract.Presenter mPresenter;

    public ResizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        ElementView elementView = (ElementView) view.getParent();
        setPresenter(elementView.getPresenter());
        if (mPresenter != null) mPresenter.openElementOption();
        RelativeLayout parentView = (RelativeLayout) elementView.getParent();
        int size = (int) getResources().getDimension(R.dimen.dp_100);
        int j = (int) event.getRawX();
        int i = (int) event.getRawY();
        RelativeLayout.LayoutParams layoutParams =
            (RelativeLayout.LayoutParams) elementView.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                elementView.invalidate();
                mBaseX = j;
                mBaseY = i;
                mBaseW = elementView.getWidth();
                mBaseH = elementView.getHeight();
                int[] location = new int[2];
                elementView.getLocationOnScreen(location);
                mMargl = layoutParams.leftMargin;
                mMargt = layoutParams.topMargin;
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
                    layoutParams.width = k;
                    layoutParams.leftMargin = (mMargl - i);
                }
                if (m > MIN_WIDTH) {
                    layoutParams.height = m;
                    layoutParams.topMargin = (mMargt - j);
                }
                if (layoutParams.leftMargin < minLeft) layoutParams.leftMargin = minLeft;
                if (layoutParams.topMargin < minTop) layoutParams.topMargin = minTop;
                if (layoutParams.leftMargin > maxLeft) layoutParams.leftMargin = maxLeft;
                if (layoutParams.topMargin > maxTop) layoutParams.topMargin = maxTop;
                elementView.setLayoutParams(layoutParams);
                elementView.performLongClick();
                break;
        }
        return true;
    }

    public void setPresenter(MockDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
