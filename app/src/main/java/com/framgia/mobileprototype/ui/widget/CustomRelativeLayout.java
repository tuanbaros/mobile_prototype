package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.mockdetail.MockDetailContract;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class CustomRelativeLayout extends RelativeLayout implements View.OnTouchListener {
    private static final int MIN_MARGIN = 0;
    private static final int NUMBER_DOT_CONTROL = 4;
    private MockDetailContract.Presenter mPresenter;

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ElementView elementView = (ElementView) View.inflate(view.getContext(), R.layout
                .element, null);
            elementView.setPresenter(mPresenter);
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
            params.width = Constant.MIN_SIZE;
            params.height = Constant.MIN_SIZE;
            elementView.setLayoutParams(params);
            hideControlOfChildView();
            Element element = new Element();
            element.setId((int) mPresenter.saveElement(element));
            element.setGesture(getResources().getString(R.string.title_gesture_tap));
            element.setTransition(getResources().getString(R.string.title_transition_default));
            elementView.setTag(R.string.title_element, element);
            this.addView(elementView);
            this.setTag(elementView);
            if (mPresenter != null) mPresenter.openElementOption();
        }
        return true;
    }

    public void hideControlOfChildView() {
        for (int i = 1; i < this.getChildCount(); i++) {
            ElementView elementView = (ElementView) this.getChildAt(i);
            for (int j = 0; j < NUMBER_DOT_CONTROL; j++) {
                elementView.getChildAt(j).setVisibility(GONE);
            }
        }
    }

    public void setPresenter(MockDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
