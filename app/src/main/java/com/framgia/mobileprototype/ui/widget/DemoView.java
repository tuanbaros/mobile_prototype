package com.framgia.mobileprototype.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.demo.DemoContract;

/**
 * Created by tuannt on 15/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
@SuppressLint("ViewConstructor")
public class DemoView extends View implements View.OnTouchListener {
    private float mDownX;
    private float mDownY;
    private GestureDetector mGestureDetector;
    private String mAction;
    private DemoContract.Presenter mListener;
    private boolean mIsDone;
    private boolean mIsTouchDown;
    private float mBaseX;

    public DemoView(Context context, String action, DemoContract.Presenter listener) {
        super(context);
        mListener = listener;
        mAction = action;
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new MyGesture());
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getPointerCount() > 1) {
            zoomEvent(event);
            mIsTouchDown = false;
            return true;
        }
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mIsTouchDown = true;
                mDownX = event.getX();
                mDownY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();
                float deltaX = -mDownX + upX;
                float deltaY = mDownY - upY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX > 0) {
                        onRightSwipe();
                        return true;
                    }
                    if (deltaX < 0) {
                        onLeftSwipe();
                        return true;
                    }
                } else {
                    if (deltaY < 0) {
                        onDownSwipe();
                        return true;
                    }
                    if (deltaY > 0) {
                        onUpSwipe();
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void onRightSwipe() {
        doAction(R.string.title_gesture_swiperight);
    }

    public void onLeftSwipe() {
        doAction(R.string.title_gesture_swipeleft);
    }

    public void onDownSwipe() {
        doAction(R.string.title_gesture_swipedown);
    }

    public void onUpSwipe() {
        doAction(R.string.title_gesture_swipeup);
    }

    public void onZoomOut() {
        doAction(R.string.title_gesture_zoomout);
    }

    public void onZoomIn() {
        doAction(R.string.title_gesture_zoomin);
    }

    private class MyGesture extends GestureDetector.SimpleOnGestureListener {
        MyGesture() {
            super();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            doAction(R.string.title_gesture_doubletap);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            doAction(R.string.title_gesture_tap);
            return true;
        }
    }

    private void notifyAction() {
        if (mIsDone) return;
        if (mIsTouchDown)
            Toast.makeText(getContext(), getResources().getString(R.string.msg_please_action,
                mAction), Toast.LENGTH_SHORT).show();
        mIsTouchDown = false;
    }

    private void doAction(int resId) {
        if (getResources().getString(resId).equals(mAction)) {
            Element element = (Element) this.getTag(R.string.title_element);
            mListener.openNextScreen(element.getLinkTo(), element.getTransition());
            mIsDone = true;
        } else {
            notifyAction();
        }
        mBaseX = 0;
    }

    private void zoomEvent(MotionEvent event) {
        if (mBaseX == 0) mBaseX = Math.abs(event.getX(0) - event.getX(1));
        if (event.getAction() == MotionEvent.ACTION_CANCEL ||
            event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            mIsTouchDown = true;
            float currentX = Math.abs(event.getX(0) - event.getX(1));
            if (currentX > mBaseX) onZoomOut();
            else onZoomIn();
        }
    }
}
