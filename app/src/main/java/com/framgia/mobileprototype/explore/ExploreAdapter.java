package com.framgia.mobileprototype.explore;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ItemExploreBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder>  {
    private List<Project> mProjects = new ArrayList<>();
    private ExploreContract.Presenter mPresenter;

    public ExploreAdapter(ExploreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void updateData(List<Project> projects) {
        if (projects == null || projects.size() == 0) return;
        mProjects.addAll(projects);
        notifyDataSetChanged();
    }

    public void clearData() {
        mProjects.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemExploreBinding itemExploreBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()), R.layout.item_explore, parent, false);
        return new ViewHolder(itemExploreBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mProjects.get(position));
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemExploreBinding mItemExploreBinding;

        private ExploreItemHandler mExploreItemHandler;

        public ViewHolder(ItemExploreBinding itemExploreBinding) {
            super(itemExploreBinding.getRoot());
            mItemExploreBinding = itemExploreBinding;
            mExploreItemHandler = new ExploreItemHandler(mPresenter);
            mItemExploreBinding.setHandler(mExploreItemHandler);
        }

        public void bindData(Project project) {
            if (project == null) return;
            mItemExploreBinding.setProject(project);
            mItemExploreBinding.executePendingBindings();
        }
    }
}
