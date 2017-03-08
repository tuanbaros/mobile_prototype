package com.framgia.mobileprototype.mockdetail;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;

import java.util.List;

/**
 * Created by tuannt on 06/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.mockdetail
 */
public class MockDetailPresenter implements MockDetailContract.Presenter {
    private MockDetailContract.View mMockDetailView;
    private ElementRepository mElementRepository;

    public MockDetailPresenter(MockDetailContract.View mockDetailView,
                               ElementRepository elementRepository) {
        mElementRepository = elementRepository;
        mMockDetailView = mockDetailView;
    }

    @Override
    public void getElements(String mockId) {
        mElementRepository.getData(mockId, new DataSource.GetListCallback<Element>() {
            @Override
            public void onSuccess(List<Element> datas) {
                mMockDetailView.elementsLoaded(datas);
            }

            @Override
            public void onError() {
                mMockDetailView.elementsNotAvailable();
            }
        });
    }

    @Override
    public void openElementOption() {
        mMockDetailView.showElementOption();
    }

    @Override
    public void start() {
        mMockDetailView.onPrepare();
    }
}
