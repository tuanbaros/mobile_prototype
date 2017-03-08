package com.framgia.mobileprototype.linkto;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;

import java.util.List;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.linkto
 */
public class LinkToPresenter implements LinkToContract.Presenter {
    private LinkToContract.View mLinkToView;
    private MockRepository mMockRepository;

    public LinkToPresenter(LinkToContract.View linkToView,
                           MockRepository mockRepository) {
        mLinkToView = linkToView;
        mMockRepository = mockRepository;
    }

    @Override
    public void start() {
    }

    @Override
    public void getMocks(String projectId) {
        mMockRepository.getData(projectId, new DataSource.GetListCallback<Mock>() {
            @Override
            public void onSuccess(List<Mock> datas) {
                mLinkToView.mockLoaded(datas);
            }

            @Override
            public void onError() {
                mLinkToView.mockNotAvailable();
            }
        });
    }

    @Override
    public void chooseMock(Mock mock) {
        mLinkToView.saveLinkTo(mock);
    }
}
