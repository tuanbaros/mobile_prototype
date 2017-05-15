package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.library.LibraryContract;

/**
 * Created by tuannt on 5/6/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class AddView extends RelativeLayout implements View.OnTouchListener {
    private static final int NUMBER_DOT_CONTROL = 4;
    private int mXDelta;
    private int mYDelta;
    private LibraryContract.Presenter mPresenter;
    private int mType;

    public AddView(Context context) {
        super(context);
    }

    public AddView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setPresenter(LibraryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public AddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        RelativeLayout parentView = (RelativeLayout) view.getParent();
        hideAnotherView();
        showControl();
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
                if (maxLeft < minLeft) maxLeft = minLeft;
                if (maxTop < minTop) maxTop = minTop;
                params.leftMargin = X - mXDelta;
                params.topMargin = Y - mYDelta;
                if (params.leftMargin < minLeft) params.leftMargin = minLeft;
                if (params.topMargin < minTop) params.topMargin = minTop;
                if (params.leftMargin > maxLeft) params.leftMargin = maxLeft;
                if (params.topMargin > maxTop) params.topMargin = maxTop;
                view.setLayoutParams(params);
                break;
        }
        if (mPresenter != null) mPresenter.handleShowOption();
        parentView.setTag(view);
        return true;
    }

    public void hideControl() {
        for (int j = 1; j <= NUMBER_DOT_CONTROL; j++) {
            getChildAt(j).setVisibility(GONE);
        }
        setBackgroundResource(0);
    }

    private void showControl() {
        for (int j = 1; j <= NUMBER_DOT_CONTROL; j++) {
            getChildAt(j).setVisibility(VISIBLE);
        }
        setBackgroundResource(R.drawable.border_add_view);
    }

    private void hideAnotherView() {
        RelativeLayout parentView = (RelativeLayout) getParent();
        for (int i = 0; i < parentView.getChildCount(); i++) {
            AddView addView = (AddView) parentView.getChildAt(i);
            addView.hideControl();
        }
    }
}
