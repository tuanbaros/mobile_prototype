package com.framgia.mobileprototype.comment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Comment;
import com.framgia.mobileprototype.databinding.ItemCommentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.comment
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Comment> mComments = new ArrayList<>();
    private CommentContract.Presenter mPresenter;
    private static final int COMMENT_TYPE = 0;
    private static final int LOAD_MORE_TYPE = 1;
    public CommentAdapter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void updateData(List<Comment> comments) {
        if (comments == null || comments.size() == 0) return;
        mComments.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mComments.get(position) == null) {
            return LOAD_MORE_TYPE;
        }
        return COMMENT_TYPE;
    }

    public void clearData() {
        mComments.clear();
    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addLoadMoreView() {
        mComments.add(null);
        notifyItemInserted(mComments.size() - 1);
    }

    public void removeLoadMoreView() {
        if (mComments.size() > 0 && mComments.get(mComments.size() - 1) == null) {
            mComments.remove(mComments.size() - 1);
            notifyItemRemoved(mComments.size() - 1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMENT_TYPE) {
            ItemCommentBinding itemCommentBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_comment, parent, false);
            return new CommentAdapter.ViewHolder(itemCommentBinding);
        }
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_load_more, parent, false);
        return new CommentAdapter.LoadMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentAdapter.ViewHolder) {
            ((CommentAdapter.ViewHolder) holder).bindData(mComments.get(position), position);
        }
        if (holder instanceof LoadMoreViewHolder) {
            ((LoadMoreViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding mItemCommentBinding;

        private CommentItemHandler mCommentItemHandler;

        public ViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            mItemCommentBinding = itemCommentBinding;
            mCommentItemHandler = new CommentItemHandler();
            mItemCommentBinding.setHandler(mCommentItemHandler);
        }

        public void bindData(Comment comment, int position) {
            if (comment == null) return;
            RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams)mItemCommentBinding.linearComment.getLayoutParams();
            if (position == mComments.size() - 1) {
                params.bottomMargin = mItemCommentBinding.getRoot()
                    .getContext()
                    .getResources()
                    .getDimensionPixelOffset(R.dimen.dp_52);
            } else {
                params.bottomMargin = 0;
            }
            mItemCommentBinding.setComment(comment);
            mItemCommentBinding.executePendingBindings();
        }
    }
}
