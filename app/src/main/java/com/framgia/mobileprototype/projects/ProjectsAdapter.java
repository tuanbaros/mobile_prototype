package com.framgia.mobileprototype.projects;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ItemProjectBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 24/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {
    private List<Project> mProjects = new ArrayList<>();
    private List<Project> mProjectsCopy = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private ProjectsContract.Presenter mListener;

    ProjectsAdapter(Context context, List<Project> projects,
                    ProjectsContract.Presenter listener) {
        if (projects != null) {
            mProjects.addAll(projects);
            mProjectsCopy.addAll(projects);
        }
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    void updateData(Project project) {
        if (project != null) {
            mProjects.add(0, project);
            mProjectsCopy.add(0, project);
            notifyDataSetChanged();
        }
    }

    void removeItem(int position) {
        mProjects.remove(position);
        mProjectsCopy.remove(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProjectBinding mItemProjectBinding;
        private ProjectItemActionHandler mProjectItemActionHandler;

        ViewHolder(ItemProjectBinding itemProjectBinding) {
            super(itemProjectBinding.getRoot());
            mItemProjectBinding = itemProjectBinding;
            mProjectItemActionHandler = new ProjectItemActionHandler(mListener);
            mItemProjectBinding.setHandler(mProjectItemActionHandler);
        }

        void bindData(Project project, int position) {
            if (project == null) return;
            mItemProjectBinding.setProject(project);
            mItemProjectBinding.setPosition(position);
            mItemProjectBinding.executePendingBindings();
        }
    }

    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProjectBinding itemProjectBinding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_project, parent, false);
        return new ProjectsAdapter.ViewHolder(itemProjectBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mProjects.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null != mProjects ? mProjects.size() : 0;
    }

    public int getRealItemCount() {
        return null != mProjectsCopy ? mProjectsCopy.size() : 0;
    }

    public void filter(String text) {
        mProjects.clear();
        if (text.trim().equals("") || text.isEmpty()) {
            mProjects.addAll(mProjectsCopy);
        } else {
            text = text.toLowerCase();
            for (Project item : mProjectsCopy) {
                if (item.getTitle().toLowerCase().contains(text)) {
                    mProjects.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
