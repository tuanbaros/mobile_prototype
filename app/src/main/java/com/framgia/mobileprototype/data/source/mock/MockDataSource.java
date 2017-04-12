package com.framgia.mobileprototype.data.source.mock;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataSource;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.mock
 */
public interface MockDataSource extends DataSource<Mock> {
    interface GetCallback {
        void onMockLoaded(Mock mock);
        void onMockNotAvailable();
    }
    void getMockByEntryId(String mockEntryId, GetCallback getCallback);
    void getSameOrientationProject(String orientation, GetListCallback<Project> getListCallback);
}
