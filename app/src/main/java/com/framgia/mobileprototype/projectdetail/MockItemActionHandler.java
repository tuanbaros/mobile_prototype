package com.framgia.mobileprototype.projectdetail;

import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class MockItemActionHandler {
    private ProjectDetailContract.Presenter mListener;

    public MockItemActionHandler(ProjectDetailContract.Presenter listener) {
        mListener = listener;
    }

    public void completeChanged(Mock mock, boolean isRemoving) {
        if (mListener == null) return;
        mock.setCheckToDelete(isRemoving);
        if (isRemoving) mListener.addMockToRemoveList(mock);
        else mListener.clearMockFromRemoveList(mock);
    }

    public void openMockDetail(Mock mock) {
        if (mListener == null) return;
        mListener.openMockDetail(mock);
    }

    private void editMock(Mock mock) {
        if (mListener == null) return;
        mListener.openEditMockDialog(mock);
    }

    private void cloneMock(boolean isPortrait, Mock mock) {
        if (mListener == null) return;
        mListener.openCloneMockDialog(isPortrait, mock);
    }

    public void openMockOption(View view, final boolean isPortrait, final Mock mock) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.activity_project_detail_mock_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        editMock(mock);
                        break;
                    case R.id.action_clone:
                        cloneMock(isPortrait, mock);
                        break;
                    case R.id.action_remove:
                        mListener.addMockToRemoveList(mock);
                        mListener.openDeleteMockDialog();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
