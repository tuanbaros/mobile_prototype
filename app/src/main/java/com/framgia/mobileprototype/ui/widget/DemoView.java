package com.framgia.mobileprototype.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.demo.DemoContract;

/**
 * Created by tuannt on 15/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
@SuppressLint("ViewConstructor")
public class DemoView extends View implements View.OnTouchListener {
    private static final float ORIGIN_SCALE_FACTOR = 1.0f;
    private float mDownX;
    private float mDownY;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleDetector;
    private String mAction;
    private DemoContract.Presenter mListener;
    private boolean mIsDone;
    private boolean mIsTouchDown;

    public DemoView(Context context, String action, DemoContract.Presenter listener) {
        super(context);
        mListener = listener;
        mAction = action;
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new MyGesture());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (detector.getScaleFactor() >= ORIGIN_SCALE_FACTOR) onZoomOut();
            else onZoomIn();
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
            mListener.openNextScreen((String) this.getTag(R.string.title_link_to));
            mIsDone = true;
        } else {
            notifyAction();
        }
    }
}
