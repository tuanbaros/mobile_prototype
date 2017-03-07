package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class CustomRelativeLayout extends RelativeLayout implements View.OnTouchListener {
    private static final int MIN_MARGIN = 0;
    private static final int NUMBER_DOT_CONTROL = 4;

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            RelativeLayout element = (RelativeLayout) View.inflate(view.getContext(), R.layout
                .element, null);
            ElementView elementView = (ElementView) element.getChildAt(0);
            int size = (int) getResources().getDimension(R.dimen.dp_100);
            int maxLeft = view.getWidth() - size;
            int minLeft = MIN_MARGIN;
            int maxTop = view.getHeight() - size;
            int minTop = MIN_MARGIN;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
            int left = (int) motionEvent.getX() - size / 2;
            if (left >= minLeft || left <= maxLeft) params.leftMargin = left;
            if (left < minLeft) params.leftMargin = minLeft;
            if (left > maxLeft) params.leftMargin = maxLeft;
            int top = (int) motionEvent.getY() - size / 2;
            if (top >= minTop || top <= maxTop) params.topMargin = top;
            if (top < minTop) params.topMargin = MIN_MARGIN;
            if (top > maxTop) params.topMargin = maxTop;
            elementView.setLayoutParams(params);
            hideControlOfChildView();
            this.addView(element);
        }
        return true;
    }

    private void hideControlOfChildView() {
        for (int i = 1; i < this.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) this.getChildAt(i);
            ElementView elementView = (ElementView) relativeLayout.getChildAt(0);
            for (int j = 0; j < NUMBER_DOT_CONTROL; j++) {
                elementView.getChildAt(j).setVisibility(GONE);
            }
        }
    }
}
