package com.framgia.mobileprototype.mockdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.linkto.LandspaceLinkToActivity;
import com.framgia.mobileprototype.ui.widget.ElementView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import java.util.ArrayList;
import java.util.List;

public class LandcapeMockDetailActivity extends MockDetailActivity {

    public static Intent getMockDetailIntent(Context context, Mock mock, Project project) {
        Intent intent = new Intent(context, LandcapeMockDetailActivity.class);
        intent.putExtra(EXTRA_MOCK, mock);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void setUpElement(List<Element> elements) {
        int paddingSize = (int) getResources().getDimension(R.dimen.dp_8);
        float sw = (float) 1 / ScreenSizeUtil.sScaleLandscapeWidth;
        float sh = (float) 1 / ScreenSizeUtil.sScaleLandscapeHeight;
        for (Element element : elements) {
            ElementView elementView =
                    (ElementView) View.inflate(getBaseContext(), R.layout.element, null);
            elementView.setTag(R.string.title_element, element);
            int width = (Math.round(element.getWidth() * sw)) + 2 * paddingSize;
            int height = (Math.round(element.getHeight() * sh)) + 2 * paddingSize;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.leftMargin = Math.round(element.getX() * sw) - paddingSize;
            params.topMargin = Math.round(element.getY() * sh) - paddingSize;
            elementView.setLayoutParams(params);
            elementView.setPresenter(mMockDetailPresenter);
            elementView.setGesture(element.getGesture());
            if (!TextUtils.isEmpty(element.getLinkTo())) elementView.setLinkTo(element.getLinkTo());
            mCustomRelativeLayout.addView(elementView);
        }
        mCustomRelativeLayout.hideControlOfChildView();
    }

    @Override
    public void getAllElementView() {
        if (mCustomRelativeLayout.getChildCount() < 2) {
            Toast.makeText(this, R.string.msg_empty_element, Toast.LENGTH_SHORT).show();
            mCustomRelativeLayout.setEnabled(true);
        } else {
            int paddingSize = (int) getResources().getDimension(R.dimen.dp_8);
            List<Element> elements = new ArrayList<>();
            for (int i = 1; i < mCustomRelativeLayout.getChildCount(); i++) {
                ElementView elementView = (ElementView) mCustomRelativeLayout.getChildAt(i);
                Element element = (Element) elementView.getTag(R.string.title_element);
                element.setMockId(mMock.getId());
                int x = Math.round((elementView.getX() + paddingSize)
                        * ScreenSizeUtil.sScaleLandscapeWidth);
                int y = Math.round((elementView.getY() + paddingSize)
                        * ScreenSizeUtil.sScaleLandscapeHeight);
                int width = Math.round((elementView.getWidth() - 2 * paddingSize)
                        * ScreenSizeUtil.sScaleLandscapeWidth);
                int height = Math.round((elementView.getHeight() - 2 * paddingSize)
                        * ScreenSizeUtil.sScaleLandscapeHeight);
                element.setX(x);
                element.setY(y);
                element.setWidth(width);
                element.setHeight(height);
                if (elementView.getTag() != null) {
                    element.setLinkTo((String) elementView.getTag());
                }
                elements.add(element);
            }
            mMockDetailPresenter.saveAllElement(elements);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_remove:
                ElementView elementView = (ElementView) mCustomRelativeLayout.getTag();
                mCustomRelativeLayout.removeView(elementView);
                mMockDetailPresenter.deleteElement(
                        (Element) elementView.getTag(R.string.title_element));
                hideElementOption();
                break;
            case R.id.action_link:
                ElementView ev = (ElementView) mCustomRelativeLayout.getTag();
                Element element = (Element) ev.getTag(R.string.title_element);
                startActivityForResult(
                        LandspaceLinkToActivity.getLinkToIntent(this, mProject, element),
                        LINKTO_REQUEST_CODE);
                break;
            case R.id.action_gesture:
                showGestureDialog();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (ScreenSizeUtil.sScaleLandscapeWidth == 0 || ScreenSizeUtil.sScaleLandscapeHeight == 0) {
            ActionBar actionBar = getSupportActionBar();
            if (ScreenSizeUtil.sScaleLandscapeWidth == 0) {
                if (actionBar != null) {
                    int actionBarHeight = actionBar.getHeight();
                    int statusBarHeight = getStatusBarHeight();
                    int paddingDistance = 2 * (int) getResources().getDimension(R.dimen.dp_16);
                    int childLandscapeWidth = ScreenSizeUtil.sHeight - (paddingDistance);
                    int childLandscapeHeight = ScreenSizeUtil.sWidth
                            - actionBarHeight
                            - statusBarHeight
                            - paddingDistance;
                    ScreenSizeUtil.sScaleLandscapeWidth =
                            (float) ScreenSizeUtil.sHeight / childLandscapeWidth;
                    ScreenSizeUtil.sScaleLandscapeHeight =
                            (float) ScreenSizeUtil.sWidth / childLandscapeHeight;
                }
            }
        }
        start();
        return true;
    }
}
