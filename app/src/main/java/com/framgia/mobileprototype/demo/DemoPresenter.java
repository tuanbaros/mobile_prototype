package com.framgia.mobileprototype.demo;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;

import java.util.List;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype
 */
public class DemoPresenter implements DemoContract.Presenter {
    private DemoContract.View mDemoView;
    private MockRepository mMockRepository;
    private ElementRepository mElementRepository;

    public DemoPresenter(DemoContract.View demoView,
                         ElementRepository elementRepository,
                         MockRepository mockRepository) {
        mDemoView = demoView;
        mElementRepository = elementRepository;
        mMockRepository = mockRepository;
    }

    @Override
    public void start() {
    }

    @Override
    public void getElements(String mockId) {
        mElementRepository.getData(mockId, new DataSource.GetListCallback<Element>() {
            @Override
            public void onSuccess(List<Element> datas) {
                mDemoView.onElementLoaded(datas);
            }

            @Override
            public void onError() {
                mDemoView.onElementError();
            }
        });
    }

    @Override
    public void getMock(String mockEntryId) {
        mMockRepository.getMockByEntryId(mockEntryId, new MockDataSource.GetCallback() {
            @Override
            public void onMockLoaded(Mock mock) {
                mDemoView.onMockLoaded(mock);
            }

            @Override
            public void onMockNotAvailable() {
                mDemoView.onMockError();
            }
        });
    }

    @Override
    public void openNextScreen(String linkTo, String anim) {
        mDemoView.showNextScreen(linkTo, anim);
    }
}
