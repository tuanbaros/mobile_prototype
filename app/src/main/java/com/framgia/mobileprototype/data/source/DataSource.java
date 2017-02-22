package com.framgia.mobileprototype.data.source;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source
 */
public interface DataSource<T> {
    interface GetListCallback<T> {
        void onSuccess(List<T> datas);
        void onError();
    }
    void getDatas(@NonNull GetListCallback getListCallback);
    void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback);
    void saveData(T data);
    void updateData(T data);
    void deleteData(T data);
}
