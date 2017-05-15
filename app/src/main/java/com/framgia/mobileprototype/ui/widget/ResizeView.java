package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.mockdetail.MockDetailContract;

/**
 * Created by tuannt on 07/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.ui.widget
 */
public class ResizeView extends RelativeLayout implements View.OnTouchListener {
    private int mBaseX;
    private int mBaseY;
    private int mBaseW;
    private int mBaseH;
    private int mBaseTopMargin;
    private int mBaseLeftMargin;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mMaxTopMargin;
    private int mMaxLeftMargin;
    private int mMinSize;
    private MockDetailContract.Presenter mPresenter;

    public ResizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        RelativeLayout viewParent;
        if (view.getParent() instanceof ElementView) {
            viewParent = (ElementView) view.getParent();
            setPresenter(((ElementView) viewParent).getPresenter());
            if (mPresenter != null) mPresenter.openElementOption();
        } else {
            viewParent = (AddView) view.getParent();
        }
        int X = (int) event.getRawX();
        int Y = (int) event.getRawY();
        RelativeLayout.LayoutParams params =
            (RelativeLayout.LayoutParams) viewParent.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBaseX = X;
                mBaseY = Y;
                mBaseW = params.width;
                mBaseH = params.height;
                if (viewParent instanceof ElementView) {
                    mMinSize = getResources().getDimensionPixelSize(R.dimen.dp_50);
                } else {
                    mMinSize = getResources().getDimensionPixelSize(R.dimen.dp_20);
                }
                switch (view.getId()) {
                    case R.id.top_right_resize_image_view:
                        mBaseTopMargin = params.topMargin;
                        mBaseLeftMargin = params.leftMargin;
                        mMaxTopMargin = params.topMargin + mBaseH - mMinSize;
                        mMaxHeight = mBaseTopMargin + mBaseH;
                        break;
                    case R.id.bottom_left_resize_image_view:
                        mBaseTopMargin = params.topMargin;
                        mBaseLeftMargin = params.leftMargin;
                        mMaxLeftMargin = mBaseLeftMargin + mBaseW - mMinSize;
                        mMaxWidth = mBaseLeftMargin + mBaseW;
                        break;
                    case R.id.top_left_resize_image_view:
                        mBaseTopMargin = params.topMargin;
                        mBaseLeftMargin = params.leftMargin;
                        mMaxTopMargin = params.topMargin + mBaseH - mMinSize;
                        mMaxHeight = mBaseTopMargin + mBaseH;
                        mMaxLeftMargin = mBaseLeftMargin + mBaseW - mMinSize;
                        mMaxWidth = mBaseLeftMargin + mBaseW;
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (view.getId()) {
                    case R.id.top_right_resize_image_view:
                        int deltaX = X - mBaseX;
                        int deltaY = mBaseY - Y;
                        params.width = mBaseW + deltaX;
                        params.height = mBaseH + deltaY;
                        params.leftMargin = mBaseLeftMargin;
                        params.topMargin = mBaseTopMargin - deltaY;
                        if (params.topMargin < 0) params.topMargin = 0;
                        if (params.topMargin > mMaxTopMargin) params.topMargin = mMaxTopMargin;
                        if (params.height > mMaxHeight) params.height = mMaxHeight;
                        break;
                    case R.id.top_left_resize_image_view:
                        int deltaX2 = mBaseX - X;
                        int deltaY2 = mBaseY - Y;
                        params.width = mBaseW + deltaX2;
                        params.height = mBaseH + deltaY2;
                        params.topMargin = mBaseTopMargin - deltaY2;
                        params.leftMargin = mBaseLeftMargin - deltaX2;
                        if (params.topMargin < 0) params.topMargin = 0;
                        if (params.topMargin > mMaxTopMargin) params.topMargin = mMaxTopMargin;
                        if (params.height > mMaxHeight) params.height = mMaxHeight;
                        if (params.leftMargin < 0) params.leftMargin = 0;
                        if (params.leftMargin > mMaxLeftMargin) params.leftMargin = mMaxLeftMargin;
                        if (params.width > mMaxWidth) params.width = mMaxWidth;
                        break;
                    case R.id.bottom_left_resize_image_view:
                        int deltaX1 = mBaseX - X;
                        int deltaY1 = Y - mBaseY;
                        params.width = mBaseW + deltaX1;
                        params.height = mBaseH + deltaY1;
                        params.topMargin = mBaseTopMargin;
                        params.leftMargin = mBaseLeftMargin - deltaX1;
                        if (params.leftMargin < 0) params.leftMargin = 0;
                        if (params.leftMargin > mMaxLeftMargin) params.leftMargin = mMaxLeftMargin;
                        if (params.width > mMaxWidth) params.width = mMaxWidth;
                        break;
                    case R.id.bottom_right_resize_image_view:
                        params.width = mBaseW + X - mBaseX;
                        params.height = mBaseH + Y - mBaseY;
                        break;
                    default:
                        break;
                }
                if (params.width < mMinSize) params.width = mMinSize;
                if (params.height < mMinSize) params.height = mMinSize;
                viewParent.setLayoutParams(params);
                break;
        }
        return true;
    }

    public void setPresenter(MockDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
