package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.mockdetail.MockDetailContract;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class ElementView extends RelativeLayout implements View.OnTouchListener {
    private int mXDelta;
    private int mYDelta;
    private static final int NUMBER_DOT_CONTROL = 4;
    private MockDetailContract.Presenter mPresenter;

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        RelativeLayout parentView = (RelativeLayout) view.getParent();
        hideAnotherElement();
        showControlView();
        view.bringToFront();
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        RelativeLayout.LayoutParams params =
            (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mXDelta = X - params.leftMargin;
                mYDelta = Y - params.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                int maxLeft = parentView.getWidth() - params.width;
                int maxTop = parentView.getHeight() - params.height;
                int minLeft = 0;
                int minTop = 0;
                params.leftMargin = X - mXDelta;
                params.topMargin = Y - mYDelta;
                if (params.leftMargin < minLeft) params.leftMargin = minLeft;
                if (params.topMargin < minTop) params.topMargin = minTop;
                if (params.leftMargin > maxLeft) params.leftMargin = maxLeft;
                if (params.topMargin > maxTop) params.topMargin = maxTop;
                view.setLayoutParams(params);
                break;
        }
        if (mPresenter != null) mPresenter.openElementOption();
        parentView.setTag(view);
        return true;
    }

    private void hideAnotherElement() {
        RelativeLayout parentView = (RelativeLayout) this.getParent();
        for (int i = 1; i < parentView.getChildCount(); i++) {
            ElementView elementView = (ElementView) parentView.getChildAt(i);
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

    public void setPresenter(MockDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public MockDetailContract.Presenter getPresenter() {
        return mPresenter;
    }

    public void setLinkTo(String mockEntryId) {
        if (TextUtils.isEmpty(mockEntryId)) return;
        ((Element) this.getTag(R.string.title_element)).setLinkTo(mockEntryId);
        this.getChildAt(NUMBER_DOT_CONTROL).setBackgroundResource(R.drawable.link_to_selector);
        for (int j = 0; j < NUMBER_DOT_CONTROL; j++) {
            this.getChildAt(j).setBackgroundResource(R.drawable.link_to_resize);
        }
        this.setTag(R.string.title_link_to, mockEntryId);
    }

    public void setGesture(String gesture) {
        Element element = (Element) getTag(R.string.title_element);
        if (gesture == null) {
            gesture = getResources().getString(R.string.title_gesture_tap);
        }
        element.setGesture(gesture);
        ImageView imageView = (ImageView) getChildAt(getChildCount() - 1);
        imageView.setImageResource(getIconResource(gesture));
    }

    private int getIconResource(String gesture) {
        Resources resources = getResources();
        if (gesture.equals(resources.getString(R.string.title_gesture_tap))) {
            return R.drawable.ic_gesture_tap;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_swipeup))) {
            return R.drawable.ic_gesture_swipe_top;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_swipedown))) {
            return R.drawable.ic_gesture_swipe_down;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_swipeleft))) {
            return R.drawable.ic_gesture_swipe_left;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_swiperight))) {
            return R.drawable.ic_gesture_swipe_right;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_zoomin))) {
            return R.drawable.ic_gesture_zoom_in;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_zoomout))) {
            return R.drawable.ic_gesture_zoom_out;
        }
        if (gesture.equals(resources.getString(R.string.title_gesture_doubletap))) {
            return R.drawable.ic_gesture_double_tap;
        }
        return R.drawable.ic_gesture_tap;
    }
}
