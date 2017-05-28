package com.framgia.mobileprototype.explore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class ExploreItemHandler {
    private ExploreContract.Presenter mPresenter;

    public ExploreItemHandler(ExploreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void downloadProject(View view, final Project project) {
        if (mPresenter == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(project.getTitle());
        builder.setMessage(R.string.text_do_you_want_download);
        builder.setNegativeButton(R.string.action_cancel_project, null);
        builder.setPositiveButton(R.string.action_ok,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.downloadProject(project);
            }
        });
        builder.create().show();
    }

    public void showComment(Project project) {
        mPresenter.showComment(project);
    }
}
