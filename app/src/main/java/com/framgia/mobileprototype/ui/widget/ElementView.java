package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class ElementView extends RelativeLayout implements View.OnTouchListener {
    private int mXDelta;
    private int mYDelta;
    private static final int NUMBER_DOT_CONTROL = 4;

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        RelativeLayout parentView = (RelativeLayout) view.getParent().getParent();
        hideAnotherElement();
        showControlView();
        view.bringToFront();
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
                mXDelta = X - params.leftMargin;
                mYDelta = Y - params.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                int sizeWidth = view.getWidth();
                int sizeHeight = view.getHeight();
                int maxLeft = parentView.getWidth() - sizeWidth;
                int maxTop = parentView.getHeight() - sizeHeight;
                int minLeft = 0;
                int minTop = 0;
                RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - mXDelta;
                layoutParams.topMargin = Y - mYDelta;
                if (layoutParams.leftMargin < minLeft) layoutParams.leftMargin = minLeft;
                if (layoutParams.topMargin < minTop) layoutParams.topMargin = minTop;
                if (layoutParams.leftMargin > maxLeft) layoutParams.leftMargin = maxLeft;
                if (layoutParams.topMargin > maxTop) layoutParams.topMargin = maxTop;
                view.setLayoutParams(layoutParams);
                break;
        }
        parentView.invalidate();
        return true;
    }

    private void hideAnotherElement() {
        RelativeLayout parentView = (RelativeLayout) this.getParent().getParent();
        for (int i = 1; i < parentView.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) parentView.getChildAt(i);
            ElementView elementView = (ElementView) relativeLayout.getChildAt(0);
            for (int j = 0; j < NUMBER_DOT_CONTROL; j++) {
                elementView.getChildAt(j).setVisibility(GONE);
            }
        }
    }

    private void showControlView() {
        for (int j = 0; j < NUMBER_DOT_CONTROL; j++) {
            this.getChildAt(j).setVisibility(VISIBLE);
        }
    }
}
