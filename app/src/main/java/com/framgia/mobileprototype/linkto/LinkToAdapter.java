package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.databinding.ItemLinkToBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class LinkToAdapter extends RecyclerView.Adapter<LinkToAdapter.ViewHolder> {
    private List<Mock> mMocks = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private LinkToContract.Presenter mListener;

    public LinkToAdapter(Context context, List<Mock> mocks,
                         LinkToContract.Presenter listener) {
        if (mocks != null) mMocks.addAll(mocks);
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    public void updateData(Mock mock) {
        if (mock != null) {
            mMocks.add(mock);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLinkToBinding mItemLinkToBinding;
        private LinkToItemActionHandler mLinkToItemActionHandler;

        ViewHolder(ItemLinkToBinding itemLinkToBinding) {
            super(itemLinkToBinding.getRoot());
            mItemLinkToBinding = itemLinkToBinding;
            mLinkToItemActionHandler = new LinkToItemActionHandler(mListener);
            mItemLinkToBinding.setHandler(mLinkToItemActionHandler);
        }

        void bindData(Mock mock, int position) {
            if (mock == null) return;
            mItemLinkToBinding.setMock(mock);
            mItemLinkToBinding.setPosition(position);
            mItemLinkToBinding.executePendingBindings();
        }
    }

    @Override
    public LinkToAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLinkToBinding itemLinkToBinding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_link_to, parent, false);
        return new LinkToAdapter.ViewHolder(itemLinkToBinding);
    }

    @Override
    public void onBindViewHolder(final LinkToAdapter.ViewHolder holder, final int position) {
        holder.bindData(mMocks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null != mMocks ? mMocks.size() : 0;
    }
}
