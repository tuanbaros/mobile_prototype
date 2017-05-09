package com.framgia.mobileprototype.library;

/**
 * Created by tuannt on 5/8/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.library
 */
public class LibraryPresenter implements LibraryContract.Presenter {

    private LibraryContract.View mLibraryView;

    public LibraryPresenter(LibraryContract.View libraryView) {
        mLibraryView = libraryView;
    }

    @Override
    public void start() {
        mLibraryView.start();
    }

    @Override
    public void handleShowOption() {
        mLibraryView.showOption();
    }
}
