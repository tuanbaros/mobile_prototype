package com.framgia.mobileprototype.projects;

import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;

/**
 * Created by tuannt on 24/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public class ProjectItemActionHandler {
    private ProjectsContract.Presenter mListener;

    public ProjectItemActionHandler(ProjectsContract.Presenter listener) {
        mListener = listener;
    }

    public void editProject(Project project) {
        mListener.editProject(project);
    }

    public void removeProject(Project project, int position) {
        mListener.requestDeleteProject(project, position);
    }

    public void openDetailProject(Project project) {
        mListener.openDetailProject(project);
    }

    public void openProjectOption(View view, final int position, final Project project) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.activity_projects_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        editProject(project);
                        break;
                    case R.id.action_share:
                        break;
                    case R.id.action_remove:
                        removeProject(project, position);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
