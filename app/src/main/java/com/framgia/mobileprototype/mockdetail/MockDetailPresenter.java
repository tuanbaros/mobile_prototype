package com.framgia.mobileprototype.mockdetail;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.util.EntryIdUtil;

import java.util.List;

/**
 * Created by tuannt on 06/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.mockdetail
 */
public class MockDetailPresenter implements MockDetailContract.Presenter {
    private MockDetailContract.View mMockDetailView;
    private ElementRepository mElementRepository;
    private String mMockId;

    public MockDetailPresenter(MockDetailContract.View mockDetailView,
                               ElementRepository elementRepository) {
        mElementRepository = elementRepository;
        mMockDetailView = mockDetailView;
    }

    @Override
    public void getElements(String mockId) {
        mMockId = mockId;
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
    public void getAllElementInMock() {
        mMockDetailView.getAllElementView();
    }

    @Override
    public void saveAllElement(List<Element> elements) {
        for (Element element : elements) {
            long elementId = mElementRepository.saveData(element);
            if (elementId < 1) mElementRepository.updateData(element);
        }
        mMockDetailView.onSaveElementDone();
    }

    @Override
    public long saveElement(Element element) {
        return mElementRepository.saveData(element);
    }

    @Override
    public long updateElement(Element element) {
        return mElementRepository.updateData(element);
    }

    @Override
    public void deleteElement(Element element) {
        mElementRepository.deleteData(element);
    }

    @Override
    public void setElementGesture(String gesture) {
        mMockDetailView.hideGestureDialog();
        mMockDetailView.showElementGesture(gesture);
    }

    @Override
    public void start() {
        mMockDetailView.onPrepare();
    }

    @Override
    public String getMockId() {
        return mMockId;
    }
}
