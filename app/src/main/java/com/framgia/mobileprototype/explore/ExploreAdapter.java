package com.framgia.mobileprototype.explore;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
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
public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Project> mProjects = new ArrayList<>();
    private ExploreContract.Presenter mPresenter;
    private static final int PROJECT_TYPE = 0;
    private static final int LOAD_MORE_TYPE = 1;

    public ExploreAdapter(ExploreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void updateData(List<Project> projects) {
        if (projects == null || projects.size() == 0) return;
        mProjects.addAll(projects);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProjects.get(position) == null) {
            return LOAD_MORE_TYPE;
        }
        return PROJECT_TYPE;
    }

    public void clearData() {
        mProjects.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PROJECT_TYPE) {
            ItemExploreBinding itemExploreBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_explore, parent, false);
            return new ViewHolder(itemExploreBinding);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more,
                parent, false);
        return new LoadMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExploreAdapter.ViewHolder) {
            ((ExploreAdapter.ViewHolder) holder).bindData(mProjects.get(position));
        }
        if (holder instanceof LoadMoreViewHolder) {
            ((LoadMoreViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
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

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addLoadMoreView() {
        mProjects.add(null);
        notifyItemInserted(mProjects.size() - 1);
    }

    public void removeLoadMoreView() {
        if (mProjects.size() > 0 && mProjects.get(mProjects.size() - 1) == null) {
            mProjects.remove(mProjects.size() - 1);
            notifyItemRemoved(mProjects.size() - 1);
        }
    }
}
