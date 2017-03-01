package com.framgia.mobileprototype.projectdetail;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;

import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class ProjectDetailPresenter implements ProjectDetailContract.Presenter {
    private ProjectDetailContract.View mProjectDetailView;
    private MockRepository mMockRepository;

    public ProjectDetailPresenter(MockRepository mockRepository,
                                  ProjectDetailContract.View projectDetailView) {
        mMockRepository = mockRepository;
        mProjectDetailView = projectDetailView;
    }

    @Override
    public void getMocks(String projectId) {
        mMockRepository.getDatas(new DataSource.GetListCallback<Mock>() {
            @Override
            public void onSuccess(List<Mock> datas) {
                mProjectDetailView.mocksLoaded(datas);
            }

            @Override
            public void onError() {
                mProjectDetailView.mocksNotAvailable();
            }
        });
    }

    @Override
    public void openDeleteMockDialog(Mock mock) {
        mProjectDetailView.showDeleteMockDialog();
    }

    @Override
    public void openCreateMockDialog() {
        mProjectDetailView.showCreateMockDialog();
    }

    @Override
    public void start() {
        mProjectDetailView.onPrepare();
    }
}
