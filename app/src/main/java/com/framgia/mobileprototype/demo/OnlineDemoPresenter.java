package com.framgia.mobileprototype.demo;

/**
 * Created by FRAMGIA\nguyen.thanh.tuan on 29/05/2017.
 */

public class OnlineDemoPresenter implements DemoContract.Presenter {

    private DemoContract.View mDemoView;

    public OnlineDemoPresenter(DemoContract.View demoView) {
        mDemoView = demoView;
    }

    @Override
    public void start() {

    }

    @Override
    public void getElements(String mockId) {

    }

    @Override
    public void getMock(String mockEntryId) {

    }

    @Override
    public void openNextScreen(String linkTo, String anim) {
        mDemoView.showNextScreen(linkTo, anim);
    }
}
