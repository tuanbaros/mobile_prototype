package com.framgia.mobileprototype.projectdetail;

import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ItemProjectSpinnerBinding;

import java.util.List;

/**
 * Created by tuannt on 12/04/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class ProjectSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<Project> mProjects;

    public ProjectSpinnerAdapter(List<Project> projects) {
        if (projects == null || projects.size() < 1) return;
        mProjects = projects;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public int getCount() {
        return mProjects == null ? 0 : mProjects.size();
    }

    @Override
    public Object getItem(int i) {
        return mProjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Project project = mProjects.get(i);
        ItemProjectSpinnerBinding itemBinding;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            itemBinding = ItemProjectSpinnerBinding.inflate(inflater, viewGroup, false);
        } else {
            itemBinding = DataBindingUtil.getBinding(view);
        }
        itemBinding.setProject(project);
        itemBinding.executePendingBindings();
        return itemBinding.getRoot();
    }

    @Override
    public int getItemViewType(int i) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        Project project = mProjects.get(i);
        ItemProjectSpinnerBinding itemBinding;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            itemBinding = ItemProjectSpinnerBinding.inflate(inflater, viewGroup, false);
        } else {
            itemBinding = DataBindingUtil.getBinding(view);
        }
        itemBinding.setProject(project);
        itemBinding.executePendingBindings();
        return itemBinding.getRoot();
    }
}
