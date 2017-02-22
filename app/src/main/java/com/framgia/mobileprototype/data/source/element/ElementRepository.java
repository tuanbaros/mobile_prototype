package com.framgia.mobileprototype.data.source.element;

import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.source.DataSource;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.element
 */
public class ElementRepository implements DataSource<Element> {
    private static ElementRepository sElementRepository;
    private ElementLocalDataSource mElementLocalDataSource;

    private ElementRepository(ElementLocalDataSource elementLocalDataSource) {
        mElementLocalDataSource = elementLocalDataSource;
    }

    public static ElementRepository getInstance(ElementLocalDataSource elementLocalDataSource) {
        if (sElementRepository == null) {
            sElementRepository = new ElementRepository(elementLocalDataSource);
        }
        return sElementRepository;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        mElementLocalDataSource.getDatas(getListCallback);
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        mElementLocalDataSource.getData(dataId, getListCallback);
    }

    @Override
    public long saveData(Element data) {
        return mElementLocalDataSource.saveData(data);
    }

    @Override
    public void updateData(Element data) {
        mElementLocalDataSource.updateData(data);
    }

    @Override
    public void deleteData(Element data) {
        mElementLocalDataSource.deleteData(data);
    }
}
